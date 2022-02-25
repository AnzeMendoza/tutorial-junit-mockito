package com.sucho.tutorialtest.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransaccionDTO {
    private long cuentaOrigenId;
    private long cuentaDestinoId;
    private BigDecimal monto;
    private long bancoId;
}
