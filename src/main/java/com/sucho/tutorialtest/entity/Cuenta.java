package com.sucho.tutorialtest.entity;

import com.sucho.tutorialtest.exception.DineroInsuficienteException;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;


@Data
@AllArgsConstructor
@Entity
@Table(name = "cuenta")
public class Cuenta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String persona;

    private BigDecimal saldo;

    public void realizarDebito(BigDecimal monto) {
        BigDecimal nuevoSaldo = saldo.subtract(monto);

        if (nuevoSaldo.compareTo(BigDecimal.ZERO) < 0) {
            throw new DineroInsuficienteException("Dinero insuficiente en la cuenta");
        }
        saldo = nuevoSaldo;
    }

    public void realizarCredito(BigDecimal monto) {
        saldo = saldo.add(monto);
    }
}
