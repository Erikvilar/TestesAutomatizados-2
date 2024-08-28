package com.iftm.client.resources;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.iftm.client.dto.ClientDTO;
import com.iftm.client.entities.Client;
import com.iftm.client.repositories.ClientRepository;
import com.iftm.client.services.ClientService;

//necessário para utilizar o MockMVC
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ClientResourceIntegrationTest {
    @Autowired
    private MockMvc mockMVC;

    @Autowired
    private ClientService service;

   @Autowired
    private ClientRepository repository;

     @BeforeEach
    public void setUp() {
        repository.deleteAll(); // Limpa a base de dados antes de cada teste

        // Insere os registros necessários para os testes
        repository.save(new Client(7L, "Jose Saramago", "10239254871", 5000.0, Instant.parse("1996-12-23T07:00:00Z"), 0));
        repository.save(new Client(8L, "Toni Morrison", "10219344681", 10000.0, Instant.parse("1940-02-23T07:00:00Z"), 0));
        repository.save(new Client(9L, "Virginia Woolf", "10429355681", 7500.0, Instant.parse("1980-01-15T07:00:00Z"), 2));
    }
    
    @Test
    @DisplayName("Verificar se o endpoint get/clients/ retorna todos os clientes existentes")
    public void testarEndPointListarTodosClientesRetornaCorreto() throws Exception{
        //arrange


      Double salarioResultado = 5000.0;
    PageRequest pageRequest = PageRequest.of(0, 10);
        List<ClientDTO> listaClientes = new ArrayList<>();
        listaClientes.add(new ClientDTO(new Client(7L, "Jose Saramago", "10239254871", salarioResultado, Instant.parse("1996-12-23T07:00:00Z"), 0)));
        listaClientes.add(new ClientDTO(new Client(8L, "Toni Morrison", "10219344681", salarioResultado, Instant.parse("1940-02-23T07:00:00Z"), 0)));

        Page<ClientDTO> page = new PageImpl<>(listaClientes, pageRequest, listaClientes.size());

        ResultActions result = mockMVC.perform(get("/clients/income/")
        .param("income", String.valueOf(salarioResultado))
        .param("page", "0") // Parâmetros de paginação
        .param("size", "10")
        .accept(MediaType.APPLICATION_JSON));


        //assign
        result.andExpect(status().isOk())
        .andExpect(jsonPath("$.content").exists())
        .andExpect(jsonPath("$.content").isArray())
        .andExpect(jsonPath("$.content.length()").value(listaClientes.size()))
        .andExpect(jsonPath("$.content[0].id").value(7L))
        .andExpect(jsonPath("$.content[0].name").value("Jose Saramago"))
        .andExpect(jsonPath("$.content[0].income").value(salarioResultado))
        .andExpect(jsonPath("$.content[1].id").value(8L))
        .andExpect(jsonPath("$.content[1].name").value("Toni Morrison"))
        .andExpect(jsonPath("$.content[1].income").value(salarioResultado))
        .andExpect(jsonPath("$.totalElements").value(listaClientes.size()));
}
}
