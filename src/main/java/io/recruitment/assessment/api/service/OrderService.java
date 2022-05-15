package io.recruitment.assessment.api.service;

import io.recruitment.assessment.api.dto.Order;
import io.recruitment.assessment.api.dto.OrderItem;
import io.recruitment.assessment.api.dto.Product;
import io.recruitment.assessment.api.dto.ShoppingCartItem;
import io.recruitment.assessment.api.entity.OrderEntity;
import io.recruitment.assessment.api.exception.InternalErrorException;
import io.recruitment.assessment.api.repository.OrderRepository;
import io.recruitment.assessment.api.utils.NumberUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    OrderRepository orderRepository;
    @Autowired
    ProductService productService;
    @Autowired
    ShoppingCartService shoppingCartService;
    @Autowired
    OrderItemService orderItemService;

    private static ModelMapper modelMapper = new ModelMapper();


    /**
     * @Description Generate Order
     * @param cartItemIdList
     * @param userId
     * @return
     *
     * @TODO: Involve Resilience4j-CircuitBreaker to protect the process and to control high traffic.
     */
    @Transactional
    public String generateOder(Integer[] cartItemIdList, Integer userId) {
        List<ShoppingCartItem> shoppingCartItems = shoppingCartService.getItemList(cartItemIdList, userId);

        if (CollectionUtils.isEmpty(shoppingCartItems)) {
            throw new InternalErrorException("Cannot find shopping cart items.");
        }

        List<Integer> itemIdList = shoppingCartItems.stream().map(ShoppingCartItem::getCartItemId).collect(Collectors.toList());
        List<Integer> productIds = shoppingCartItems.stream().map(ShoppingCartItem::getProductId).collect(Collectors.toList());
        List<Product> products = productService.getProducts(productIds);
        // Check if the product is deleted.
        List<Product> productsDeleted = products.stream().filter(product -> product.getDeleteFlg() == 1).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(productsDeleted)) {
            // If not empty means that includes deleted products.
            throw new InternalErrorException(productsDeleted.get(0).getName() + "Cannot generate order for deleted products.");
        }
        Map<Integer, Product> productMap = products.stream().collect(Collectors.toMap(Product::getProductId, Function.identity(), (entity1, entity2) -> entity1));
        // Check the stock
        for (ShoppingCartItem shoppingCartItem : shoppingCartItems) {
            if (!productMap.containsKey(shoppingCartItem.getProductId())) {
                throw new InternalErrorException("Cannot find product in user's shopping cart.");
            }
            if (shoppingCartItem.getProductCount() > productMap.get(shoppingCartItem.getProductId()).getStockNumber()) {
               throw new InternalErrorException("Insufficient inventory.");
            }
        }
        // Delete shopping cat items
        if (!CollectionUtils.isEmpty(itemIdList) && !CollectionUtils.isEmpty(productIds) && !CollectionUtils.isEmpty(products)) {
            if (shoppingCartService.deleteItems(itemIdList) > 0) {
                // Generate order number.
                String orderNo = NumberUtil.genOrderNo();
                double priceTotal = 0.0;
                // Update stock number in product table.
                for (ShoppingCartItem cartItem : shoppingCartItems) {
                    productService.updateProductStockNumber(cartItem.getCartItemId(), cartItem.getProductId());
                    priceTotal += cartItem.getProductCount() * products.stream().filter(
                            product -> product.getProductId().equals(cartItem.getProductId())
                    ).findAny().get().getPrice();
                }
                // Save Order
                Order order = new Order();
                order.setOrderNo(orderNo);
                order.setUserId(userId);
                order.setTotalPrice(priceTotal);
                Integer orderId = saveOrder(order);
                // Save Order items
                List<OrderItem> orderItems = new ArrayList<>();
                shoppingCartItems.forEach(shoppingCartItem -> {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setOrderId(orderId);
                    orderItem.setProductId(shoppingCartItem.getProductId());
                    orderItem.setProductCount(shoppingCartItem.getProductCount());
                    orderItem.setTotalPrice(shoppingCartItem.getProductCount() * products.stream().filter(
                            product -> product.getProductId().equals(shoppingCartItem.getProductId())
                    ).findAny().get().getPrice());
                    orderItems.add(orderItem);
                });
                orderItemService.saveOrderItems(orderItems);
                return orderNo;
            } else {
                throw new InternalErrorException("Cannot delete shopping cart items.");
            }
        } else {
            throw new InternalErrorException("Data missing in shopping cart or products.");
        }
    }


    /**
     * @Description Save Order
     * @param order
     * @return
     */
    public Integer saveOrder(Order order) {
        OrderEntity orderEntity = modelMapper.map(order, OrderEntity.class);
        orderRepository.save(orderEntity);

        return orderEntity.getOrderId();
    }

}
