package org.example.eksamen3sembackend.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@Entity
@NoArgsConstructor
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long deliveryId;

    @ManyToOne
    @JoinColumn(name = "drone_id_fk", referencedColumnName = "droneId")
    @JsonManagedReference
    private Drone drone;

    @ManyToOne
    @JoinColumn(name = "pizza_id_fk", referencedColumnName = "pizzaId")
    private Pizza pizza;

    private String address;
    private LocalDateTime expectedDeliveryTime;
    private LocalDateTime actualDeliveryTime;



    public Delivery(String address, LocalDateTime expectedDeliveryTime) {
        this.address = address;
        this.expectedDeliveryTime = expectedDeliveryTime;
    }



}
