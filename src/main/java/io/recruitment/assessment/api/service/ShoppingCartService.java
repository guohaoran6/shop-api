package io.recruitment.assessment.api.service;

import io.recruitment.assessment.api.dto.Product;
import io.recruitment.assessment.api.dto.ShoppingCartItem;
import io.recruitment.assessment.api.entity.ShoppingCartEntity;
import io.recruitment.assessment.api.exception.IllegalArgumentException;
import io.recruitment.assessment.api.exception.InternalErrorException;
import io.recruitment.assessment.api.exception.ResourceNotFoundException;
import io.recruitment.assessment.api.repository.ShoppingCartRepository;
import io.recruitment.assessment.api.utils.PageQuery;
import io.recruitment.assessment.api.utils.PaginationResult;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class ShoppingCartService {

    @Autowired
    ShoppingCartRepository shoppingCartRepository;
    @Autowired
    ProductService productService;

    private static ModelMapper modelMapper = new ModelMapper();

    /**
     * @Description Get shopping cart item list by user id
     * @param pageQuery
     * @param userId
     * @return
     */
    public PaginationResult getItemsById(PageQuery pageQuery, Integer userId) {
        List<ShoppingCartEntity> shoppingCartEntities = shoppingCartRepository.findItemsByUserId(pageQuery, userId);
        List<ShoppingCartItem> shoppingCartItems = new ArrayList<>();
        if (!CollectionUtils.isEmpty(shoppingCartEntities)) {
            shoppingCartEntities.forEach(shoppingCartEntity -> {
                ShoppingCartItem shoppingCartItem = modelMapper.map(shoppingCartEntity, ShoppingCartItem.class);
                Double total = productService.getProductById(shoppingCartEntity.getProductId()).getPrice()
                        * shoppingCartEntity.getProductCount();
                shoppingCartItem.setTotal(total);
                shoppingCartItems.add(shoppingCartItem);
            });
        }
        int totalCount = shoppingCartRepository.findTotalItemsByUserId(userId);

        return new PaginationResult(shoppingCartItems, totalCount, pageQuery.getLimit(), pageQuery.getPage());
    }

    /**
     * @Description Get item list by cart item id array
     * @param cartItemIds
     * @param userId
     * @return
     */
    public List<ShoppingCartItem> getItemList(Integer[] cartItemIds, Integer userId) {
        List<ShoppingCartEntity> shoppingCartEntities = shoppingCartRepository.findItemList(cartItemIds, userId);
        List<ShoppingCartItem> shoppingCartItems = new ArrayList<>();
        shoppingCartEntities.forEach(shoppingCartEntity -> shoppingCartItems.add(modelMapper.map(shoppingCartEntity, ShoppingCartItem.class)));

        return shoppingCartItems;
    }


    /**
     * @Description Save shopping cart item
     * @param shoppingCartItem
     */
    public Integer saveItem(ShoppingCartItem shoppingCartItem) {
        ShoppingCartEntity shoppingCartEntityCheck = shoppingCartRepository.findItemByProductId(
                shoppingCartItem.getProductId(), shoppingCartItem.getUserId());
        if (shoppingCartEntityCheck != null) {
            throw new InternalErrorException("Shopping cart item existed.");
        }
        Product product = productService.getProductById(shoppingCartItem.getProductId());
        if (product == null) {
            throw new ResourceNotFoundException("Cannot find products.");
        }
        if (shoppingCartItem.getProductCount() < 1) {
            throw new IllegalArgumentException("Product count number must be above 1.");
        }
        if (shoppingCartItem.getProductCount() > product.getStockNumber()) {
            throw new InternalErrorException("Product count number is over stock number.");
        }
        ShoppingCartEntity shoppingCartEntity = modelMapper.map(shoppingCartItem, ShoppingCartEntity.class);
        shoppingCartRepository.save(shoppingCartEntity);

        return shoppingCartEntity.getCartItemId();
    }

    /**
     * @Description Delete item list
     * @param itemIdList
     * @return
     */
    public Integer deleteItems(List<Integer> itemIdList) {
        return shoppingCartRepository.deleteItems(itemIdList);
    }



}
