package com.group7.cafemanagementsystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Food")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "status")
    private boolean status;

    @ManyToOne
    @JoinColumn(name = "idCategrory", nullable = false)
    private FoodCategory foodCategory;

    @Column(name = "price")
    private Long price;

    @Column(name = "images")
    private String images;

    @Column(name = "description")
    private String description;
}
