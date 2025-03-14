package org.example.eksamen3sembackend.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.eksamen3sembackend.dto.DroneDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("Test")
class DroneControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAllDrones() throws Exception {
        mockMvc.perform(get("/api/v1/drones"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void testAddDrone() throws Exception {
        mockMvc.perform(post("/api/v1/drones/add"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.droneId").exists())
                .andExpect(jsonPath("$.status").value("I_DRIFT"))
                .andExpect(jsonPath("$.publicSerialNumber").isNotEmpty());
    }

    @Test
    void testEnableDrone() throws Exception {
        String droneJson = mockMvc.perform(post("/api/v1/drones/add"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        DroneDTO createdDrone = objectMapper.readValue(droneJson, DroneDTO.class);

        mockMvc.perform(post("/api/v1/drones/disable")
                        .param("id", String.valueOf(createdDrone.droneId())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UDE_AF_DRIFT"));

        mockMvc.perform(post("/api/v1/drones/enable")
                        .param("id", String.valueOf(createdDrone.droneId())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("I_DRIFT"));
    }

    @Test
    void testRetireDrone() throws Exception {
        String droneJson = mockMvc.perform(post("/api/v1/drones/add"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        DroneDTO createdDrone = objectMapper.readValue(droneJson, DroneDTO.class);

        mockMvc.perform(post("/api/v1/drones/retire")
                        .param("id", String.valueOf(createdDrone.droneId())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UDFASET"));
    }

    @Test
    void testDisableDrone() throws Exception {
        String droneJson = mockMvc.perform(post("/api/v1/drones/add"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        DroneDTO createdDrone = objectMapper.readValue(droneJson, DroneDTO.class);

        mockMvc.perform(post("/api/v1/drones/disable")
                        .param("id", String.valueOf(createdDrone.droneId())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UDE_AF_DRIFT"));
    }
}
