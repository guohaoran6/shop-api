package io.recruitment.assessment.api.repository;

import io.recruitment.assessment.api.entity.OrderItemEntity;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;


@Repository
@Mapper
public interface OrderItemRepository {

    @Insert("INSERT INTO order_item (order_id, product_id, total_price, product_count)" +
            "VALUES ( #{orderItemEntity.orderId}, #{orderItemEntity.productId}, #{orderItemEntity.totalPrice}, " +
            "#{orderItemEntity.productCount} )")
    @Options(useGeneratedKeys = true, keyProperty = "orderItemEntity.orderItemId", keyColumn = "order_item_id")
    void save(@Param("orderItemEntity") OrderItemEntity orderItemEntity);


}
