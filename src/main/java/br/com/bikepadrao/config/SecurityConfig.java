package br.com.bikepadrao.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {

        return PasswordEncoderFactories
                .createDelegatingPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        http
                .authorizeHttpRequests(authorize ->
                        authorize

                                .requestMatchers(
                                        "/",
                                        "/produtos",
                                        "/produto/**",
                                        "/css/**",
                                        "/js/**",
                                        "/images/**",
                                        "/uploads/**",
                                        "/admin/login",
                                        "/error"
                                )
                                .permitAll()

                                .requestMatchers("/admin/**")
                                .hasRole("ADMIN")

                                .anyRequest()
                                .permitAll()
                )


                .formLogin(form ->
                        form
                                .loginPage("/admin/login")

                                .loginProcessingUrl(
                                        "/admin/login"
                                )

                                .defaultSuccessUrl(
                                        "/admin",
                                        true
                                )

                                .failureUrl(
                                        "/admin/login?erro"
                                )

                                .permitAll()
                )


                .logout(logout ->
                        logout
                                .logoutUrl("/admin/logout")

                                .logoutSuccessUrl(
                                        "/admin/login?logout"
                                )

                                .permitAll()
                );


        return http.build();
    }
}