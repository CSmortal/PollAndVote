package com.joven.poller;

import com.joven.poller.authObject.AuthenticationRequest;
import com.joven.poller.authObject.AuthenticationResponse;
import com.joven.poller.dto.CreatePollDTO;
import com.joven.poller.dto.PollDTO;
import com.joven.poller.dto.PollOptionDTO;
import com.joven.poller.entity.Poll;
import com.joven.poller.entity.User;
import com.joven.poller.repository.PollOptionRepository;
import com.joven.poller.repository.PollRepository;
import com.joven.poller.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class PollerApplicationTests {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PollRepository pollRepository;

    @Autowired
    private PollOptionRepository pollOptionRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private TestRestTemplate restTemplate = new TestRestTemplate();

//    @Autowired
//    public PollerApplicationTests(UserRepository userRepository, PollRepository pollRepository) {
//        this.userRepository = userRepository;
//        this.pollRepository = pollRepository;
//        this.restTemplate = new TestRestTemplate();
//    }

//    @BeforeEach
//    public void beforeAll() {
//        User user = User.builder()
//                .userName("Joven")
//                .email("test@gmail.com")
//                .password(passwordEncoder.encode("1234"))
//                .role(Role.USER)
//                .build();
//        userRepository.save(user);
//    }


    @Test
    public void createPoll_authenticated_success() {
        String loginUrl = "http://localhost:8080/auth/login";
        AuthenticationRequest authRequest = AuthenticationRequest.builder()
                .email("test@gmail.com")
                .password("1234")
                .build();
        ResponseEntity<AuthenticationResponse> response
                = restTemplate.postForEntity(loginUrl, authRequest, AuthenticationResponse.class);
        System.out.println(response.getBody());
        assertEquals(200, response.getStatusCode());
        AuthenticationResponse authResponse = response.getBody();
        assertTrue(authResponse.isSuccess());

        String addPollUrl = "http://localhost:8080/api/addPoll";
        PollDTO pollDTO = PollDTO.builder()
                .userId(1L)
                .pollContent("Whats for thursday?")
                .onlyOneSelection(true)
                .build();

        PollOptionDTO pollOptionDTO1 = PollOptionDTO.builder()
                .pollOptionContent("Laksa")
                .build();
        PollOptionDTO pollOptionDTO2 = PollOptionDTO.builder()
                .pollOptionContent("Nasi Lemak")
                .build();
        PollOptionDTO pollOptionDTO3 = PollOptionDTO.builder()
                .pollOptionContent("Hokkien Mee")
                .build();
        CreatePollDTO createPollDTO = CreatePollDTO.builder()
                .pollDto(pollDTO)
                .pollOptionDtoList(List.of(pollOptionDTO1, pollOptionDTO2, pollOptionDTO3))
                .build();
        ResponseEntity<Boolean> createPollResponse
                = restTemplate.postForEntity(addPollUrl, createPollDTO, Boolean.class);
        Optional<Poll> createdPollOpt = pollRepository.findById(1L);
        assertTrue(createdPollOpt.isPresent());

    }

}
