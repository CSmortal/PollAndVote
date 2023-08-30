package com.joven.poller.validation;

import com.joven.poller.dto.PollOptionDTO;

public class PollOptionDTOValidator {
    public static boolean validate(PollOptionDTO optionDTO) {
        String content = optionDTO.getPollOptionContent();

        if (content.isEmpty()) {
            return false;
        }
        return true;
    }
}
