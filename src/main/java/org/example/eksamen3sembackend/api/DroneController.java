package org.example.eksamen3sembackend.api;


import org.example.eksamen3sembackend.dto.DroneDTO;
import org.example.eksamen3sembackend.model.DroneStatus;
import org.example.eksamen3sembackend.service.DroneService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/drones")
@CrossOrigin
public class DroneController {

    private final DroneService droneService;

    public DroneController(DroneService droneService) {
        this.droneService = droneService;
    }


    @GetMapping
    public ResponseEntity<List<DroneDTO>> getAllDrones() {
        return ResponseEntity.ok(droneService.getAllDrones());
    }


    @PostMapping("/add")
    public ResponseEntity<DroneDTO> addDrone() {
        DroneDTO created = droneService.addDrone();
        return ResponseEntity.ok(created);
    }


    @PostMapping("/enable")
    public ResponseEntity<DroneDTO> enableDrone(@RequestParam Long id) {
        return ResponseEntity.ok(droneService.changeDroneStatus(id, DroneStatus.I_DRIFT));
    }


    @PostMapping("/disable")
    public ResponseEntity<DroneDTO> disableDrone(@RequestParam Long id) {
        return ResponseEntity.ok(droneService.changeDroneStatus(id, DroneStatus.UDE_AF_DRIFT));
    }


    @PostMapping("/retire")
    public ResponseEntity<DroneDTO> retireDrone(@RequestParam Long id) {
        return ResponseEntity.ok(droneService.changeDroneStatus(id, DroneStatus.UDFASET));
    }
}

