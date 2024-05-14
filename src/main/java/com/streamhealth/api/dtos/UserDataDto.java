package com.streamhealth.api.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDataDto {
    private Long id;
    private String name;
    private String login;
    private BigDecimal totalSalesToday;
}
