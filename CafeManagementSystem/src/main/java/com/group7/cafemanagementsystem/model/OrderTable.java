package com.group7.cafemanagementsystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "OrderTable")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "OrderTime")
    private LocalDateTime orderTime;

    @Column(name = "CustomerName")
    private String customerName;

    @Column(name = "PhoneNumber")
    private String phoneNumber;

    @Min(value = 1, message = "Number of people cannot be smaller than 1")
    @Column(name = "NumberOfPeole")
    private int numPeople;

    @Column(name = "Note")
    private String note;

    @Column(name = "TotalPrice")
    private double totalPrice;

    @Column(name = "Status")
    private boolean status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TableFood", nullable = false)
    private TableFood tableFood;

    @OneToMany(mappedBy = "orderTable", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetail> orderDetails;

    @ManyToOne
    @JoinColumn(name = "staff_id")
    private Account staff;
}
