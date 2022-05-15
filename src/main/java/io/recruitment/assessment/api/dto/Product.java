package io.recruitment.assessment.api.dto;

import lombok.Data;

@Data
public class Product {

    private Integer productId;

    private String name;

    private String desc;

    private String imgUrl;

    private Double price;

    private Integer stockNumber;

    private String tag;

    private Integer deleteFlg;

    private Integer version;
}
