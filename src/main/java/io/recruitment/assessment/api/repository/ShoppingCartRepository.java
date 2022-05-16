package io.recruitment.assessment.api.repository;

import io.recruitment.assessment.api.entity.ShoppingCartEntity;
import io.recruitment.assessment.api.utils.PageQuery;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface ShoppingCartRepository {

    @Select("<script>" +
            "SELECT * FROM shopping_cart WHERE user_id = #{userId} AND delete_flg = 0 " +
            "<if test=\"pageQuery.start != null and pageQuery.limit != null\"> limit #{pageQuery.start}, #{pageQuery.limit} </if>" +
            "</script>")
    List<ShoppingCartEntity> findItemsByUserId(@Param("pageQuery") PageQuery pageQuery, Integer userId);

    @Select("SELECT COUNT(*) FROM shopping_cart WHERE delete_flg = 0 AND user_id = #{userId}")
    int findTotalItemsByUserId(Integer userId);

    @Select("SELECT * FROM shopping_cart WHERE user_id = #{userId} AND product_id = #{productId} AND delete_flg = 0 ")
    ShoppingCartEntity findItemByProductId(Integer productId, Integer userId);

    @Select("<script>" +
            "SELECT * FROM shopping_cart WHERE user_id = #{userId} AND delete_flg = 0 " +
            "<if test=\"cartItemIds != null\"> AND cart_item_id IN " +
            "<foreach item='cartItemId' index='index' collection='cartItemIds' open='(' separator=',' close=')'>#{cartItemId}</foreach></if>" +
            "</script>")
    List<ShoppingCartEntity> findItemList(Integer[] cartItemIds, Integer userId);

    @Insert("INSERT INTO shopping_cart (user_id, product_id, product_count)" +
            "VALUES ( #{shoppingCartEntity.userId}, #{shoppingCartEntity.productId}, #{shoppingCartEntity.productCount} )")
    @Options(useGeneratedKeys = true, keyProperty = "shoppingCartEntity.cartItemId", keyColumn = "cart_item_id")
    void save(@Param("shoppingCartEntity") ShoppingCartEntity shoppingCartEntity);

    @Update("<script>" +
            "UPDATE shopping_cart SET delete_flg = 1 WHERE cart_item_id IN " +
            "<foreach item='cartItemId' index='index' collection='cartItemIds' open='(' separator=',' close=')'>#{cartItemId}</foreach>" +
            "</script>")
    int deleteItems(@Param("cartItemIds") List<Integer> cartItemIds);

}
