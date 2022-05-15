package io.recruitment.assessment.api.entity;

import lombok.Data;

import java.util.Date;

@Data
public class UserEntity {
    private Integer userId;

    private String userName;

    private String passwordMd5;

    private Byte isAdmin;

    private Byte isDeleted;

    private Date createTime;
}
