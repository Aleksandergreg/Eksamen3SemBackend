package org.example.eksamen3sembackend.repository;

import org.example.eksamen3sembackend.model.Drone;
import org.example.eksamen3sembackend.model.DroneStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class DroneRepositoryTest {

    @Autowired
    private DroneRepository droneRepository;

    @BeforeEach
    void setUp() {


        Drone drone1 = new Drone("ABC123", DroneStatus.I_DRIFT);
        Drone drone2 = new Drone("XYZ789", DroneStatus.UDE_AF_DRIFT);

        droneRepository.save(drone1);
        droneRepository.save(drone2);
    }

    @Test
    void testFindAll() {
        List<Drone> allDrones = droneRepository.findAll();
        assertEquals(2, allDrones.size());
    }



    @Test
    void testSave() {
        Drone newDrone = new Drone("NEW999", DroneStatus.I_DRIFT);
        Drone savedDrone = droneRepository.save(newDrone);

        assertNotNull(savedDrone.getDroneId(), "Drone ID should be generated");
        assertEquals("NEW999", savedDrone.getPublicSerialNumber());
        assertEquals(DroneStatus.I_DRIFT, savedDrone.getStatus());
    }



    @Test
    void testDelete() {
        droneRepository.deleteById(1L);
        boolean exists = droneRepository.existsById(1L);
        assertFalse(exists, "Drone with ID=1 should be deleted");
    }
}
