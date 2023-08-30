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
    private Map<String,Double> mapOptionToPercentage;

    // we need to return this to client so that we know which options he/she selects when voting.
    @NotNull
    private Map<String,Long> mapOptionToOptionId;

    @NotNull
    private Integer totalVotes;

    @NotNull
    private Boolean isLimitedView;
}
