package org.example.eksamen3sembackend.service;


import org.example.eksamen3sembackend.dto.CreateDeliveryDTO;
import org.example.eksamen3sembackend.dto.DeliveryDTO;
import org.example.eksamen3sembackend.exception.BadRequestException;
import org.example.eksamen3sembackend.exception.ResourceNotFoundException;
import org.example.eksamen3sembackend.model.Delivery;
import org.example.eksamen3sembackend.model.Drone;
import org.example.eksamen3sembackend.model.DroneStatus;
import org.example.eksamen3sembackend.model.Pizza;
import org.example.eksamen3sembackend.repository.DeliveryRepository;
import org.example.eksamen3sembackend.repository.DroneRepository;
import org.example.eksamen3sembackend.repository.PizzaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final PizzaRepository pizzaRepository;
    private final DroneRepository droneRepository;

    public DeliveryService(DeliveryRepository deliveryRepository,
                           PizzaRepository pizzaRepository,
                           DroneRepository droneRepository) {
        this.deliveryRepository = deliveryRepository;
        this.pizzaRepository = pizzaRepository;
        this.droneRepository = droneRepository;
    }


    public List<DeliveryDTO> getAllUndelivered() {
        return deliveryRepository.findAll()
                .stream()
                .filter(d -> d.getActualDeliveryTime() == null)
                .map(this::mapDeliveryToDTO)
                .toList();
    }


    public DeliveryDTO addDelivery(CreateDeliveryDTO createDeliveryDTO) {
        Pizza pizza = pizzaRepository.findById(createDeliveryDTO.pizzaId())
                .orElseThrow(() -> new ResourceNotFoundException("Pizza not found with id " + createDeliveryDTO.pizzaId()));

        Delivery delivery = new Delivery(
                createDeliveryDTO.address(),
                LocalDateTime.now().plusMinutes(30)
        );
        delivery.setPizza(pizza);

        return mapDeliveryToDTO(deliveryRepository.save(delivery));
    }


    public List<DeliveryDTO> getAllUnassigned() {
        return deliveryRepository.findAll()
                .stream()
                .filter(d -> d.getDrone() == null)
                .map(this::mapDeliveryToDTO)
                .toList();
    }


    public DeliveryDTO scheduleDelivery(Long deliveryId, Long droneId) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery not found with id " + deliveryId));

        if (delivery.getDrone() != null) {
            throw new BadRequestException("Delivery already has a drone assigned.");
        }

        Drone drone = droneRepository.findById(droneId)
                .orElseThrow(() -> new ResourceNotFoundException("Drone not found with id " + droneId));

        if (drone.getStatus() != DroneStatus.I_DRIFT) {
            throw new BadRequestException("Drone is not 'i drift' and cannot be assigned.");
        }

        delivery.setDrone(drone);
        return mapDeliveryToDTO(deliveryRepository.save(delivery));
    }


    public DeliveryDTO finishDelivery(Long deliveryId) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery not found with id " + deliveryId));

        if (delivery.getDrone() == null) {
            throw new BadRequestException("Cannot finish a delivery that has no drone assigned.");
        }

        delivery.setActualDeliveryTime(LocalDateTime.now());
        return mapDeliveryToDTO(deliveryRepository.save(delivery));
    }

    private DeliveryDTO mapDeliveryToDTO(Delivery delivery) {
        Long droneId = (delivery.getDrone() != null) ? delivery.getDrone().getDroneId() : null;
        Long pizzaId = (delivery.getPizza() != null) ? delivery.getPizza().getPizzaId() : null;

        return new DeliveryDTO(
                delivery.getDeliveryId(),
                droneId,
                pizzaId,
                delivery.getAddress(),
                delivery.getExpectedDeliveryTime(),
                delivery.getActualDeliveryTime()
        );
    }
}
