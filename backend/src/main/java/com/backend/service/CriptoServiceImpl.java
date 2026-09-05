package com.backend.service;

import com.backend.dto.CriptoCarteiraResponse;
import com.backend.dto.CriptoOperacaoRequest;
import com.backend.dto.CriptoOperacaoResponse;
import com.backend.dto.CriptoPosicaoResponse;
import com.backend.entity.Auth;
import com.backend.entity.CriptoAtivo;
import com.backend.entity.CriptoOperacao;
import com.backend.entity.CriptoOperacao.TipoCriptoOperacao;
import com.backend.entity.Operacao;
import com.backend.entity.Operacao.TipoOperacao;
import com.backend.repository.AuthRepository;
import com.backend.repository.CriptoAtivoRepository;
import com.backend.repository.CriptoOperacaoRepository;
import com.backend.repository.OperacaoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class CriptoServiceImpl implements CriptoService {

    private final CriptoAtivoRepository criptoAtivoRepository;
    private final CriptoOperacaoRepository criptoOperacaoRepository;
    private final OperacaoRepository operacaoRepository;
    private final AuthRepository authRepository;
    private final CotacaoService cotacaoService;

    public CriptoServiceImpl(CriptoAtivoRepository criptoAtivoRepository,
                             CriptoOperacaoRepository criptoOperacaoRepository,
                             OperacaoRepository operacaoRepository,
                             AuthRepository authRepository,
                             CotacaoService cotacaoService) {
        this.criptoAtivoRepository = criptoAtivoRepository;
        this.criptoOperacaoRepository = criptoOperacaoRepository;
        this.operacaoRepository = operacaoRepository;
        this.authRepository = authRepository;
        this.cotacaoService = cotacaoService;
    }

    @Override
    public CriptoOperacaoResponse comprar(String token, CriptoOperacaoRequest request) {
        Auth usuario = autenticar(token);
        String simbolo = cotacaoService.normalizar(request.getSimbolo());
        BigDecimal quantidade = validarQuantidade(request);
        BigDecimal preco = cotacaoService.getPreco(simbolo);
        BigDecimal valorTotal = quantidade.multiply(preco).setScale(2, RoundingMode.HALF_UP);

        if (calcularSaldo(usuario).compareTo(valorTotal) < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Saldo insuficiente para comprar " + simbolo);
        }

        registrarMovimentacaoFiat(usuario, TipoOperacao.SAQUE, valorTotal,
                "Compra de " + quantidade + " " + simbolo);
        creditarAtivo(usuario, simbolo, quantidade);

        CriptoOperacao operacao = novaOperacao(TipoCriptoOperacao.COMPRA, simbolo, quantidade,
                preco, valorTotal, request.getDescricao(), usuario, usuario);
        return new CriptoOperacaoResponse(criptoOperacaoRepository.save(operacao));
    }

    @Override
    public CriptoOperacaoResponse vender(String token, CriptoOperacaoRequest request) {
        Auth usuario = autenticar(token);
        String simbolo = cotacaoService.normalizar(request.getSimbolo());
        BigDecimal quantidade = validarQuantidade(request);
        BigDecimal preco = cotacaoService.getPreco(simbolo);
        BigDecimal valorTotal = quantidade.multiply(preco).setScale(2, RoundingMode.HALF_UP);

        debitarAtivo(usuario, simbolo, quantidade);
        registrarMovimentacaoFiat(usuario, TipoOperacao.DEPOSITO, valorTotal,
                "Venda de " + quantidade + " " + simbolo);

        CriptoOperacao operacao = novaOperacao(TipoCriptoOperacao.VENDA, simbolo, quantidade,
                preco, valorTotal, request.getDescricao(), usuario, usuario);
        return new CriptoOperacaoResponse(criptoOperacaoRepository.save(operacao));
    }

    @Override
    public CriptoOperacaoResponse transferir(String token, CriptoOperacaoRequest request) {
        Auth origem = autenticar(token);
        String simbolo = cotacaoService.normalizar(request.getSimbolo());
        BigDecimal quantidade = validarQuantidade(request);

        if (request.getDestinoUsername() == null || request.getDestinoUsername().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe o usuário de destino");
        }

        Auth destino = authRepository.findByUsername(request.getDestinoUsername().trim())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário de destino não encontrado"));

        if (destino.getId().equals(origem.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Não é possível transferir para si mesmo");
        }

        debitarAtivo(origem, simbolo, quantidade);
        creditarAtivo(destino, simbolo, quantidade);

        CriptoOperacao operacao = novaOperacao(TipoCriptoOperacao.TRANSFERENCIA, simbolo, quantidade,
                null, null, request.getDescricao(), origem, destino);
        return new CriptoOperacaoResponse(criptoOperacaoRepository.save(operacao));
    }

    @Override
    public CriptoCarteiraResponse carteira(String token) {
        Auth usuario = autenticar(token);
        List<CriptoPosicaoResponse> posicoes = criptoAtivoRepository.findByUsuario(usuario)
                .stream()
                .filter(ativo -> ativo.getQuantidade().compareTo(BigDecimal.ZERO) > 0)
                .map(ativo -> new CriptoPosicaoResponse(
                        ativo.getSimbolo(), ativo.getQuantidade(), cotacaoService.getPreco(ativo.getSimbolo())))
                .toList();
        return new CriptoCarteiraResponse(usuario.getUsername(), posicoes);
    }

    @Override
    public List<CriptoOperacaoResponse> extrato(String token) {
        Auth usuario = autenticar(token);
        return criptoOperacaoRepository.findByOrigemOrDestinoOrderByCreatedAtDesc(usuario, usuario)
                .stream()
                .map(CriptoOperacaoResponse::new)
                .toList();
    }

    @Override
    public Map<String, BigDecimal> cotacoes() {
        return cotacaoService.listarCotacoes();
    }

    private Auth autenticar(String header) {
        if (header == null || header.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token não informado");
        }
        String token = header.startsWith("Bearer ") ? header.substring(7) : header;
        return authRepository.findByToken(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token inválido"));
    }

    private BigDecimal validarQuantidade(CriptoOperacaoRequest request) {
        if (request.getQuantidade() == null || request.getQuantidade().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A quantidade deve ser maior que zero");
        }
        return request.getQuantidade();
    }

    private void creditarAtivo(Auth usuario, String simbolo, BigDecimal quantidade) {
        CriptoAtivo ativo = criptoAtivoRepository.findByUsuarioAndSimbolo(usuario, simbolo)
                .orElseGet(() -> {
                    CriptoAtivo novo = new CriptoAtivo();
                    novo.setUsuario(usuario);
                    novo.setSimbolo(simbolo);
                    novo.setQuantidade(BigDecimal.ZERO);
                    return novo;
                });
        ativo.setQuantidade(ativo.getQuantidade().add(quantidade));
        criptoAtivoRepository.save(ativo);
    }

    private void debitarAtivo(Auth usuario, String simbolo, BigDecimal quantidade) {
        CriptoAtivo ativo = criptoAtivoRepository.findByUsuarioAndSimbolo(usuario, simbolo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Você não possui " + simbolo + " na carteira"));
        if (ativo.getQuantidade().compareTo(quantidade) < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Quantidade de " + simbolo + " insuficiente");
        }
        ativo.setQuantidade(ativo.getQuantidade().subtract(quantidade));
        criptoAtivoRepository.save(ativo);
    }

    private void registrarMovimentacaoFiat(Auth usuario, TipoOperacao tipo, BigDecimal valor, String descricao) {
        Operacao operacao = new Operacao();
        operacao.setTipo(tipo);
        operacao.setValor(valor);
        operacao.setDescricao(descricao);
        operacao.setOrigem(usuario);
        operacao.setDestino(tipo == TipoOperacao.DEPOSITO ? usuario : null);
        operacaoRepository.save(operacao);
    }

    private CriptoOperacao novaOperacao(TipoCriptoOperacao tipo, String simbolo, BigDecimal quantidade,
                                        BigDecimal preco, BigDecimal valorTotal, String descricao,
                                        Auth origem, Auth destino) {
        CriptoOperacao operacao = new CriptoOperacao();
        operacao.setTipo(tipo);
        operacao.setSimbolo(simbolo);
        operacao.setQuantidade(quantidade);
        operacao.setPrecoUnitario(preco);
        operacao.setValorTotal(valorTotal);
        operacao.setDescricao(descricao);
        operacao.setOrigem(origem);
        operacao.setDestino(destino);
        return operacao;
    }

    private BigDecimal calcularSaldo(Auth usuario) {
        BigDecimal creditos = operacaoRepository.somaCreditos(usuario);
        BigDecimal debitos = operacaoRepository.somaDebitos(usuario);
        return creditos.subtract(debitos);
    }
}
