package com.joven.poller.service;

import com.joven.poller.entity.Poll;
import com.joven.poller.entity.PollOption;
import com.joven.poller.entity.PollVote;
import com.joven.poller.entity.User;
import com.joven.poller.exception.InvalidPollDataException;
import com.joven.poller.repository.PollOptionRepository;
import com.joven.poller.repository.PollRepository;
import com.joven.poller.repository.PollVoteRepository;
import com.joven.poller.repository.UserRepository;
import com.joven.poller.response.PollResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.*;

@Service
@RequiredArgsConstructor
public class PollService {

    private final UserRepository userRepository;
    private final PollRepository pollRepository;
    private final PollOptionRepository pollOptionRepository;
    private final PollVoteRepository pollVoteRepository;

    public boolean createPoll(@RequestBody Poll newPoll, @RequestBody List<PollOption> pollOptions) {
        try {
            pollRepository.save(newPoll); // might need to move

            for (PollOption pollOption : pollOptions) {
                pollOptionRepository.save(pollOption);
            }
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public void endPoll(@RequestBody Poll poll) throws InvalidPollDataException {
        Poll pollToEnd = pollRepository.findById(poll.getPollId())
                .orElseThrow(InvalidPollDataException::new);

        pollRepository.endPoll(pollToEnd);
    }

    public PollResponse getPollResults(Long pollId, User requestor) throws InvalidPollDataException {
        // get the poll from db. If the poll hasnt ended, then we return a diff response, unless the person who requested this action is the poll creator himself
        // if poll has ended, then everyone can see the results

        // 1. Get poll from db
        Optional<Poll> pollOpt = pollRepository.findById(pollId);

        if (pollOpt.isEmpty()) {
            throw new InvalidPollDataException("No such poll with this id exists");
        }

        Map<String,Double> mapOptionsToPercentageVoted = new HashMap<>();
        Map<String,Long> mapOptionToOptionId = new HashMap<>();
        Poll poll = pollOpt.get();
        List<PollOption> pollOptions = pollOptionRepository.findPollOptionsByPoll(poll);

        int totalVotesCount = 0;
        for (PollOption option : pollOptions) {
            Integer votesForOption =  pollVoteRepository.countPollVotesForPollOption(option);
            mapOptionsToPercentageVoted.put(option.getPollOptionContent(), Double.valueOf(votesForOption));
            mapOptionToOptionId.put(option.getPollOptionContent(), option.getPollOptionId());
            totalVotesCount += votesForOption;
        }

        for (Map.Entry<String,Double> entry : mapOptionsToPercentageVoted.entrySet()) {
            entry.setValue(entry.getValue() / totalVotesCount);
        }

        if (poll.isHasEnded() || poll.getUser().equals(requestor)) {
            return PollResponse.builder()
                    .isLimitedView(false)
                    .totalVotes(totalVotesCount)
                    .mapOptionToPercentage(mapOptionsToPercentageVoted)
                    .mapOptionToOptionId(mapOptionToOptionId)
                    .build();
        } else {
            return PollResponse.builder()
                    .isLimitedView(true)
                    .totalVotes(totalVotesCount)
                    .mapOptionToOptionId(mapOptionToOptionId)
                    .build();
        }
    }

    public List<Poll> getAllPolls() {
        return pollRepository.findAll();
    }

//    public Map<PollOption,Integer> getPollResults(Poll poll) {
//        List<PollOption> allOptionsForPoll = pollOptionRepository.findPollOptionsByPoll(poll);
//
//        Map<PollOption,Integer> result = new HashMap<>();
//
//        for (PollOption option : allOptionsForPoll) {
//            Integer countForOption = pollVoteRepository.countPollVotesForPollOption(option);
//            result.put(option, countForOption);
//        }
//
//        return result;
//    }

    public boolean voteOnPoll(Long pollId, Long userIdOfVoter, List<Long> listOfSelectedOptionIds) throws InvalidPollDataException {
        // we have to check if the poll has ended, and also if the pollId is valid
        Optional<Poll> pollOpt = pollRepository.findById(pollId);

        if (pollOpt.isEmpty()) {
            throw new InvalidPollDataException(String.format("pollId of %d is invalid", pollId));
        }
        Poll poll = pollOpt.get();

        if (poll.isHasEnded()) {
            return false;
        }
        // if can vote, we need to check if the listOfSelectedIds is 0.
        // If more than 1, we need to check if the poll allows multiple selections
        if (listOfSelectedOptionIds.isEmpty()) {
            throw new InvalidPollDataException("no options were selected when voting");
        }
        if (listOfSelectedOptionIds.size() > 1 && poll.isOnlyOneSelection()) {
            throw new InvalidPollDataException("more than 1 option was selected for a poll allowing only 1 selection");
        } else {
            List<PollVote> newPollVotes = new ArrayList<>();
            Optional<User> voterOpt = userRepository.findById(userIdOfVoter);

            if (voterOpt.isEmpty()) {
                throw new InvalidPollDataException("userId of voter provided is invalid");
            }
            User voter = voterOpt.get();

            for (Long selectedPollOptionId : listOfSelectedOptionIds) {
                Optional<PollOption> pollOptionOpt = pollOptionRepository.findById(selectedPollOptionId);

                if (pollOptionOpt.isEmpty()) {
                    throw new InvalidPollDataException("at least one of the provided poll option id is invalid");
                }
                PollOption selectedPollOption = pollOptionOpt.get();
                PollVote newVote = PollVote.builder()
                        .user(voter)
                        .pollOption(selectedPollOption)
                        .build();
                newPollVotes.add(newVote);
            }
            pollVoteRepository.saveAll(newPollVotes);
            return true;
        }
    }
}
