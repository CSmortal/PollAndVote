package com.joven.poller.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PollSurface {
    @NotNull
    @NotBlank
    private String pollContent;

    @NotNull
    @NotBlank
    private String nameOfPoster;

    @NotNull
    private Long totalVotes;

    @NotNull
    private Long pollId;

    private boolean pollEnded;
}
