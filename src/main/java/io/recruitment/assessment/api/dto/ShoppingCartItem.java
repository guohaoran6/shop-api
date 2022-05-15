package io.recruitment.assessment.api.dto;

import lombok.Data;

@Data
public class ShoppingCartItem {

    private Integer cartItemId;

    private Integer userId;

    private Integer productId;

    private Integer productCount;

    private Double total;

}
