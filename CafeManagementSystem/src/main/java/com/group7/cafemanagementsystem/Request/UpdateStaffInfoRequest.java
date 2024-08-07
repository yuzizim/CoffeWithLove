package com.group7.cafemanagementsystem.Request;

import com.group7.cafemanagementsystem.model.OrderTable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateStaffInfoRequest {

    @NotEmpty(message = "Username is required")
    @Pattern(regexp = "^[a-zA-Z0-9_]*$", message = "Username can only contain letters, numbers, and underscores")
    private String userName;

    @Pattern(regexp = "^[a-zA-Z ]*$", message = "Username can only contain letters")
    @NotEmpty(message = "Full name is required")
    private String fullName;

    @Email(message = "Email should be valid")
    @NotEmpty(message = "Email is required")
    private String email;

    @NotEmpty(message = "Phone number is required")
    @Pattern(regexp = "^\\d{10}$", message = "Phone number should be 10 digits")
    private String phoneNumber;
}
