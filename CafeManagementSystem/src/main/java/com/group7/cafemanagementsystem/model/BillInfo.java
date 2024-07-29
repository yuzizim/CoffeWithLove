package com.group7.cafemanagementsystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;

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
    @Cascade(org.hibernate.annotations.CascadeType.REMOVE)
    private Food food;

    @Column(name = "count")
    private int count;
}
