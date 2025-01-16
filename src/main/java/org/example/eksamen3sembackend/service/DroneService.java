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


    public DroneDTO addDrone() {
        List<Station> stations = stationRepository.findAll();
        if (stations.isEmpty()) {
            throw new ResourceNotFoundException("No stations available for the new drone");
        }

        Station stationWithFewestDrones = stations.stream()
                .min((s1, s2) -> s1.getDrones().size() - s2.getDrones().size())
                .orElseThrow(() -> new ResourceNotFoundException("No stations found"));

        Drone drone = new Drone(
                UUID.randomUUID().toString(),
                DroneStatus.I_DRIFT
        );
        drone.setStation(stationWithFewestDrones);

        Drone saved = droneRepository.save(drone);
        return mapDroneToDTO(saved);
    }


    public DroneDTO changeDroneStatus(Long droneId, DroneStatus newStatus) {
        Drone drone = droneRepository.findById(droneId)
                .orElseThrow(() -> new ResourceNotFoundException("Drone not found with id " + droneId));
        drone.setStatus(newStatus);
        return mapDroneToDTO(droneRepository.save(drone));
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

