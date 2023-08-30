package com.joven.poller.validation;

import com.joven.poller.dto.CreatePollDTO;
import com.joven.poller.dto.PollOptionDTO;
import com.joven.poller.entity.PollOption;

import java.util.List;

public class CreatePollDTOValidator {
    public static boolean validate(CreatePollDTO createPollDto) {
        if (!PollDTOValidator.validate(createPollDto.getPollDto())) {
            return false;
        }
        List<PollOptionDTO> pollOptionsDtoList = createPollDto.getPollOptionDtoList();

        for (PollOptionDTO pollOptionDto : pollOptionsDtoList) {
            if (!PollOptionDTOValidator.validate(pollOptionDto)) {
                return false;
            }
        }
        return true;
    }
}
