package com.joven.poller.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Poll {
    @Id
    @GeneratedValue
    private Long pollId;

    @NotNull
    private String pollContent;

    @NotNull
    private boolean isOnlyOneSelection;

    @NotNull
    private boolean hasEnded; // poll must be manually ended by user

    @ManyToOne(
            optional = false
    )
    @JoinColumn(
            name = "id_of_poll_creator",
            referencedColumnName = "userId"
    )
    private User user;

}
