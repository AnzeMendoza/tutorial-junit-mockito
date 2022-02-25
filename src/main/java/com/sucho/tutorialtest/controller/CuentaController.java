package com.sucho.tutorialtest.controller;

import com.sucho.tutorialtest.entity.Cuenta;
import com.sucho.tutorialtest.entity.TransaccionDTO;
import com.sucho.tutorialtest.service.CuentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/cuentas")
public class CuentaController {
    @Autowired
    private CuentaService cuentaService;

    @GetMapping("/")
    public List<Cuenta> listarCuentas(){
        return cuentaService.findAll();
    }

    @GetMapping("/{id}")
    public Cuenta verDetaller(@PathVariable long id){
        return cuentaService.findById(id);
    }

    @PostMapping("/")
    public Cuenta guardarCuenta(@RequestBody Cuenta cuenta){
        return cuentaService.save(cuenta);
    }

    @PostMapping("/transferir")
    public ResponseEntity<?> tranferirDinero(@RequestBody TransaccionDTO transaccionDTO){
        cuentaService.transferirDinero(transaccionDTO.getCuentaOrigenId()
                , transaccionDTO.getCuentaDestinoId()
                , transaccionDTO.getMonto()
                , transaccionDTO.getBancoId());
        Map<String, Object> respuesta = new HashMap<>();

        respuesta.put("date", LocalDate.now().toString());
        respuesta.put("status", "ok");
        respuesta.put("mensaje", "Transferencia realizada con exito");
        respuesta.put("transaccionDTO", transaccionDTO);

        return ResponseEntity.ok(respuesta);
    }

}
