package io.recruitment.assessment.api.dto;

import lombok.Data;


@Data
public class OrderItem {
    private Integer orderItemId;

    private Integer orderId;

    private Integer productId;

    private Double totalPrice;

    private Integer productCount;
}
