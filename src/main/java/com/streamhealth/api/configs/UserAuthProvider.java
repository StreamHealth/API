package com.streamhealth.api.configs;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.streamhealth.api.dtos.UserDto;
import com.streamhealth.api.entities.User;
import com.streamhealth.api.exceptions.AppException;
import com.streamhealth.api.mappers.UserMapper;
import com.streamhealth.api.repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Collections;
import java.util.Date;

@RequiredArgsConstructor
@Component
public class UserAuthProvider {

    private final UserRepository userRepository;
    private final UserMapper userM;

    @Value("${security.jwt.token.secret-key}")
    private String secretKey;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createToken(UserDto dto) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + 86400000); // 24 hours in milliseconds

        return JWT.create()
                .withIssuer(dto.getLogin())
                .withIssuedAt(now)
                .withExpiresAt(validity)
                .withClaim("name", dto.getName())
                .withClaim("userId", dto.getId())
                .withClaim("login", dto.getLogin())
                .sign(Algorithm.HMAC256(secretKey));
    }


    public Authentication validateToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secretKey);

        JWT.require(algorithm).build();

        DecodedJWT decodedJWT = JWT.decode(token);

        UserDto user = UserDto.builder()
                .login(decodedJWT.getIssuer())
                .name(decodedJWT.getClaim("name").asString())
                .id(decodedJWT.getClaim("userId").asLong())
                .build();
        return new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
    }

    public Authentication validateTokenStrongly(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secretKey);

        JWT.require(algorithm).build();
        DecodedJWT decodedJWT = JWT.decode(token);

        User user = userRepository.findByLogin(decodedJWT.getIssuer())
                .orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));

        UserDto userDto = userM.toUserDto(user);

        return new UsernamePasswordAuthenticationToken(userDto, null, Collections.emptyList());
    }
}
