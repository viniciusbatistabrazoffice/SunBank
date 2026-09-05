package com.backend.service;

import com.backend.dto.CarteiraEnviarRequest;
import com.backend.dto.CarteiraEnvioResponse;
import com.backend.dto.CarteiraResponse;
import com.backend.dto.CarteiraSaldoResponse;

public interface CarteiraService {
    CarteiraResponse criar(String token);
    CarteiraResponse consultar(String token);
    CarteiraSaldoResponse saldo(String token);
    CarteiraEnvioResponse enviar(String token, CarteiraEnviarRequest request);
}
