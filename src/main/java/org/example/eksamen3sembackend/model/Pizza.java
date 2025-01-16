package org.example.eksamen3sembackend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Pizza {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pizzaId;

    private String title;
    private int price;



    public Pizza(String title, int price) {
        this.title = title;
        this.price = price;
    }




}
