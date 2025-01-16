package org.example.eksamen3sembackend.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Station {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stationId;

    private double latitude;
    private double longitude;

    @OneToMany(mappedBy = "station")
    private Set<Drone> drones = new HashSet<>();

    public Station() {
    }

    public Station(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }


    public Long getStationId() {
        return stationId;
    }

    public void setStationId(Long stationId) {
        this.stationId = stationId;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Set<Drone> getDrones() {
        return drones;
    }

    public void setDrones(Set<Drone> drones) {
        this.drones = drones;
    }
}
