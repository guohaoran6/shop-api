package io.recruitment.assessment.api.event;

import lombok.Data;

@Data
public class EventEntity {
    private Integer productId;
    private String eventMessage;
    private Integer updatedBy;
}
