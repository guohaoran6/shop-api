package io.recruitment.assessment.api.entity;

import lombok.Data;

import java.util.Date;

@Data
public class UserTokenEntity {

    private Integer userId;

    private String token;

    private Date updateTime;

    private Date expireTime;

}
