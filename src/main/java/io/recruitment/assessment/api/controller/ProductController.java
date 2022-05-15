package io.recruitment.assessment.api.controller;

import io.recruitment.assessment.api.dto.Product;
import io.recruitment.assessment.api.dto.User;
import io.recruitment.assessment.api.exception.InternalErrorException;
import io.recruitment.assessment.api.exception.UnauthorizedErrorException;
import io.recruitment.assessment.api.service.AuthenticationService;
import io.recruitment.assessment.api.service.ProductService;
import io.recruitment.assessment.api.utils.PageQuery;
import io.recruitment.assessment.api.utils.PaginationResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/products")
@Api(tags = "Product APIs")
public class ProductController {

    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private ProductService productService;

    public final static int PRODUCT_SEARCH_PAGE_LIMIT = 3;

    @GetMapping("/{productId}")
    @ApiOperation(value = "Fetch a product info", tags = "v1")
    public Product getProduct(@PathVariable int productId, @RequestHeader("Authorization") String token) {
        authenticationService.authenticateUser(token);
        Product product = productService.getProductById(productId);

        return product;
    }

    @GetMapping("/search")
    @ApiOperation(value = "Search products", tags = "v1")
    public PaginationResult<List<Product>> searchProducts(@RequestParam String keyword,
                                                          @RequestParam(required = false) Integer pageNumber,
                                                          @RequestHeader("Authorization") String token) {
        authenticationService.authenticateUser(token);
        if (pageNumber == null || pageNumber < 1) {
            pageNumber = 1;
        }
        PageQuery pageQuery = new PageQuery(pageNumber, PRODUCT_SEARCH_PAGE_LIMIT, keyword.trim());

        return productService.searchProducts(pageQuery);
    }

    @PostMapping(value = "")
    @ApiOperation(value = "Add a new product", tags = "v1")
    public Product saveProduct(@RequestBody @Valid Product product, @RequestHeader("Authorization") String token) {
        User user = authenticationService.authenticateUser(token);
        if (user.getIsAdmin() != 1) {
            throw new UnauthorizedErrorException("Only admin user can add new product.");
        }
        return productService.saveProduct(product, user);
    }

    @PutMapping(value = "/{productId}")
    @ApiOperation(value = "Update a product info", tags = "v1")
    public Product updateProduct(@PathVariable int productId, @RequestBody @Valid Product product, @RequestHeader("Authorization") String token) {
        User user = authenticationService.authenticateUser(token);
        if (user.getIsAdmin() != 1) {
            throw new UnauthorizedErrorException("Only admin user can update product info.");
        }
        product.setProductId(productId);
        return productService.updateProduct(product, user);
    }

    @PutMapping(value = "/delete")
    @ApiOperation(value = "Delete products", tags = "v1")
    public void deleteProducts(@RequestParam(name = "productIds") Integer[] productIds, @RequestHeader("Authorization") String token) {
        User user = authenticationService.authenticateUser(token);
        if (user.getIsAdmin() != 1) {
            throw new UnauthorizedErrorException("Only admin user can delete products.");
        }
        if (productIds.length < 1) {
            throw new InternalErrorException("None product in request param.");
        }
        productService.deleteProducts(productIds);
    }

}
