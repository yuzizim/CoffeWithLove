package com.group7.cafemanagementsystem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "Food")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "Name cannot be blank")
    @Pattern(regexp = "^[a-zA-Z0-9 ]*$", message = "Name cannot contain special characters")
    @Column(name = "name")
    private String name;

    @Column(name = "status")
    private boolean status;

    @Column(name = "images")
    private String images;

    @Column(name = "description")
    private String description;

    @JsonIgnore
    @NotNull(message = "Category can not be null")
    @ManyToOne
    @JoinColumn(name = "idCategrory", nullable = false)
    private FoodCategory foodCategory;

    @NotNull(message = "Price cannot be null")
    @Min(value = 0, message = "Price cannot be smaller than 0")
    @Column(name = "price")
    private double price;

    @Override
    public String toString() {
        return "Food{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", images='" + images + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                '}';
    }

}
