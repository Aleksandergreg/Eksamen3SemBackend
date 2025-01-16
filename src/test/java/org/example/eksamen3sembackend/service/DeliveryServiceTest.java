package org.example.eksamen3sembackend.service;

import org.example.eksamen3sembackend.dto.CreateDeliveryDTO;
import org.example.eksamen3sembackend.dto.DeliveryDTO;
import org.example.eksamen3sembackend.exception.BadRequestException;
import org.example.eksamen3sembackend.exception.ResourceNotFoundException;
import org.example.eksamen3sembackend.model.*;
import org.example.eksamen3sembackend.repository.DeliveryRepository;
import org.example.eksamen3sembackend.repository.DroneRepository;
import org.example.eksamen3sembackend.repository.PizzaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;

@SpringBootTest
@ActiveProfiles("Test")
class DeliveryServiceTest {

    @Mock
    private DeliveryRepository mockedDeliveryRepository;

    @Mock
    private PizzaRepository mockedPizzaRepository;

    @Mock
    private DroneRepository mockedDroneRepository;

    private DeliveryService deliveryService;

    @BeforeEach
    void init() {
        deliveryService = new DeliveryService(mockedDeliveryRepository, mockedPizzaRepository, mockedDroneRepository);

        Pizza pizza1 = new Pizza("Pizza1", 100);
        pizza1.setPizzaId(1L);

        Drone drone1 = new Drone("Drone1", DroneStatus.I_DRIFT);
        drone1.setDroneId(11L);

        Delivery assignedDelivery = new Delivery("Assigned Street", LocalDateTime.now().plusHours(1));
        assignedDelivery.setDeliveryId(101L);
        assignedDelivery.setDrone(drone1);
        assignedDelivery.setPizza(pizza1);

        Delivery unassignedDelivery = new Delivery("Unassigned Street", LocalDateTime.now().plusMinutes(30));
        unassignedDelivery.setDeliveryId(102L);
        unassignedDelivery.setPizza(pizza1);


        Mockito.when(mockedPizzaRepository.findById(1L)).thenReturn(Optional.of(pizza1));
        Mockito.when(mockedPizzaRepository.findById(999L)).thenReturn(Optional.empty());

        Mockito.when(mockedDroneRepository.findById(11L)).thenReturn(Optional.of(drone1));
        Mockito.when(mockedDroneRepository.findById(999L)).thenReturn(Optional.empty());

        List<Delivery> allDeliveries = new ArrayList<>(List.of(assignedDelivery, unassignedDelivery));
        Mockito.when(mockedDeliveryRepository.findAll()).thenReturn(allDeliveries);

        Mockito.when(mockedDeliveryRepository.findById(101L)).thenReturn(Optional.of(assignedDelivery));
        Mockito.when(mockedDeliveryRepository.findById(102L)).thenReturn(Optional.of(unassignedDelivery));
        Mockito.when(mockedDeliveryRepository.findById(999L)).thenReturn(Optional.empty());

        Mockito.when(mockedDeliveryRepository.save(ArgumentMatchers.any(Delivery.class))).thenAnswer(new Answer<Delivery>() {
            @Override
            public Delivery answer(InvocationOnMock invocation) {
                Delivery toSave = invocation.getArgument(0);
                if (toSave.getDeliveryId() == null) {
                    toSave.setDeliveryId(200L);
                }
                return toSave;
            }
        });
    }

    @Test
    void testGetAllUndelivered() {
        List<DeliveryDTO> result = deliveryService.getAllUndelivered();
        assertEquals(2, result.size());
    }

    @Test
    void testAddDelivery_Success() {
        CreateDeliveryDTO dto = new CreateDeliveryDTO(1L, "New Address");
        DeliveryDTO created = deliveryService.addDelivery(dto);

        assertNotNull(created.deliveryId());
        assertEquals("New Address", created.address());
        assertEquals(1L, created.pizzaId());
    }

    @Test
    void testAddDelivery_PizzaNotFound() {
        CreateDeliveryDTO dto = new CreateDeliveryDTO(999L, "BadPizzaID");
        assertThrows(ResourceNotFoundException.class, () -> deliveryService.addDelivery(dto));
    }

    @Test
    void testGetAllUnassigned() {
        List<DeliveryDTO> queue = deliveryService.getAllUnassigned();
        assertEquals(1, queue.size());
        assertEquals("Unassigned Street", queue.get(0).address());
    }

    @Test
    void testScheduleDelivery_Success() {
        DeliveryDTO scheduled = deliveryService.scheduleDelivery(102L, 11L);
        assertEquals(11L, scheduled.droneId());
    }

    @Test
    void testScheduleDelivery_DeliveryNotFound() {
        assertThrows(ResourceNotFoundException.class,
                () -> deliveryService.scheduleDelivery(999L, 11L));
    }

    @Test
    void testScheduleDelivery_DroneNotFound() {
        assertThrows(ResourceNotFoundException.class,
                () -> deliveryService.scheduleDelivery(102L, 999L));
    }

    @Test
    void testScheduleDelivery_AlreadyAssigned() {
        assertThrows(BadRequestException.class,
                () -> deliveryService.scheduleDelivery(101L, 11L));
    }

    @Test
    void testScheduleDelivery_DroneNotIDrift() {
        Drone drone1 = mockedDroneRepository.findById(11L).orElseThrow();
        drone1.setStatus(DroneStatus.UDE_AF_DRIFT);

        assertThrows(BadRequestException.class,
                () -> deliveryService.scheduleDelivery(102L, 11L));
    }

    @Test
    void testFinishDelivery_Success() {
        DeliveryDTO finished = deliveryService.finishDelivery(101L);
        assertNotNull(finished.actualDeliveryTime());
    }

    @Test
    void testFinishDelivery_NoDroneFails() {
        assertThrows(BadRequestException.class,
                () -> deliveryService.finishDelivery(102L));
    }

    @Test
    void testFinishDelivery_NotFound() {
        assertThrows(ResourceNotFoundException.class,
                () -> deliveryService.finishDelivery(999L));
    }
}
