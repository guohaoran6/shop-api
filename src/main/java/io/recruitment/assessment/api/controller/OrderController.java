package io.recruitment.assessment.api.controller;

import io.recruitment.assessment.api.dto.User;
import io.recruitment.assessment.api.exception.InternalErrorException;
import io.recruitment.assessment.api.exception.UnauthorizedErrorException;
import io.recruitment.assessment.api.service.AuthenticationService;
import io.recruitment.assessment.api.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/api/v1/orders")
@Api(tags = "Order APIs")
public class OrderController {

    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private OrderService orderService;

    @PostMapping("/save")
    @ApiOperation(value = "Generate Order", tags = "v1")
    public String generateOder(@RequestParam @Valid Integer[] cartItemIds, @RequestHeader("Authorization") String token) {
        User user = authenticationService.authenticateUser(token);
        if (user.getIsAdmin() != 0) {
            throw new UnauthorizedErrorException("Only customer user can generate order.");
        }
        if (cartItemIds.length < 1) {
            throw new InternalErrorException("Non shopping cart item ids.");
        }
        return orderService.generateOder(cartItemIds, user.getUserId());
    }

    /**
     * @TODO: [GET] Get Order List
     * @TODO: [GET] Get an Order Detail Info
     * @TODO: [PUT] Cancel Order
     * @TODO: [PUT] Update Order
     */

}
