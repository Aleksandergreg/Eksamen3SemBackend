package org.example.eksamen3sembackend.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.eksamen3sembackend.dto.CreateDeliveryDTO;
import org.example.eksamen3sembackend.dto.DeliveryDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("Test")
class DeliveryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("GET /api/v1/deliveries -> should return a list (200 OK)")
    void testGetAllUndelivered() throws Exception {
        mockMvc.perform(get("/api/v1/deliveries"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @DisplayName("POST /api/v1/deliveries/add -> should create a new delivery (200 OK)")
    void testAddDelivery() throws Exception {
        CreateDeliveryDTO request = new CreateDeliveryDTO(1L, "Test Address");
        String requestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/v1/deliveries/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.deliveryId").exists())
                .andExpect(jsonPath("$.pizzaId").value(1))
                .andExpect(jsonPath("$.address").value("Test Address"))
                .andExpect(jsonPath("$.expectedDeliveryTime").exists())
                .andExpect(jsonPath("$.actualDeliveryTime").isEmpty());
    }

    @Test
    @DisplayName("GET /api/v1/deliveries/queue -> should return all unassigned deliveries (200 OK)")
    void testGetAllUnassignedDeliveries() throws Exception {
        mockMvc.perform(get("/api/v1/deliveries/queue"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("POST /api/v1/deliveries/schedule -> schedule a drone to a delivery (200 OK)")
    void testScheduleDelivery() throws Exception {
        CreateDeliveryDTO req = new CreateDeliveryDTO(1L, "New Scheduling Address");
        String reqJson = objectMapper.writeValueAsString(req);

        String deliveryJson = mockMvc.perform(post("/api/v1/deliveries/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reqJson))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        DeliveryDTO createdDelivery = objectMapper.readValue(deliveryJson, DeliveryDTO.class);
        Long createdDeliveryId = createdDelivery.deliveryId();


        Long droneId = 1L;

        mockMvc.perform(post("/api/v1/deliveries/schedule")
                        .param("deliveryId", String.valueOf(createdDeliveryId))
                        .param("droneId", String.valueOf(droneId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.deliveryId").value(createdDeliveryId))
                .andExpect(jsonPath("$.droneId").value(droneId));
    }

    @Test
    @DisplayName("POST /api/v1/deliveries/finish -> finish an assigned delivery (200 OK)")
    void testFinishDelivery() throws Exception {
        CreateDeliveryDTO req = new CreateDeliveryDTO(1L, "Finish Address");
        String reqJson = objectMapper.writeValueAsString(req);

        String deliveryJson = mockMvc.perform(post("/api/v1/deliveries/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reqJson))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        DeliveryDTO createdDelivery = objectMapper.readValue(deliveryJson, DeliveryDTO.class);
        Long createdDeliveryId = createdDelivery.deliveryId();

        mockMvc.perform(post("/api/v1/deliveries/schedule")
                        .param("deliveryId", String.valueOf(createdDeliveryId))
                        .param("droneId", "1"))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/v1/deliveries/finish")
                        .param("deliveryId", String.valueOf(createdDeliveryId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.deliveryId").value(createdDeliveryId))
                .andExpect(jsonPath("$.actualDeliveryTime").isNotEmpty());
    }
}
