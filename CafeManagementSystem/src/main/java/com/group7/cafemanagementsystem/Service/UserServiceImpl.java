package com.group7.cafemanagementsystem.Service;

import com.group7.cafemanagementsystem.Repository.RefreshTokenRepository;
import com.group7.cafemanagementsystem.Repository.UserRepository;
import com.group7.cafemanagementsystem.Response.PageFoodResponse;
import com.group7.cafemanagementsystem.Response.PageUserResponse;
import com.group7.cafemanagementsystem.model.Account;
import com.group7.cafemanagementsystem.model.Food;
import com.group7.cafemanagementsystem.model.RefreshToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    JavaMailSender mailSender;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, RefreshTokenRepository refreshTokenRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.refreshTokenRepository = refreshTokenRepository;
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
    public String sendMailReset(Account user){
        try{
            String resetLink = generateResetToken(user);

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(user.getEmail());
            message.setSubject("Reset password");
            message.setText("Hello " + user.getFullName()+ "\n\n" +"Please click on this to reset your password: " + resetLink+". \n\n" + "Regards \n" +"CoffeWithLove.");
            mailSender.send(message);
            return "Success";
        }catch (Exception e){
            e.printStackTrace();
            return "Error";
        }
    }
    public String generateResetToken(Account user){
        RefreshToken existingToken = refreshTokenRepository.findByAccount(user);
        if (existingToken != null) {
            refreshTokenRepository.delete(existingToken);
        }
        UUID uuid = UUID.randomUUID();
        Instant expiryDate = Instant.now().plus(5, ChronoUnit.MINUTES);
        RefreshToken resetToken = new RefreshToken();
        resetToken.setAccount(user);
        resetToken.setToken(uuid.toString());
        resetToken.setExpiryDate(expiryDate);
        RefreshToken token = refreshTokenRepository.save(resetToken);
        if(token != null){
            String endpointUrl = "http://localhost:8080/auth/reset-password";
            return endpointUrl + "/" + resetToken.getToken();
        }
        return "";
    }

    public boolean hasExpiredToken(Instant expiryDate) {
        return Instant.now().isAfter(expiryDate);
    }
    public String sendMail(Account user, String text, String subject){
        try{
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(user.getEmail());
            message.setSubject(subject);
            message.setText("Hello "+ user.getFullName()+ "\n\n" +text+ "\n\n" + "Regards \n" +"CoffeWithLove.");
            mailSender.send(message);
            return "Success";
        }catch (Exception e){
            e.printStackTrace();
            return "Error";
        }
    }

    public boolean passwordMatches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    @Override
    public List<Account> findByRole(String role) {
        return userRepository.findAccountByRole(role);
    }

    @Override
    public Account findByUserNameForChangePass(String username) {
        return userRepository.findByUserName(username).get();
    }
}
