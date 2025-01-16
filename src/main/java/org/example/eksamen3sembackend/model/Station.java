package org.example.eksamen3sembackend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
@Getter
@Setter
@NoArgsConstructor
@Entity
public class Station {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stationId;

    private double latitude;
    private double longitude;

    @OneToMany(mappedBy = "station")
    private Set<Drone> drones = new HashSet<>();



    public Station(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

}
