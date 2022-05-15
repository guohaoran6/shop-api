package io.recruitment.assessment.api.repository;

import io.recruitment.assessment.api.entity.OrderEntity;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface OrderRepository {

    @Insert("INSERT INTO `order` (`order_no`, `user_id`, `total_price`) " +
            "VALUES ( #{orderEntity.orderNo}, #{orderEntity.userId}, #{orderEntity.totalPrice} )")
    @Options(useGeneratedKeys = true, keyProperty = "orderEntity.orderId", keyColumn = "order_id")
    void save(@Param("orderEntity") OrderEntity orderEntity);


}
