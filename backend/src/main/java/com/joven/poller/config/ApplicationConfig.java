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

import java.util.List;

@Configuration
public class ApplicationConfig {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PollRepository pollRepository;
    @Autowired
    private PollOptionRepository pollOptionRepository;

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
