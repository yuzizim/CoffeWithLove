package com.group7.cafemanagementsystem.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDto {
    private int id;
    private String userName;
    private String fullName;
    private String email;
    private String role;
}
