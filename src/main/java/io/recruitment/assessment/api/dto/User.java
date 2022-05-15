package io.recruitment.assessment.api.dto;

import lombok.Data;


@Data
public class User {

    private Integer userId;

    private String userName;

    private String passwordMd5;

    private Byte isAdmin;

    private Byte deletedFlg;

}
