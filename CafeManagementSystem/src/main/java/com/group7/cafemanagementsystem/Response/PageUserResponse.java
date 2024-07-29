package com.group7.cafemanagementsystem.Response;

import com.group7.cafemanagementsystem.model.Account;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageUserResponse {
    private List<Account> accounts;
    private int totalPages;
}
