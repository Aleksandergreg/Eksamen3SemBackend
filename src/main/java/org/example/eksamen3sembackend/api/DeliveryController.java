package org.example.eksamen3sembackend.api;

import org.example.eksamen3sembackend.dto.CreateDeliveryDTO;
import org.example.eksamen3sembackend.dto.DeliveryDTO;
import org.example.eksamen3sembackend.service.DeliveryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/deliveries")
@CrossOrigin
public class DeliveryController {

    private final DeliveryService deliveryService;

    public DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }


    @GetMapping
    public ResponseEntity<List<DeliveryDTO>> getAllUndelivered() {
        return ResponseEntity.ok(deliveryService.getAllUndelivered());
    }


    @PostMapping("/add")
    public ResponseEntity<DeliveryDTO> addDelivery(@RequestBody CreateDeliveryDTO dto) {
        return ResponseEntity.ok(deliveryService.addDelivery(dto));
    }


    @GetMapping("/queue")
    public ResponseEntity<List<DeliveryDTO>> getAllUnassignedDeliveries() {
        return ResponseEntity.ok(deliveryService.getAllUnassigned());
    }


    @PostMapping("/schedule")
    public ResponseEntity<DeliveryDTO> scheduleDelivery(
            @RequestParam Long deliveryId,
            @RequestParam Long droneId
    ) {
        return ResponseEntity.ok(deliveryService.scheduleDelivery(deliveryId, droneId));
    }


    @PostMapping("/finish")
    public ResponseEntity<DeliveryDTO> finishDelivery(@RequestParam Long deliveryId) {
        return ResponseEntity.ok(deliveryService.finishDelivery(deliveryId));
    }
}

