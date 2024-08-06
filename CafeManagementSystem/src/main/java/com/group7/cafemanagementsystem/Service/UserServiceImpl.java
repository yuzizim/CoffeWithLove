package com.group7.cafemanagementsystem.Service;

import com.group7.cafemanagementsystem.Repository.UserRepository;
import com.group7.cafemanagementsystem.Request.UpdateStaffInfoRequest;
import com.group7.cafemanagementsystem.Response.PageFoodResponse;
import com.group7.cafemanagementsystem.Response.PageUserResponse;
import com.group7.cafemanagementsystem.model.Account;
import com.group7.cafemanagementsystem.model.Food;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public PageUserResponse getUserByPage(Boolean status, String search, int page, int size) {
        if (search == null) search = "";
        List<Account> accounts = new ArrayList<>();
        Pageable paging = PageRequest.of(page, size);

        Page<Account> pageAccounts;
        if (status == null) {
            pageAccounts = userRepository.findByStatusOrderByStatus(paging, search);
        } else {
            pageAccounts = userRepository.findByStatus(status, paging);
        }

        accounts = pageAccounts.getContent();
        int totalPages = pageAccounts.getTotalPages();
        return new PageUserResponse(accounts, totalPages);
    }

    @Override
    public Account saveAccount(Account account) {
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        account.setRole("STAFF");
        account.setStatus(true);
        account.setAvatar("/static/img/account/user.png");
        return userRepository.save(account);
    }

    @Override
    public Account deleteAccount(int id) {
        Account account = userRepository.findById(id).get();
        account.setStatus(false);
        return userRepository.save(account);
    }

    @Override
    public Account changeStatusAccount(int id) {
        Account account = userRepository.findById(id).get();
        account.setStatus(!account.isStatus());
        return userRepository.save(account);
    }

    @Override
    public Account findById(int id) {
        return userRepository.findById(id).get();
    }

    @Override
    public boolean findByUserName(String username) {
        return userRepository.findByUserName(username).isPresent();
    }

    @Override
    public List<Account> findByRole(String role) {
        return userRepository.findAccountByRole(role);
    }

    @Override
    public Account updateStaffInfoByAdmin(int staffId, UpdateStaffInfoRequest request, String image) {
        Account staff = findById(staffId);
        staff.setUserName(request.getUserName());
        staff.setPhoneNumber(request.getPhoneNumber());
        staff.setFullName(request.getFullName());
        staff.setEmail(request.getEmail());

        if (!image.equals("")) {
            staff.setAvatar("/static/img/account/" + image);
        }
        return userRepository.save(staff);
    }
}
