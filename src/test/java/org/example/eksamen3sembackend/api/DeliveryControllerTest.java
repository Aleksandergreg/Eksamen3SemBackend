package org.example.eksamen3sembackend.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.eksamen3sembackend.dto.CreateDeliveryDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class DeliveryControllerTest {

    @Autowired
    private MockMvc mockMvc;

   @Autowired
   private ObjectMapper objectMapper;

    @Test
    void createDelivery() throws Exception {


        CreateDeliveryDTO request = new CreateDeliveryDTO(1L, "Test Address");

        String jsonRequest = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/v1/deliveries/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.deliveryId").exists())
                .andExpect(jsonPath("$.pizzaId").value(1))
                .andExpect(jsonPath("$.address").value("Test Address"))
                .andExpect(jsonPath("$.expectedDeliveryTime").exists())
                .andExpect(jsonPath("$.actualDeliveryTime").isEmpty());
    }
}
