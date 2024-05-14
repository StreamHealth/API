package com.streamhealth.api.configs;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.util.Set;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserAuthProvider userAuthProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        Set<String> getEndpoints = Set.of(
                "/api/v1/transaction/get_transactions",
                "/api/v1/transaction/get_transaction/{transactionId}",
                "/api/v1/product/get_products",
                "/api/v1/product/get_product/{productId}",
                "/api/v1/user/get_user_and_sales"
        );

        Set<String> postEndpoints = Set.of(
                "/api/v1/product/add_product",
                "/api/v1/auth/register",
                "/api/v1/auth/login"
        );

        Set<String> deleteEndpoints = Set.of(
                "/api/v1/transaction/delete_transaction/{transactionId}",
                "/api/v1/product/delete_product/{productId}"
        );

        Set<String> putEndpoints = Set.of(
                "/api/v1/transaction/update_transaction/{transactionId}",
                "/api/v1/product/update_product/{productId}"
        );

        http.csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(new JWTAuthFilter(userAuthProvider), BasicAuthenticationFilter.class)
                .sessionManagement(customizer -> customizer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests((requests) -> {
                    for (String endpoint : getEndpoints) {
                        requests.requestMatchers(HttpMethod.GET, endpoint).permitAll();
                    }

                    for (String endpoint : postEndpoints) {
                        requests.requestMatchers(HttpMethod.POST, endpoint).permitAll();
                    }

                    for (String endpoint : deleteEndpoints) {
                        requests.requestMatchers(HttpMethod.DELETE, endpoint).permitAll();
                    }

                    for (String endpoint : putEndpoints) {
                        requests.requestMatchers(HttpMethod.PUT, endpoint).permitAll();
                    }

                    requests.anyRequest().authenticated();
                });
        return http.build();
    }
}
