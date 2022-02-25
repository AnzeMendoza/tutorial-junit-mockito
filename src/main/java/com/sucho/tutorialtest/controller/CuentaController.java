package com.sucho.tutorialtest.controller;

import com.sucho.tutorialtest.entity.Cuenta;
import com.sucho.tutorialtest.entity.TransaccionDTO;
import com.sucho.tutorialtest.service.CuentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/cuentas")
public class CuentaController {
    @Autowired
    private CuentaService cuentaService;

    @GetMapping("/{id}")
    public Cuenta verDetaller(@PathVariable long id){
        return cuentaService.findById(id);
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
        respuesta.put("transaccion", transaccionDTO);

        return ResponseEntity.ok(respuesta);
    }

}
