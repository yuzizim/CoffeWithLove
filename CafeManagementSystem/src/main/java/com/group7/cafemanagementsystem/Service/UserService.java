package com.group7.cafemanagementsystem.Service;

import com.group7.cafemanagementsystem.model.Account;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserService {

    List<Account> getAllAccounts();
    List<Account> getAccountsByPagination(int pageNo, int pageSize);
    Integer getAccountsCount();
    Account getAccountById(int id);
    Account addAccount(Account account);
    Account saveAccount(Account account);
    Account updateAccount(Account account);
    void deleteAccount(int id);
    String uploadImage(MultipartFile image);
    String updateImage(String oldImage,MultipartFile image) throws IOException;
    String encryptPassword(String password);
}
