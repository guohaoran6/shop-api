package io.recruitment.assessment.api.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public class Event {

    Integer productId;
    Map<String, String> eventMessage;
    Integer updatedBy;

}
