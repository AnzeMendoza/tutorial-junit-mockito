package com.sucho.tutorialtest;

import com.sucho.tutorialtest.entity.Banco;
import com.sucho.tutorialtest.entity.Cuenta;

import java.math.BigDecimal;
import java.util.Optional;

public class Datos {

    public static Optional<Cuenta> crearCuenta001(){
        return Optional.of(new Cuenta(1L, "Franck", new BigDecimal("1000"))) ;
    }

    public static Optional<Cuenta> crearCuenta002(){
        return Optional.of(new Cuenta(2L, "Nicolina", new BigDecimal("2000"))) ;
    }

    public static Optional<Banco> crearBanco(){
        return Optional.of(new Banco(1L, "Banco Nación",0));
    }
}
