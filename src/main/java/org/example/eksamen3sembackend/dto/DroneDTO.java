package org.example.eksamen3sembackend.dto;

public record DroneDTO(
        Long droneId,
        String publicSerialNumber,
        String status,
        StationDTO station
) {}
