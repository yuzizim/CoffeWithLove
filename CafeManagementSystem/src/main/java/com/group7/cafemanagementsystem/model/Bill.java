package com.group7.cafemanagementsystem.model;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "Bill")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "DateCheckIn")
    private LocalDateTime dateCheckIn;

    @Column(name = "DateCheckOut")
    private LocalDateTime dateCheckOut;

    @OneToOne
    @JoinColumn(name = "order_id")
    private OrderTable orderTable;
}
