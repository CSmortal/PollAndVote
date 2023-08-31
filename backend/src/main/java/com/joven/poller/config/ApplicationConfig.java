package com.joven.poller.config;

import com.joven.poller.Role;
import com.joven.poller.entity.Poll;
import com.joven.poller.entity.User;
import com.joven.poller.repository.PollOptionRepository;
import com.joven.poller.repository.PollRepository;
import com.joven.poller.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private UserRepository userRepository;
    private PollRepository pollRepository;
    private PollOptionRepository pollOptionRepository;


    @Bean
    public UserDetailsService userDetailsService() {
        // the below is an implementation of loadUserByUsername method that UserDetailsService implementations have to implement
        return username -> userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setPasswordEncoder(passwordEncoder());
        authProvider.setUserDetailsService(userDetailsService());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CommandLineRunner commandLineRunner() {
        return args -> {
            User testUser1 = User.builder()
                    .userId(1L)
                    .userName("Joven")
                    .email("test@gmail.com")
                    .password("1234")
                    .role(Role.USER)
                    .build();
            User testUser2 = User.builder()
                    .userId(2L)
                    .userName("Joel")
                    .email("joel@gmail.com")
                    .password("1234")
                    .role(Role.USER)
                    .build();
            User testUser3 = User.builder()
                    .userId(3L)
                    .userName("Jonathan")
                    .email("sim@gmail.com")
                    .password("1234")
                    .role(Role.USER)
                    .build();
            userRepository.saveAll(List.of(testUser1, testUser2, testUser3));

//            Poll newPoll1 = Poll.builder()
//                    .pollId(1L)
//                    .pollContent("What is good for a Wednesday dinner?")
//                    .hasEnded(false)
//                    .onlyOneSelection(true)
//                    .user(testUser)
//                    .build();
//            Poll newPoll2 = Poll.builder()
//                    .pollId(2L)
//                    .pollContent("Who should we vote in the election? Multiple answers allowed.")
//                    .hasEnded(false)
//                    .onlyOneSelection(false)
//                    .user(testUser)
//                    .build();
//            pollRepository.save(newPoll1);
//            pollRepository.save(newPoll2);



        };
    }
}
