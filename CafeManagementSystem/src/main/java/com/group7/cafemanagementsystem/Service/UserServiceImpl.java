package com.group7.cafemanagementsystem.Service;

import com.group7.cafemanagementsystem.Repository.AccountRespository;
import com.group7.cafemanagementsystem.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private AccountRespository accountRespository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public List<Account> getAllAccounts() {
        return accountRespository.findAll();
    }

    @Override
    public List<Account> getAccountsByPagination(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return accountRespository.findAll(pageable).getContent();
    }

    @Override
    public Integer getAccountsCount() {
        return Math.toIntExact(accountRespository.count());
    }

    @Override
    public Account getAccountById(int id) {
        return accountRespository.findById(id).orElse(null);
    }

    @Override
    public Account addAccount(Account account) {
        account.setPassword(encryptPassword(account.getPassword()));
        return accountRespository.save(account);
    }

    @Override
    public Account saveAccount(Account account) {
        return accountRespository.save(account);
    }

    @Override
    public Account updateAccount(Account account) {
        if (accountRespository.existsById(account.getID())) {
            account.setPassword(encryptPassword(account.getPassword()));
            return accountRespository.save(account);
        }
        return null;
    }

    @Override
    public void deleteAccount(int id) {
        accountRespository.deleteById(id);
    }

    @Override
    public String uploadImage(MultipartFile image) {
        // Implement your image upload logic here
        return "imagePath"; // Return the path where the image is stored
    }

    @Override
    public String updateImage(String oldImage, MultipartFile image) throws IOException {
        // Implement your image update logic here
        return "newImagePath"; // Return the new image path
    }

    @Override
    public String encryptPassword(String password) {
        return passwordEncoder.encode(password);
    }
}
