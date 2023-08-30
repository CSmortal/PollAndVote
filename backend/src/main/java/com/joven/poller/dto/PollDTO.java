package com.joven.poller.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PollDTO {
    private String userEmail; // used for identification
    private String pollContent;
    private boolean isOnlyOneSelection;
}
