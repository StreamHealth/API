package com.streamhealth.api.services;

import com.streamhealth.api.dtos.CredentialsDto;
import com.streamhealth.api.dtos.SignUpDto;
import com.streamhealth.api.dtos.UserDataDto;
import com.streamhealth.api.dtos.UserDto;
import com.streamhealth.api.entities.Transaction;
import com.streamhealth.api.entities.User;
import com.streamhealth.api.exceptions.AppException;
import com.streamhealth.api.mappers.UserMapper;
import com.streamhealth.api.repositories.TransactionRepository;
import com.streamhealth.api.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.nio.CharBuffer;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final TransactionRepository transactionRepository;

    public UserDto login(CredentialsDto credentialsDto) {
        User user = userRepository.findByLogin(credentialsDto.login())
                .orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));

        if (passwordEncoder.matches(CharBuffer.wrap(credentialsDto.password()), user.getPassword())) {
            return userMapper.toUserDto(user);
        }

        throw new AppException("Invalid password", HttpStatus.BAD_REQUEST);
    }

    public UserDto register(SignUpDto signUpDto) {
        Optional<User> oUser = userRepository.findByLogin(signUpDto.login());

        if (oUser.isPresent()) {
            throw new AppException("User already exists", HttpStatus.BAD_REQUEST);
        }

        User user = userMapper.signUpToUser(signUpDto);
        user.setPassword(passwordEncoder.encode(CharBuffer.wrap(signUpDto.password())));
        User savedUser = userRepository.save(user);
        return userMapper.toUserDto(savedUser);
    }

    public UserDto getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDto currentUser = (UserDto) authentication.getPrincipal();
        String currentLogin = currentUser.getLogin();
        User user = userRepository.findByLogin(currentLogin)
                .orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));
        return userMapper.toUserDto(user);
    }

    public UserDataDto getUserData(){
        UserDto user = getCurrentUser();
        UserDataDto userData = new UserDataDto();
        userData.setName(user.getName());
        userData.setLogin(user.getLogin());
        userData.setId(user.getId());
        return userData;
    }

    public BigDecimal getTotalSalesToday(User cashier) {
        LocalDate today = LocalDate.now();
        List<Transaction> transactions = transactionRepository.findAllByCashierAndTransactionDateBetween(
                cashier, today.atStartOfDay(), today.plusDays(1).atStartOfDay());
        return transactions.stream()
                .map(Transaction::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
