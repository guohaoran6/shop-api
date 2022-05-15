package io.recruitment.assessment.api.entity;

import lombok.Data;


@Data
public class ShoppingCartEntity {
    private Integer cartItemId;

    private Integer userId;

    private Integer productId;

    private Integer productCount;

    private Byte deletedFlg;

}
