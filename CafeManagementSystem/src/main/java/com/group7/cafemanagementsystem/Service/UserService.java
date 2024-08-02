package com.group7.cafemanagementsystem.Service;

import com.group7.cafemanagementsystem.Response.PageUserResponse;
import com.group7.cafemanagementsystem.model.Account;

public interface UserService {
    PageUserResponse getUserByPage(Boolean status, String search, int page, int size);

    Account saveAccount(Account account);

    Account deleteAccount(int id);

    Account changeStatusAccount(int id);

    Account findById(int id);

    boolean findByUserName(String username);


}
