package com.group7.cafemanagementsystem.model;

import jakarta.persistence.*;
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

    @Column(name = "name")
    private String name;

    @Column(name = "status")
    private boolean status;

    @OneToMany(mappedBy = "tableFood", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderTable> orderTables = new HashSet<>();
}
