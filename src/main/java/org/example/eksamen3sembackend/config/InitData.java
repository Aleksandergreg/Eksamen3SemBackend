package org.example.eksamen3sembackend.config;

import org.example.eksamen3sembackend.model.*;
import org.example.eksamen3sembackend.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.List;

@Configuration
public class InitData {

    @Bean
    public CommandLineRunner initDatabase(
            StationRepository stationRepo,
            PizzaRepository pizzaRepo,
            DroneRepository droneRepo,
            DeliveryRepository deliveryRepo
    ) {
        return args -> {
            Station s1 = new Station(55.41, 12.34);
            Station s2 = new Station(55.43, 12.35);
            Station s3 = new Station(55.39, 12.33);
            stationRepo.saveAll(List.of(s1, s2, s3));

            Pizza p1 = new Pizza("Margherita", 60);
            Pizza p2 = new Pizza("Pepperoni", 70);
            Pizza p3 = new Pizza("Hawaiian", 80);
            Pizza p4 = new Pizza("Veggie", 75);
            Pizza p5 = new Pizza("Meat Lovers", 90);
            pizzaRepo.saveAll(List.of(p1, p2, p3, p4, p5));

            Drone d1 = new Drone("DRONE-ABC-123", DroneStatus.I_DRIFT);
            d1.setStation(s1);
            droneRepo.save(d1);

            Delivery delivery = new Delivery("Random Street 42",
                    LocalDateTime.now().plusHours(1));
            delivery.setPizza(p1);
            deliveryRepo.save(delivery);
        };
    }
}
