package org.example.eksamen3sembackend.dto;


import java.time.LocalDateTime;

public record DeliveryDTO(
        Long deliveryId,
        Long droneId,
        Long pizzaId,
        String address,
        LocalDateTime expectedDeliveryTime,
        LocalDateTime actualDeliveryTime
) {}
