package com.joven.poller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
public class PollDTO {
//    private String userEmail; // used for identification
    private Long userId;
    private String pollContent;
    private boolean onlyOneSelection;

}
