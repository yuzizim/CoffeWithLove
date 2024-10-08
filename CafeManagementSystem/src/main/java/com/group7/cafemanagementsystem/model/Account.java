package com.group7.cafemanagementsystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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
    @NotBlank(message = "Username is required")
    @Pattern(regexp = "^[a-zA-Z0-9_]*$", message = "Username can only contain letters, numbers, and underscores")
    private String userName;

    @Column(name = "DisplayName")
    @Pattern(regexp = "^[a-zA-Z ]*$", message = "Username can only contain letters")
    @NotBlank(message = "Full name is required")
    private String fullName;

    @Column(name = "Email")
    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    private String email;

    @Column(name = "Password")
    @NotBlank(message = "Password is required")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$", message = "long and include at least one uppercase letter, one lowercase letter, and one number.")
    @Size(min = 8, message = "Password should have at least 8 characters")
    private String password;

    @Column(name = "phone")
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^0\\d{9}$", message = "Phone number should be 10 digits and start with 0")
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
