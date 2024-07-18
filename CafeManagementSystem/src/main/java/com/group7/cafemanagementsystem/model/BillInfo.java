package com.group7.cafemanagementsystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "BillInfo")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BillInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "idBill", nullable = false)
    private Bill bill;

    @OneToOne
    @JoinColumn(name = "idFood")
    private Food food;

    @Column(name = "count")
    private int count;
}
