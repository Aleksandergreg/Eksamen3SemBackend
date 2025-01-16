package org.example.eksamen3sembackend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
public class Drone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long droneId;

    @Column(unique = true, nullable = false)
    private String publicSerialNumber;

    @Enumerated(EnumType.STRING)
    private DroneStatus status;

    @ManyToOne
    @JoinColumn(name = "station_id_fk", referencedColumnName = "stationId")
    private Station station;

    @OneToMany(mappedBy = "drone")
    @JsonBackReference
    private Set<Delivery> deliveries = new HashSet<>();



    public Drone(String publicSerialNumber, DroneStatus status) {
        this.publicSerialNumber = publicSerialNumber;
        this.status = status;
    }


}
