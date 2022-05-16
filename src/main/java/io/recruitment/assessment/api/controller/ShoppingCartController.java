package io.recruitment.assessment.api.controller;

import io.recruitment.assessment.api.dto.ShoppingCartItem;
import io.recruitment.assessment.api.dto.User;
import io.recruitment.assessment.api.exception.UnauthorizedErrorException;
import io.recruitment.assessment.api.service.AuthenticationService;
import io.recruitment.assessment.api.service.ShoppingCartService;
import io.recruitment.assessment.api.utils.PageQuery;
import io.recruitment.assessment.api.utils.PaginationResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/shopping-carts")
@Api(tags = "Shopping Cart APIs")
public class ShoppingCartController {

    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private ShoppingCartService shoppingCartService;

    public final static int SHOPPING_CART_PAGE_LIMIT = 5;

    @GetMapping("/page")
    @ApiOperation(value = "Get Shopping Cart Items", tags = "v1")
    public PaginationResult<List<ShoppingCartItem>> cartItemPageList(@RequestParam(required = false) Integer pageNumber,
                                                                     @RequestHeader("Authorization") String token) {
        User user = authenticationService.authenticateUser(token);
        if (user.getIsAdmin() != 0) {
            throw new UnauthorizedErrorException("Only customer user can use shopping cart.");
        }
        if (pageNumber == null || pageNumber < 1) {
            pageNumber = 1;
        }
        PageQuery pageQuery = new PageQuery(pageNumber, SHOPPING_CART_PAGE_LIMIT);

        return shoppingCartService.getItemsById(pageQuery, user.getUserId());

    }


    @PostMapping("")
    @ApiOperation(value = "Add Products to Shopping Cart", tags = "v1")
    public Integer saveShoppingCartItem(@RequestBody ShoppingCartItem shoppingCartItem,
                                     @RequestHeader("Authorization") String token) {
        User user = authenticationService.authenticateUser(token);
        if (user.getIsAdmin() != 0) {
            throw new UnauthorizedErrorException("Only customer user can use shopping cart.");
        }
        shoppingCartItem.setUserId(user.getUserId());
        return shoppingCartService.saveItem(shoppingCartItem);
    }


    /**
     * @TODO: [PUT] Update shopping cart items
     * @TODO: [DELETE] Remove shopping cart items
     */

}
