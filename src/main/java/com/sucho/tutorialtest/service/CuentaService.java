package com.sucho.tutorialtest.service;

import com.sucho.tutorialtest.entity.Cuenta;

import java.math.BigDecimal;
import java.util.List;

public interface CuentaService {
    List<Cuenta> findAll();
    Cuenta findById(Long id);
    Cuenta save(Cuenta cuenta);
    int revisarTotalDeTransferencias(Long bancoId);
    BigDecimal revisarSaldo(Long cuentaId);
    void transferirDinero(Long numeroCuentaOrigen, Long numeroCuentaDestino, BigDecimal monto, Long banco);
}
