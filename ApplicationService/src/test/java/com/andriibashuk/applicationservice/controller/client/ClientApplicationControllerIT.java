package com.andriibashuk.applicationservice.controller.client;

import com.andriibashuk.applicationservice.entity.Application;
import com.andriibashuk.applicationservice.request.NewApplicationRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureWireMock(port = 0)
@Transactional
class ClientApplicationControllerIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void newApplication() throws Exception {
        NewApplicationRequest newApplicationRequest = new NewApplicationRequest(500);
        String json = objectMapper.writeValueAsString(newApplicationRequest);

        stubFor(get(urlEqualTo("/show/1")).willReturn(ResponseDefinitionBuilder.responseDefinition().withStatus(HttpStatus.OK.value())));


         this.mockMvc.perform(
                post("/client/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("clientId", "1")
        ).andDo(print())
                 .andExpect(status().isCreated())
                 .andExpect(jsonPath("$.clientId").value(1))
                 .andExpect(jsonPath("$.id").isNumber())
                 .andExpect(jsonPath("$.requestedAmount").value(500))
                 .andExpect(jsonPath("$.status").value(Application.Status.NEW.toString()))
                 .andExpect(jsonPath("$.createdAt").isNotEmpty())
                 .andExpect(jsonPath("$.userId").isEmpty());

    }

    @Test
    void sign() {
    }
}