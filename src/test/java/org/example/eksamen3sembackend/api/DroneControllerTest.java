package org.example.eksamen3sembackend.api;

import com.fasterxml.jackson.databind.ObjectMapper;
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

class DroneControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @Test
    void createDrone() throws Exception {

        mockMvc.perform(post("/api/v1/drones/add")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.droneId").exists())
                .andExpect(jsonPath("$.publicSerialNumber").isNotEmpty())
                .andExpect(jsonPath("$.status").value("I_DRIFT"))
                .andExpect(jsonPath("$.station").exists());
    }
}
