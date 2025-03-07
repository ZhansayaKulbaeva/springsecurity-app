package kz.bitlab.springsecurity_app.config;

import kz.bitlab.springsecurity_app.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public UserService userService() {
        return new UserService();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userService()).passwordEncoder(passwordEncoder());

        http.authorizeRequests(
                authorize -> authorize
                        .requestMatchers("/myProfile").authenticated()
                        .anyRequest().permitAll()
        ).formLogin(
                login -> login
                        .loginProcessingUrl("/enterbek")
                        .defaultSuccessUrl("/myProfile")
                        .loginPage("/loginPage")
                        .failureUrl("/loginPage?someErrorText")
                        .usernameParameter("email")
                        .passwordParameter("password")
        ).logout(
                logout -> logout
                        .logoutSuccessUrl("/loginPage?logout")
                        .logoutUrl("/logoutbek")
        ).csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }
}

