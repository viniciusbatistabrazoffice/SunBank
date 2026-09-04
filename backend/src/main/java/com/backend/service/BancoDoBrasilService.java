package com.backend.service;

import com.backend.config.BancoDoBrasilConfig;
import com.backend.dto.bb.TokenResponse;
import com.backend.dto.bb.TransferenciaRequest;
import com.backend.dto.bb.TransferenciaResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Collections;

/**
 * Integração com a API de Transferências do Banco do Brasil.
 *
 * Fluxo implementado:
 * 1. Valida configuração e credenciais.
 * 2. Obtém token OAuth2 via client_credentials.
 * 3. Envia a transferência para o endpoint configurado.
 * 4. Retorna o identificador da transação bancária.
 *
 * Os campos do DTO TransferenciaRequest seguem o padrão da API TED/DOC do BB.
 * Ajuste os nomes de propriedade conforme a documentação oficial/homologação obtida
 * no portal developers.bb.com.br.
 */
@Service
public class BancoDoBrasilService {

    private final BancoDoBrasilConfig config;
    private final RestTemplate restTemplate;

    private volatile String accessToken;
    private volatile long tokenExpiresAt;

    public BancoDoBrasilService(BancoDoBrasilConfig config) {
        this.config = config;
        this.restTemplate = new RestTemplate();
    }

    /**
     * Realiza uma transferência bancária via API do Banco do Brasil.
     *
     * @param valor        valor a ser transferido
     * @param contaDestino dados da conta de destino (formato ajustável conforme API)
     * @return identificador da transação bancária
     */
    public String transferir(BigDecimal valor, String contaDestino) {
        if (!config.isEnabled()) {
            throw new UnsupportedOperationException(
                    "Integração com Banco do Brasil está desabilitada. "
                            + "Configure sunbank.bancodobrasil.enabled=true e as credenciais oficiais para prosseguir."
            );
        }

        validarConfiguracao();

        String token = obterToken();

        TransferenciaRequest request = montarRequisicao(valor, contaDestino);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<TransferenciaRequest> entity = new HttpEntity<>(request, headers);

        try {
            ResponseEntity<TransferenciaResponse> response = restTemplate.exchange(
                    config.getApiUrl(),
                    HttpMethod.POST,
                    entity,
                    TransferenciaResponse.class
            );

            TransferenciaResponse body = response.getBody();
            if (body == null || body.getIdentificadorTransferencia() == null) {
                throw new RuntimeException("Resposta inesperada da API do Banco do Brasil");
            }
            return body.getIdentificadorTransferencia();
        } catch (RestClientException ex) {
            throw new RuntimeException("Falha na transferência via Banco do Brasil: " + ex.getMessage(), ex);
        }
    }

    private void validarConfiguracao() {
        if (isBlank(config.getApiUrl())) {
            throw new IllegalStateException("sunbank.bancodobrasil.api-url não configurada");
        }
        if (isBlank(config.getOauthTokenUrl())) {
            throw new IllegalStateException("sunbank.bancodobrasil.oauth-token-url não configurada");
        }
        if (isBlank(config.getClientId()) || isBlank(config.getClientSecret())) {
            throw new IllegalStateException("client-id e client-secret do Banco do Brasil não configurados");
        }
        if (isBlank(config.getAgenciaOrigem()) || isBlank(config.getContaOrigem())) {
            throw new IllegalStateException("agência e conta de origem do Banco do Brasil não configuradas");
        }
    }

    private synchronized String obterToken() {
        long now = System.currentTimeMillis();
        if (accessToken != null && now < tokenExpiresAt - 60000) {
            return accessToken;
        }

        String credentials = config.getClientId() + ":" + config.getClientSecret();
        String basicAuth = Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Basic " + basicAuth);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "client_credentials");

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<TokenResponse> response = restTemplate.exchange(
                    config.getOauthTokenUrl(),
                    HttpMethod.POST,
                    entity,
                    TokenResponse.class
            );

            TokenResponse token = response.getBody();
            if (token == null || token.getAccessToken() == null) {
                throw new RuntimeException("Não foi possível obter token OAuth2 do Banco do Brasil");
            }

            this.accessToken = token.getAccessToken();
            long validity = token.getExpiresIn() != null ? token.getExpiresIn() : 600;
            this.tokenExpiresAt = now + (validity * 1000);
            return this.accessToken;
        } catch (RestClientException ex) {
            throw new RuntimeException("Falha na autenticação OAuth2 do Banco do Brasil: " + ex.getMessage(), ex);
        }
    }

    private TransferenciaRequest montarRequisicao(BigDecimal valor, String contaDestino) {
        TransferenciaRequest request = new TransferenciaRequest();

        request.setNumeroRequisicao(gerarNumeroRequisicao());
        request.setAgenciaDebito(config.getAgenciaOrigem());
        request.setContaCorrenteDebito(config.getContaOrigem());

        if (contaDestino != null && contaDestino.contains("/")) {
            String[] parts = contaDestino.split("/");
            request.setAgenciaCredito(parts[0].trim());
            String[] contaDigito = parts[1].trim().split("-");
            request.setContaCorrenteCredito(contaDigito[0]);
            if (contaDigito.length > 1) {
                request.setDigitoVerificadorContaCorrenteCredito(contaDigito[1]);
            }
        } else if (contaDestino != null && contaDestino.contains("-")) {
            String[] contaDigito = contaDestino.trim().split("-");
            request.setContaCorrenteCredito(contaDigito[0]);
            if (contaDigito.length > 1) {
                request.setDigitoVerificadorContaCorrenteCredito(contaDigito[1]);
            }
        } else {
            request.setContaPagamentoCredito(contaDestino);
        }

        request.setValor(valor);
        request.setTipoTransferencia("TED");
        request.setDescricao("Transferencia SunBank");

        return request;
    }

    private String gerarNumeroRequisicao() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        return "SUN" + timestamp + System.currentTimeMillis() % 1000;
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
