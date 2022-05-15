package io.recruitment.assessment.api.controller;

import io.recruitment.assessment.api.dto.User;
import io.recruitment.assessment.api.exception.InternalErrorException;
import io.recruitment.assessment.api.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/api/v1/user")
@Api(tags = "User APIs")
public class UserController {

    @Autowired
    private UserService userService;

    public final static int TOKEN_LENGTH = 32; //token length


    @PostMapping(value = "/login")
    @ApiOperation(value = "User login", tags = "v1")
    public String login(@RequestBody @Valid User user) {
        String token = userService.login(user.getUserName(), user.getPasswordMd5());

        if (!token.isEmpty() && token.length() == TOKEN_LENGTH) {
            return token;
        } else {
            throw new InternalErrorException("Error on generating user token.");
        }
    }

    @PutMapping(value = "/logout")
    @ApiOperation(value = "User logout", tags = "v1")
    public void logout(@RequestBody @Valid User user) {
        userService.logout(user.getUserId());
    }
}
