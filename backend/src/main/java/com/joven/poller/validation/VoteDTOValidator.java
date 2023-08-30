package com.joven.poller.validation;

import com.joven.poller.dto.VoteDTO;

public class VoteDTOValidator {
    public static boolean validate(VoteDTO voteDto) {
        if (voteDto.getPollId() == null) {
            return false;
        }
        if (voteDto.getUserIdOfVoter() == null) {
            return false;
        }
        if (voteDto.getOptionIdToVoteList().size() == 0) {
            return false;
        }

        return true;
    }
}
