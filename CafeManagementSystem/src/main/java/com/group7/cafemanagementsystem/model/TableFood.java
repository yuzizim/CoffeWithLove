package com.group7.cafemanagementsystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "TableFood")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TableFood {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "Name cannot be blank")
    @Pattern(regexp = "^[a-zA-Z0-9 ]*$", message = "Name cannot contain special characters")
    @Column(name = "name")
    private String name;

    @Column(name = "status")
    private boolean status;

    @OneToMany(mappedBy = "tableFood", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderTable> orderTables = new HashSet<>();
}
