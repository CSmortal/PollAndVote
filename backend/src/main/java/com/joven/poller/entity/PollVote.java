package com.joven.poller.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PollVote {
    // only if an option in a poll has been voted for by someone, will there be a row in this PollVote table

    @Id
    @GeneratedValue
    private Long pollVoteId;

    @ManyToOne(
            optional = false
    )
    @JoinColumn(
            name = "id_of_voter",
            referencedColumnName = "userId"
    )
    private User user;

    @ManyToOne(
            optional = false
    )
    @JoinColumn(
            name = "poll_option_id",
            referencedColumnName = "pollOptionId"
    )
    private PollOption pollOption;
}
