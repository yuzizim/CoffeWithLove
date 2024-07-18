package com.group7.cafemanagementsystem.Dto.Mappper;

import com.group7.cafemanagementsystem.Dto.UserDto;
import com.group7.cafemanagementsystem.model.Account;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class UserMapper implements Function<Account, UserDto> {
    @Override
    public UserDto apply(Account account) {
        return new UserDto(
                account.getID(),
                account.getUserName(),
                account.getFullName(),
                account.getEmail(),
                account.getRole()
        );
    }
}
