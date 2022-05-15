package io.recruitment.assessment.api.entity;

import lombok.Data;


@Data
public class OrderItemEntity {

    private Integer orderItemId;

    private Integer orderId;

    private Integer productId;

    private Double totalPrice;

    private Integer productCount;

}
