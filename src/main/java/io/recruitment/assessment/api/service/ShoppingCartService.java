package io.recruitment.assessment.api.service;

import io.recruitment.assessment.api.dto.Product;
import io.recruitment.assessment.api.dto.ShoppingCartItem;
import io.recruitment.assessment.api.entity.ShoppingCartEntity;
import io.recruitment.assessment.api.exception.InternalErrorException;
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
     * @param userId
     */
    public void saveItem(ShoppingCartItem shoppingCartItem, Integer userId) {
        ShoppingCartEntity temp = shoppingCartRepository.findItemByProductId(shoppingCartItem.getProductId(), userId);
        if (temp != null) {
            throw new InternalErrorException("");
        }
        Product product = productService.getProductById(shoppingCartItem.getProductId());
        //商品为空
        if (product == null) {
            throw new InternalErrorException("");
        }
        //超出单个商品的最大数量
        if (shoppingCartItem.getProductCount() < 1) {
            throw new InternalErrorException("");
        }
        //超出单个商品的最大数量
        if (shoppingCartItem.getProductCount() > product.getStockNumber()) {
            throw new IllegalArgumentException("");
        }
        ShoppingCartEntity shoppingCartEntity = modelMapper.map(shoppingCartItem, ShoppingCartEntity.class);
        //保存记录
        shoppingCartRepository.save(shoppingCartEntity, userId);
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
