package com.streamhealth.api.controllers;

import com.streamhealth.api.dtos.UserDataDto;
import com.streamhealth.api.entities.User;
import com.streamhealth.api.exceptions.AppException;
import com.streamhealth.api.repositories.UserRepository;
import com.streamhealth.api.services.UserService;
import com.streamhealth.api.utilities.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/get_user_and_sales")
    public ResponseEntity<UserDataDto> getUser(HttpServletRequest request) {
        String login = JWTUtil.extractLoginFromToken(request);
        User cashier = userRepository.findByLogin(login)
                .orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));
        UserDataDto userData = userService.getUserData();
        BigDecimal totalSales = userService.getTotalSalesToday(cashier);
        userData.setTotalSalesToday(totalSales);
        return ResponseEntity.ok(userData);
    }

}
