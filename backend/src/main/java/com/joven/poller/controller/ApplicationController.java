package com.joven.poller.controller;

import com.joven.poller.dto.CreatePollDTO;
import com.joven.poller.dto.PollDTO;
import com.joven.poller.dto.PollOptionDTO;
import com.joven.poller.dto.VoteDTO;
import com.joven.poller.entity.Poll;
import com.joven.poller.entity.PollOption;
import com.joven.poller.entity.User;
import com.joven.poller.exception.InvalidPollDataException;
import com.joven.poller.response.PollResponse;
import com.joven.poller.response.PollSurface;
import com.joven.poller.service.PollService;
import com.joven.poller.service.UserService;
import com.joven.poller.validation.CreatePollDTOValidator;
import com.joven.poller.validation.PollDTOValidator;
import com.joven.poller.validation.VoteDTOValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api")
@RequiredArgsConstructor
public class ApplicationController {
    public static final String INCORRECT_REQUEST_BODY = "body of request is not correct";
    // 4 features: Create poll, End poll, View poll results, Vote on a poll

    // might have a 5th feature to revote if you already voted
    private final PollService pollService;

    private final UserService userService;

    @PostMapping(path = "/addPoll")
    public ResponseEntity<Boolean> createPoll(@RequestBody CreatePollDTO createPollDto) throws InvalidPollDataException {
        // From the DTOs, we create the actual Poll, User and PollOption entities.
        boolean isDtoInvalid = !CreatePollDTOValidator.validate(createPollDto);

        if (isDtoInvalid) {
            throw new InvalidPollDataException(INCORRECT_REQUEST_BODY);
        }

        PollDTO pollDto = createPollDto.getPollDto();
        List<PollOptionDTO> pollOptionDtoList = createPollDto.getPollOptionDtoList();

//        Optional<User> userOpt = userService.findUserByEmail(pollDto.getUserEmail());
//        if (userOpt.isEmpty()) {
//            throw new InvalidPollDataException("no such user with the given email");
//        }
//        User userWhoIsCreatingPoll = userOpt.get();
        User userWhoIsCreatingPoll = new User(pollDto.getUserId());

        Poll newPoll = Poll.builder()
                .pollContent(pollDto.getPollContent())
                .onlyOneSelection(pollDto.isOnlyOneSelection())
                .hasEnded(false)
                .user(userWhoIsCreatingPoll)
                .totalVotes(0L)
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
            return ResponseEntity.badRequest().body(false);
        } else {
            return ResponseEntity.ok(true);
        }
    }

    @PutMapping(path = "/endPoll/{pollId}")
    public ResponseEntity<Boolean> endPoll(@PathVariable(value = "pollId") Long pollId) {
        try {
            pollService.endPoll(pollId);
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body(false);
        }

    }

    @GetMapping(path="/getAllPolls")
    public ResponseEntity<List<PollSurface>> getAllPollsSurface() {
        List<PollSurface> result = pollService.getAllPollsSurface();
        return ResponseEntity.ok(result);
    }

    @GetMapping(path = "/getPoll")
    public ResponseEntity<PollResponse> getPollResults(@RequestParam Long pollId, @RequestParam Long userId) {
        // need to return the options that the user selected as well, if he/she alr voted
        try {
            PollResponse result = pollService.getPollResults(pollId, userId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().build(); // might edit later
        }
    }

    @PostMapping(path = "/vote")
    public ResponseEntity<Boolean> voteOnPoll(@RequestBody VoteDTO voteDto) throws InvalidPollDataException {
        boolean isDtoInvalid = !VoteDTOValidator.validate(voteDto);
        if (isDtoInvalid) {
            throw new InvalidPollDataException(INCORRECT_REQUEST_BODY);
        }

        try {
            boolean isSuccessfulInVoting = pollService.voteOnPoll(
                    voteDto.getPollId(),
                    voteDto.getUserIdOfVoter(),
                    voteDto.getOptionIdToVoteList());

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
