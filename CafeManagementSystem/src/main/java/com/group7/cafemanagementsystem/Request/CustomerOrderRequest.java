package com.group7.cafemanagementsystem.Request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerOrderRequest {
    @NotBlank(message = "Username is not empty")
    @Pattern(regexp = "^[a-zA-Z0-9 ]*$", message = "Customer name can only contain letters")
    @Column(name = "CustomerName")
    private String customerName;

    @NotBlank(message = "Phone is not empty")
    @Pattern(regexp = "\\d{10}", message = "Phone number must be exactly 10 digits")
    @Column(name = "PhoneNumber")
    private String phoneNumber;

    @Column(name = "Note")
    private String note;
}
