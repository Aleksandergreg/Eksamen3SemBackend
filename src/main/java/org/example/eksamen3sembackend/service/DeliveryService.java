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
