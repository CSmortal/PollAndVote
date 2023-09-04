package com.joven.poller.response;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PollResponse {
    private Map<String,Double> mapOptionContentToPercentage;

    // we need to return this to client so that we know which options he/she selects when voting.
    @NotNull
    private Map<Long,String> mapOptionIdToOptionContent;

    @NotNull
    private Long totalVotes;

    private boolean hasLimitedView;

    private boolean hasUserVoted;

    private boolean voteOnlyForOneOption;

    @NotNull
    private Map<Long,Boolean> mapOptionIdToVoteStatus; // will only contain optionIds that have been voted for, so if user did not vote, this map is empty

    @NotNull
    private Long userIdOfPollCreator;
}
