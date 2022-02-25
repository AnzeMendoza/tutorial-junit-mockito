package com.sucho.tutorialtest.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sucho.tutorialtest.Datos;
import com.sucho.tutorialtest.entity.Cuenta;
import com.sucho.tutorialtest.entity.TransaccionDTO;
import com.sucho.tutorialtest.service.CuentaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CuentaController.class)
class CuentaControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CuentaService cuentaService;

    ObjectMapper objectMapper;

    @BeforeEach
    void configurar() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void verDetallesTest() throws Exception {
        when(cuentaService.findById(1L)).thenReturn(Datos.crearCuenta001().orElseThrow());

        mockMvc.perform(get("/api/cuentas/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.persona").value("Franck"))
                .andExpect(jsonPath("$.saldo").value("1000"));

        verify(cuentaService).findById(1L);
    }

    @Test
    void transferirDineroTest() throws Exception {
        TransaccionDTO transaccionDTO = new TransaccionDTO();
        transaccionDTO.setCuentaOrigenId(1L);
        transaccionDTO.setCuentaDestinoId(2L);
        transaccionDTO.setMonto(new BigDecimal("100"));
        transaccionDTO.setBancoId(1L);

        System.out.println(objectMapper.writeValueAsString(transaccionDTO));

        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("date", LocalDate.now().toString());
        respuesta.put("status", "ok");
        respuesta.put("mensaje", "Transferencia realizada con exito");
        respuesta.put("transaccionDTO", transaccionDTO);

        System.out.println(objectMapper.writeValueAsString(respuesta));

        mockMvc.perform(post("/api/cuentas/transferir").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transaccionDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.date").value(LocalDate.now().toString()))
                .andExpect(jsonPath("$.mensaje").value("Transferencia realizada con exito"))
                .andExpect(jsonPath("$.transaccionDTO.cuentaOrigenId").value(transaccionDTO.getCuentaOrigenId()))
                .andExpect(content().json(objectMapper.writeValueAsString(respuesta)));
    }

    @Test
    void listarCuentasTest() throws Exception {
        List<Cuenta> cuentas = Arrays.asList(Datos.crearCuenta001().orElseThrow(), Datos.crearCuenta002().orElseThrow());

        when(cuentaService.findAll()).thenReturn(cuentas);

        mockMvc.perform(get("/api/cuentas/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].persona").value("Franck"))
                .andExpect(jsonPath("$[0].saldo").value("1000"))
                .andExpect(jsonPath("$[1].persona").value("Nicolina"))
                .andExpect(jsonPath("$[1].saldo").value("2000"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(content().json(objectMapper.writeValueAsString(cuentas)));

        verify(cuentaService).findAll();
    }

    @Test
    void guardarCuentaTest() throws Exception {
        Cuenta cuenta = new Cuenta(100L, "Rosa", new BigDecimal("3000"));
        when(cuentaService.save(any())).then(
                invocation -> {
                    Cuenta c = invocation.getArgument(0);
                    c.setId(3L);
                    return c;
                }
        );

        //System.out.println(objectMapper.writeValueAsString(cuentaService.save(any())));

        mockMvc.perform(post("/api/cuentas/").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cuenta)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.persona", is("Rosa")))
                .andExpect(jsonPath("$.saldo", is(3000)));

        verify(cuentaService).save(any());
    }
}