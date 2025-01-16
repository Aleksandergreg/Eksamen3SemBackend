package org.example.eksamen3sembackend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

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

    public Drone() {
    }

    public Drone(String publicSerialNumber, DroneStatus status) {
        this.publicSerialNumber = publicSerialNumber;
        this.status = status;
    }


    public Long getDroneId() {
        return droneId;
    }

    public void setDroneId(Long droneId) {
        this.droneId = droneId;
    }

    public String getPublicSerialNumber() {
        return publicSerialNumber;
    }

    public void setPublicSerialNumber(String publicSerialNumber) {
        this.publicSerialNumber = publicSerialNumber;
    }

    public DroneStatus getStatus() {
        return status;
    }

    public void setStatus(DroneStatus status) {
        this.status = status;
    }

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }

    public Set<Delivery> getDeliveries() {
        return deliveries;
    }

    public void setDeliveries(Set<Delivery> deliveries) {
        this.deliveries = deliveries;
    }
}
