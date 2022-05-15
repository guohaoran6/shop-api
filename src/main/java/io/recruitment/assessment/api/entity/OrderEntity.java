package io.recruitment.assessment.api.entity;

import lombok.Data;

import java.util.Date;

@Data
public class OrderEntity {

    private Integer orderId;

    private String orderNo;

    private Long userId;

    private Integer totalPrice;

    private Byte payStatus;

    private Byte payType;

    private Date payTime;

    private Byte orderStatus;

    private String extraInfo;

    private Byte isDeleted;

    private Date createTime;

    private Date updateTime;
}
