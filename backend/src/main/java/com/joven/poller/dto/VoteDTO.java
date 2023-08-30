package com.joven.poller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class VoteDTO {
    Long pollId;
    Long userIdOfVoter;
    List<Long> optionIdToVoteList;
}
