package com.group7.cafemanagementsystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
    @NotEmpty(message = "Username is required")
    @Pattern(regexp = "^[a-zA-Z0-9_]*$", message = "Username can only contain letters, numbers, and underscores")
    private String userName;

    @Column(name = "DisplayName")
    @Pattern(regexp = "^[a-zA-Z ]*$", message = "Username can only contain letters")
    @NotEmpty(message = "Full name is required")
    private String fullName;

    @Column(name = "Email")
    @Email(message = "Email should be valid")
    @NotEmpty(message = "Email is required")
    private String email;

    @Column(name = "Password")
    @NotEmpty(message = "Password is required")
    @Size(min = 6, message = "Password should have at least 6 characters")
    private String password;

    @Column(name = "phone")
    @NotEmpty(message = "Phone number is required")
    @Pattern(regexp = "^\\d{10}$", message = "Phone number should be 10 digits")
    private String phoneNumber;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "status")
    private boolean status;

    @Column(name = "role")
    private String role;

    @OneToMany(mappedBy = "staff", cascade = CascadeType.ALL)
    private List<OrderTable> orderTables;
}
