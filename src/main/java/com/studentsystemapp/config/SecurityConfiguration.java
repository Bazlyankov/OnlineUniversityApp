package com.studentsystemapp.config;

import com.studentsystemapp.model.enums.UserRolesEnum;
import com.studentsystemapp.repo.UserRepository;
import com.studentsystemapp.service.impl.UniversityUserDetailsService;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {




    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.authorizeHttpRequests(
                // Define which urls are visible by which users
                authorizeRequests -> authorizeRequests
                        .requestMatchers("/css/**",
                                "/images/**",
                                "/fonts/**",
                                "/scripts/**").permitAll()
                        // All static resources which are situated in js, images, css are available for anyone
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        // Allow anyone to see the home page, the registration page and the login form
                        .requestMatchers("/", "/login","/logout", "/register", "/login-error").permitAll()
                        .requestMatchers("/home").authenticated()
                        .requestMatchers("/courses","/courses/{id}").authenticated()
                        .requestMatchers("/resources","/resources/{id}").authenticated()
                        .requestMatchers("/students","/students/{id}","/students/{id1}/tasks/{id2}/upload","/students/{id}/tasks").authenticated()
                        .requestMatchers("/tasks/{id}").authenticated()
                        .requestMatchers("/error").permitAll()
                        .requestMatchers("/courses/**", "/students/**", "/tasks/**").hasRole(UserRolesEnum.TEACHER.name())
                        .requestMatchers("/courses/**").hasRole(UserRolesEnum.TEACHER.name())
                        // all other requests are authenticated.
                        .anyRequest().authenticated()
        ).formLogin(
                formLogin -> {
                    formLogin
                            // redirect here when we access something which is not allowed.
                            // also this is the page where we perform login.

                            .loginPage("/login")
                            // The names of the input fields (in our case in auth-login.html)
                            .usernameParameter("username")
                            .passwordParameter("password")
                            .defaultSuccessUrl("/home")
                            .failureForwardUrl("/login-error"); // Redirect after successful login

                }
        ).logout(
                logout -> {
                    logout
                            // the URL where we should POST something in order to perform the logout

                            .logoutUrl("/logout")
                            .logoutSuccessUrl("/")
                            .permitAll()
                            // invalidate the HTTP session
                            .invalidateHttpSession(true);
                }
        ).build();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {

        return new UniversityUserDetailsService(userRepository);
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}


