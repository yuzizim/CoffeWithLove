package com.group7.cafemanagementsystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "Account")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ID;

    @Column(name = "UserName")
    private String userName;

    @Column(name = "DisplayName")
    private String fullName;

    @Column(name = "BirthDay")
    Date birthDay;

    @Column(name = "Avatar")
    private String avatar;

    @Column(name = "PhoneNumber")
    private int phoneNumber;

    @Column(name = "Email")
    private String email;

    @Column(name = "Password")
    private String password;

    @Column(name = "Address")
    private String address;

    @Column(name = "role")
    private String role;
}
