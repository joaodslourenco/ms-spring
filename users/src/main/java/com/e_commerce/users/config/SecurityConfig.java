package com.e_commerce.users.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http

                .authorizeHttpRequests(authz -> authz
//                        .requestMatchers("/common/csrf-token").permitAll()
                                .anyRequest().authenticated()
                ).csrf(AbstractHttpConfigurer::disable)
                .httpBasic(withDefaults());

        return http.build();
    }

//    @Bean
//    public InMemoryUserDetailsManager inMemoryUserDetailsManager() {
//        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
//
//        UserDetails adminUser = User.withUsername("admin")
//                .password(encoder.encode("adminPassword"))
//                .roles("ADMIN")
//                .build();
//
//        UserDetails regularUser = User.withUsername("user")
//                .password(encoder.encode("userPassword"))
//                .roles("USER")
//                .build();
//
//        return new InMemoryUserDetailsManager(adminUser, regularUser);
//    }


}
