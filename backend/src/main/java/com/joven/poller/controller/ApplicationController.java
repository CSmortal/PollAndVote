package com.joven.poller.controller;

import com.joven.poller.dto.PollDTO;
import com.joven.poller.dto.PollOptionDTO;
import com.joven.poller.entity.Poll;
import com.joven.poller.entity.PollOption;
import com.joven.poller.entity.User;
import com.joven.poller.exception.InvalidPollDataException;
import com.joven.poller.response.PollResponse;
import com.joven.poller.service.PollService;
import com.joven.poller.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api")
@RequiredArgsConstructor
public class ApplicationController {
    // 4 features: Create poll, End poll, View poll results, Vote on a poll

    // might have a 5th feature to revote if you already voted
    private final PollService pollService;

    private final UserService userService;

    @PostMapping(path = "/addPoll")
    public ResponseEntity<Boolean> createPoll(@RequestBody PollDTO pollDto, @RequestBody List<PollOptionDTO> pollOptionDtoList) throws InvalidPollDataException {
        // From the DTOs, we create the actual Poll, User and PollOption entities.
        Optional<User> userOpt = userService.findUserByEmail(pollDto.getUserEmail());
        if (userOpt.isEmpty()) {
            throw new InvalidPollDataException("no such user with the given email");
        }
        User userWhoIsCreatingPoll = userOpt.get();

        Poll newPoll = Poll.builder()
                .pollContent(pollDto.getPollContent())
                .isOnlyOneSelection(pollDto.isOnlyOneSelection())
                .hasEnded(false)
                .user(userWhoIsCreatingPoll)
                .build();

        List<PollOption> pollOptionsForNewPoll = new ArrayList<>();

        for (PollOptionDTO pollOptionDto : pollOptionDtoList) {
            PollOption pollOption = PollOption.builder()
                    .pollOptionContent(pollOptionDto.getPollOptionContent())
                    .poll(newPoll)
                    .build();
            pollOptionsForNewPoll.add(pollOption);
        }

        boolean isCreated = pollService.createPoll(newPoll, pollOptionsForNewPoll);

        if (!isCreated) {
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.ok().build();
        }
    }

    @PutMapping(path = "/endPoll")
    public ResponseEntity<Boolean> endPoll(@RequestBody Poll poll) {
        try {
            pollService.endPoll(poll);
            return ResponseEntity.ok().build();
        } catch (InvalidPollDataException e) {
            return ResponseEntity.badRequest().build();
        }

    }

    @GetMapping(path = "/getPoll")
    public ResponseEntity<PollResponse> getPollResults(@RequestBody Long pollId, @RequestBody User actionRequester) {
        try {
            PollResponse result = pollService.getPollResults(pollId, actionRequester);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().build(); // might edit later
        }
    }

    @PostMapping(path = "/vote")
    public ResponseEntity<Boolean> voteOnPoll(
            @RequestParam Long pollId,
            @RequestParam Long userId,
            @RequestBody List<Long> listOfSelectedOptionIds
            ) {
        try {
            boolean isSuccessfulInVoting = pollService.voteOnPoll(pollId, userId, listOfSelectedOptionIds);

            if (isSuccessfulInVoting) {
                return ResponseEntity.ok(true);
            } else {
                return ResponseEntity.ok(false);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body(false); // might edit later
        }
    }

}
