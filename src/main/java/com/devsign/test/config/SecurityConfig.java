package com.devsign.test.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        //.requestMatchers("/", "/css/indexStyle.css").permitAll() // 첫 화면과 정적 리소스는 인증 없이 접근
                        .anyRequest().permitAll() // 모든 요청을 인증 없이 허용
                )
                .formLogin(form -> form
                        .loginPage("/devsign/login") // 사용자 정의 로그인 페이지
                        .loginProcessingUrl("/devsign/login") // 로그인 요청을 처리할 URL
                        .defaultSuccessUrl("/", true) // 로그인 성공 시 이동할 기본 경로
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/") // 로그아웃 후 첫 화면으로 리디렉션
                        .permitAll() // 로그아웃 URL 접근 허용
                );

        // csrf disable
        http
                .csrf((auth) -> auth.disable());

        // form 로그인 disable
        http
                .formLogin((auth) ->auth.disable());

        // http basic 인증 disable
        http
                .httpBasic((auth) -> auth.disable());

        return http.build();
    }
}

