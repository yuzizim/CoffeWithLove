package com.group7.cafemanagementsystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.Date;

@Entity
@Table(name = "OrderTable")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "OrderDate")
    private Date orderDate;

    @Column(name = "OrderTime")
    private LocalTime orderTime;

    @Column(name = "CustomerName")
    private String customerName;

    @Column(name = "Email")
    private String email;

    @Column(name = "PhoneNumber")
    private String phoneNumber;

    @Column(name = "NumberOfPeole")
    private int numPeople;

    @Column(name = "Note")
    private String note;

    @ManyToOne
    @JoinColumn(name = "id_table", nullable = false)
    private TableFood tableFood;
}
