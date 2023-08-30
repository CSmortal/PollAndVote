package com.joven.poller.validation;

import com.joven.poller.dto.PollDTO;

public class PollDTOValidator {

    public static boolean validate(PollDTO pollDto) {
        if (pollDto.getUserId() == null) {
            return false;
        }
        String content = pollDto.getPollContent();
        if (content.isEmpty() || content.length() <= 3) {
            return false;
        }
        return true;
    }
}
