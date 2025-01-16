package org.example.eksamen3sembackend.service;

import org.example.eksamen3sembackend.dto.DroneDTO;
import org.example.eksamen3sembackend.model.Drone;
import org.example.eksamen3sembackend.model.DroneStatus;
import org.example.eksamen3sembackend.model.Station;
import org.example.eksamen3sembackend.repository.DroneRepository;
import org.example.eksamen3sembackend.repository.StationRepository;
import org.example.eksamen3sembackend.exception.ResourceNotFoundException;
import org.example.eksamen3sembackend.dto.StationDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

@Service
public class DroneService {

    private final DroneRepository droneRepository;
    private final StationRepository stationRepository;

    public DroneService(DroneRepository droneRepository, StationRepository stationRepository) {
        this.droneRepository = droneRepository;
        this.stationRepository = stationRepository;
    }


    public List<DroneDTO> getAllDrones() {
        return droneRepository.findAll()
                .stream()
                .map(this::mapDroneToDTO)
                .toList();
    }



    private DroneDTO mapDroneToDTO(Drone drone) {
        StationDTO stationDTO = null;
        if (drone.getStation() != null) {
            stationDTO = new StationDTO(
                    drone.getStation().getStationId(),
                    drone.getStation().getLatitude(),
                    drone.getStation().getLongitude()
            );
        }
        return new DroneDTO(
                drone.getDroneId(),
                drone.getPublicSerialNumber(),
                drone.getStatus().name(),
                stationDTO
        );
    }
}

