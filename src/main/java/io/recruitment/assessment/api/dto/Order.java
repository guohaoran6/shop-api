package io.recruitment.assessment.api.dto;

import lombok.Data;

import java.util.Date;

@Data
public class Order {
    private Integer orderId;

    private String orderNo;

    private Integer userId;

    private Double totalPrice;

    private Byte payStatus;

    private Byte payType;

    private Date payTime;

    private Byte orderStatus;

    private String extraInfo;
}
