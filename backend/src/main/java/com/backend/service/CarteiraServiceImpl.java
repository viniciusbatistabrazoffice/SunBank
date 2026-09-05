package com.backend.service;

import com.backend.dto.CarteiraEnviarRequest;
import com.backend.dto.CarteiraEnvioResponse;
import com.backend.dto.CarteiraResponse;
import com.backend.dto.CarteiraSaldoResponse;
import com.backend.entity.Auth;
import com.backend.entity.Carteira;
import com.backend.repository.AuthRepository;
import com.backend.repository.CarteiraRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.Keys;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

@Service
public class CarteiraServiceImpl implements CarteiraService {

    private static final int GCM_TAG_BITS = 128;
    private static final int GCM_IV_BYTES = 12;

    private final CarteiraRepository carteiraRepository;
    private final AuthRepository authRepository;
    private final Web3j web3j;
    private final long chainId;
    private final byte[] chaveCriptografia;
    private final String nomeRede;

    public CarteiraServiceImpl(CarteiraRepository carteiraRepository,
                               AuthRepository authRepository,
                               @Value("${ethereum.rpc-url}") String rpcUrl,
                               @Value("${ethereum.chain-id}") long chainId,
                               @Value("${wallet.encryption-secret}") String segredo) {
        this.carteiraRepository = carteiraRepository;
        this.authRepository = authRepository;
        this.web3j = Web3j.build(new HttpService(rpcUrl));
        this.chainId = chainId;
        this.chaveCriptografia = derivarChave(segredo);
        this.nomeRede = chainId == 11155111L ? "sepolia" : "chain-" + chainId;
    }

    @Override
    @Transactional
    public CarteiraResponse criar(String token) {
        Auth usuario = autenticar(token);

        if (carteiraRepository.findByUsuario(usuario).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "O usuário já possui uma carteira");
        }

        try {
            Credentials credentials = Credentials.create(Keys.createEcKeyPair());
            String chavePrivada = credentials.getEcKeyPair().getPrivateKey().toString(16);

            Carteira carteira = new Carteira();
            carteira.setUsuario(usuario);
            carteira.setEndereco(credentials.getAddress());
            carteira.setChavePrivadaCriptografada(criptografar(chavePrivada));

            return toResponse(carteiraRepository.save(carteira));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao gerar carteira");
        }
    }

    @Override
    public CarteiraResponse consultar(String token) {
        return toResponse(carteiraDoUsuario(autenticar(token)));
    }

    @Override
    public CarteiraSaldoResponse saldo(String token) {
        Carteira carteira = carteiraDoUsuario(autenticar(token));

        try {
            BigInteger wei = web3j.ethGetBalance(carteira.getEndereco(), DefaultBlockParameterName.LATEST)
                    .send()
                    .getBalance();
            return new CarteiraSaldoResponse(carteira.getEndereco(), wei, Convert.fromWei(wei.toString(), Convert.Unit.ETHER));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Erro ao consultar saldo na rede Ethereum");
        }
    }

    @Override
    public CarteiraEnvioResponse enviar(String token, CarteiraEnviarRequest request) {
        Carteira carteira = carteiraDoUsuario(autenticar(token));

        if (request.getDestino() == null || request.getDestino().isBlank()
                || request.getValorEth() == null || request.getValorEth().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe um endereço de destino e um valor maior que zero");
        }

        if (!request.getDestino().matches("^0x[0-9a-fA-F]{40}$")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Endereço de destino inválido");
        }

        try {
            String chavePrivada = descriptografar(carteira.getChavePrivadaCriptografada());
            Credentials credentials = Credentials.create(chavePrivada);

            TransactionReceipt receipt = new Transfer(web3j, new RawTransactionManager(web3j, credentials, chainId))
                    .sendFunds(request.getDestino(), request.getValorEth(), Convert.Unit.ETHER)
                    .send();

            String status = receipt.isStatusOK() ? "CONFIRMADA" : "FALHOU";
            return new CarteiraEnvioResponse(
                    receipt.getTransactionHash(),
                    carteira.getEndereco(),
                    request.getDestino(),
                    request.getValorEth(),
                    status
            );
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Erro ao enviar transação: " + e.getMessage());
        }
    }

    private CarteiraResponse toResponse(Carteira carteira) {
        return new CarteiraResponse(carteira.getId(), carteira.getEndereco(), nomeRede, carteira.getCreatedAt());
    }

    private Carteira carteiraDoUsuario(Auth usuario) {
        return carteiraRepository.findByUsuario(usuario)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Carteira não encontrada. Crie uma em POST /api/carteira"));
    }

    private Auth autenticar(String header) {
        if (header == null || header.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token não informado");
        }
        String token = header.startsWith("Bearer ") ? header.substring(7) : header;
        return authRepository.findByToken(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token inválido"));
    }

    private byte[] derivarChave(String segredo) {
        try {
            return MessageDigest.getInstance("SHA-256").digest(segredo.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new IllegalStateException("Erro ao derivar chave de criptografia", e);
        }
    }

    private String criptografar(String texto) throws Exception {
        byte[] iv = new byte[GCM_IV_BYTES];
        new SecureRandom().nextBytes(iv);

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(chaveCriptografia, "AES"), new GCMParameterSpec(GCM_TAG_BITS, iv));

        byte[] dados = cipher.doFinal(texto.getBytes(StandardCharsets.UTF_8));
        byte[] resultado = new byte[iv.length + dados.length];
        System.arraycopy(iv, 0, resultado, 0, iv.length);
        System.arraycopy(dados, 0, resultado, iv.length, dados.length);
        return Base64.getEncoder().encodeToString(resultado);
    }

    private String descriptografar(String textoCriptografado) throws Exception {
        byte[] resultado = Base64.getDecoder().decode(textoCriptografado);
        byte[] iv = new byte[GCM_IV_BYTES];
        byte[] dados = new byte[resultado.length - GCM_IV_BYTES];
        System.arraycopy(resultado, 0, iv, 0, iv.length);
        System.arraycopy(resultado, iv.length, dados, 0, dados.length);

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(chaveCriptografia, "AES"), new GCMParameterSpec(GCM_TAG_BITS, iv));
        return new String(cipher.doFinal(dados), StandardCharsets.UTF_8);
    }
}
