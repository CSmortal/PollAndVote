package com.joven.poller.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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

    @Column(name = "content")
    private String pollContent;

    @Column(name = "one_choice_only")
    private boolean onlyOneSelection;

    @Column(name = "ended")
    private boolean hasEnded = false; // poll must be manually ended by user

    @ManyToOne(
            optional = false
    )
    @JoinColumn(
            name = "id_of_poll_creator",
            referencedColumnName = "userId"
    )
    private User user;

}
