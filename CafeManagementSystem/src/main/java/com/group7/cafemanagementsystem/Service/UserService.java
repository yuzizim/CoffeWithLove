package com.group7.cafemanagementsystem.Service;

import com.group7.cafemanagementsystem.Request.UpdateStaffInfoRequest;
import com.group7.cafemanagementsystem.Response.PageUserResponse;
import com.group7.cafemanagementsystem.model.Account;

import java.util.List;

public interface UserService {
    PageUserResponse getUserByPage(Boolean status, String search, int page, int size);

    Account saveAccount(Account account);

    Account deleteAccount(int id);

    Account changeStatusAccount(int id);

    Account findById(int id);

    boolean findByUserName(String username);

    List<Account> findByRole(String role);

    Account findByUserNameForChangePass(String username);

    Account updateStaffInfoByAdmin(int staffId, UpdateStaffInfoRequest request, String image);

    Account resetPassword(int id);

    boolean checkExistEmail(String email);
}
