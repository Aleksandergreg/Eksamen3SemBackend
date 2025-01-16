package org.example.eksamen3sembackend.service;

import org.example.eksamen3sembackend.dto.DroneDTO;
import org.example.eksamen3sembackend.dto.StationDTO;
import org.example.eksamen3sembackend.exception.ResourceNotFoundException;
import org.example.eksamen3sembackend.model.Drone;
import org.example.eksamen3sembackend.model.DroneStatus;
import org.example.eksamen3sembackend.model.Station;
import org.example.eksamen3sembackend.repository.DroneRepository;
import org.example.eksamen3sembackend.repository.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("Test")
class DroneServiceTest {

    @Mock
    private DroneRepository mockedDroneRepository;

    @Mock
    private StationRepository mockedStationRepository;

    private DroneService droneService;

    @BeforeEach
    void init() {

        Station s1 = new Station(55.41, 12.34);
        s1.setStationId(100L);
        Station s2 = new Station(55.42, 12.35);
        s2.setStationId(200L);

        Set<Drone> s1Drones = new HashSet<>();
        Drone existingDrone = new Drone("DRONE-XYZ", DroneStatus.UDE_AF_DRIFT);
        existingDrone.setDroneId(1L);
        existingDrone.setStation(s1);
        s1Drones.add(existingDrone);
        s1.setDrones(s1Drones);

        List<Station> stationList = new ArrayList<>();
        stationList.add(s1);
        stationList.add(s2);
        Mockito.when(mockedStationRepository.findAll()).thenReturn(stationList);


        Mockito.when(mockedDroneRepository.findAll()).thenReturn(List.of(existingDrone));

        Mockito.when(mockedDroneRepository.findById(1L)).thenReturn(Optional.of(existingDrone));

        Mockito.when(mockedDroneRepository.findById(999L)).thenReturn(Optional.empty());

        Mockito.when(mockedDroneRepository.save(ArgumentMatchers.any(Drone.class))).thenAnswer(new Answer<Drone>() {
            @Override
            public Drone answer(InvocationOnMock invocation) {
                Drone droneToSave = invocation.getArgument(0);
                if (droneToSave.getDroneId() == null) {
                    droneToSave.setDroneId(2L);
                }
                return droneToSave;
            }
        });

        droneService = new DroneService(mockedDroneRepository, mockedStationRepository);
    }

    @Test
    void testGetAllDrones() {
        List<DroneDTO> droneDTOs = droneService.getAllDrones();

        assertEquals(1, droneDTOs.size());
        assertEquals("DRONE-XYZ", droneDTOs.get(0).publicSerialNumber());
        assertEquals("UDE_AF_DRIFT", droneDTOs.get(0).status());
    }

    @Test
    void testAddDroneSuccess() {
        DroneDTO createdDrone = droneService.addDrone();

        assertNotNull(createdDrone.droneId());
        assertEquals("I_DRIFT", createdDrone.status());
        assertNotNull(createdDrone.station());
        assertEquals(200L, createdDrone.station().stationId());
    }

    @Test
    void testAddDroneNoStationsFails() {
        Mockito.when(mockedStationRepository.findAll()).thenReturn(Collections.emptyList());

        assertThrows(ResourceNotFoundException.class, () -> droneService.addDrone());
    }

    @Test
    void testChangeDroneStatusSuccess() {
        DroneDTO updated = droneService.changeDroneStatus(1L, DroneStatus.I_DRIFT);

        assertEquals("I_DRIFT", updated.status());
    }

    @Test
    void testChangeDroneStatusNotFound() {
        assertThrows(ResourceNotFoundException.class, () -> droneService.changeDroneStatus(999L, DroneStatus.I_DRIFT));
    }
}
