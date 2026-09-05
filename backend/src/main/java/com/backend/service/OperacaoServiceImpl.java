package com.backend.service;

import com.backend.dto.OperacaoRequest;
import com.backend.dto.OperacaoResponse;
import com.backend.dto.SaldoResponse;
import com.backend.entity.Auth;
import com.backend.entity.Operacao;
import com.backend.entity.Operacao.TipoOperacao;
import com.backend.repository.AuthRepository;
import com.backend.repository.OperacaoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class OperacaoServiceImpl implements OperacaoService {

    private final OperacaoRepository operacaoRepository;
    private final AuthRepository authRepository;

    public OperacaoServiceImpl(OperacaoRepository operacaoRepository, AuthRepository authRepository) {
        this.operacaoRepository = operacaoRepository;
        this.authRepository = authRepository;
    }

    @Override
    public OperacaoResponse depositar(String token, OperacaoRequest request) {
        Auth usuario = autenticar(token);
        BigDecimal valor = validarValor(request);

        Operacao operacao = new Operacao();
        operacao.setTipo(TipoOperacao.DEPOSITO);
        operacao.setValor(valor);
        operacao.setDescricao(request.getDescricao());
        operacao.setOrigem(usuario);
        operacao.setDestino(usuario);

        return new OperacaoResponse(operacaoRepository.save(operacao));
    }

    @Override
    public OperacaoResponse sacar(String token, OperacaoRequest request) {
        Auth usuario = autenticar(token);
        BigDecimal valor = validarValor(request);
        validarSaldoSuficiente(usuario, valor);

        Operacao operacao = new Operacao();
        operacao.setTipo(TipoOperacao.SAQUE);
        operacao.setValor(valor);
        operacao.setDescricao(request.getDescricao());
        operacao.setOrigem(usuario);

        return new OperacaoResponse(operacaoRepository.save(operacao));
    }

    @Override
    public OperacaoResponse transferir(String token, OperacaoRequest request) {
        Auth origem = autenticar(token);
        BigDecimal valor = validarValor(request);

        if (request.getDestinoUsername() == null || request.getDestinoUsername().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe o usuário de destino");
        }

        Auth destino = authRepository.findByUsername(request.getDestinoUsername().trim())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário de destino não encontrado"));

        if (destino.getId().equals(origem.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Não é possível transferir para si mesmo");
        }

        validarSaldoSuficiente(origem, valor);

        Operacao operacao = new Operacao();
        operacao.setTipo(TipoOperacao.TRANSFERENCIA);
        operacao.setValor(valor);
        operacao.setDescricao(request.getDescricao());
        operacao.setOrigem(origem);
        operacao.setDestino(destino);

        return new OperacaoResponse(operacaoRepository.save(operacao));
    }

    @Override
    public SaldoResponse saldo(String token) {
        Auth usuario = autenticar(token);
        return new SaldoResponse(calcularSaldo(usuario));
    }

    @Override
    public List<OperacaoResponse> extrato(String token) {
        Auth usuario = autenticar(token);
        return operacaoRepository.findByOrigemOrDestinoOrderByCreatedAtDesc(usuario, usuario)
                .stream()
                .map(OperacaoResponse::new)
                .toList();
    }

    private Auth autenticar(String header) {
        if (header == null || header.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token não informado");
        }
        String token = header.startsWith("Bearer ") ? header.substring(7) : header;
        return authRepository.findByToken(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token inválido"));
    }

    private BigDecimal validarValor(OperacaoRequest request) {
        if (request.getValor() == null || request.getValor().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O valor deve ser maior que zero");
        }
        return request.getValor();
    }

    private void validarSaldoSuficiente(Auth usuario, BigDecimal valor) {
        if (calcularSaldo(usuario).compareTo(valor) < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Saldo insuficiente");
        }
    }

    private BigDecimal calcularSaldo(Auth usuario) {
        BigDecimal creditos = operacaoRepository.somaCreditos(usuario);
        BigDecimal debitos = operacaoRepository.somaDebitos(usuario);
        return creditos.subtract(debitos);
    }
}
