package io.recruitment.assessment.api.repository;

import io.recruitment.assessment.api.entity.ProductEntity;
import io.recruitment.assessment.api.utils.PageQuery;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface ProductRepository {

    @Select("SELECT * FROM product WHERE product_id = #{productId} AND delete_flg = 0")
    ProductEntity findById(int experimentId);

    @Select("<script>" +
            "SELECT * FROM product WHERE product_id IN " +
            "<foreach item='productId' index='index' collection='productIds' open='(' separator=',' close=')'>#{productId}</foreach>" +
            "</script>")
    List<ProductEntity> findProductList(@Param("productIds") List<Integer> productIds);

    @Select("SELECT * FROM product WHERE delete_flg = 0 AND name like CONCAT('%',#{pageQuery.keyword},'%') " +
            "limit #{pageQuery.start}, #{pageQuery.limit}")
    List<ProductEntity> findProductsListBySearch(@Param("pageQuery") PageQuery pageQuery);

    @Select("SELECT COUNT(*) FROM product WHERE delete_flg = 0 AND name like CONCAT('%',#{keyword},'%')")
    int findTotalProductsBySearch(String keyword);

    @Insert("INSERT INTO `product` (`name`, `desc`, `img_url`, `price`, `stock_number`, `tag`, `create_user`, `update_user`) " +
            "VALUES ( #{productEntity.name}, #{productEntity.desc}, #{productEntity.imgUrl}, #{productEntity.price}, " +
            "#{productEntity.stockNumber}, #{productEntity.tag}, #{userId}, #{userId} )")
    @Options(useGeneratedKeys = true, keyProperty = "productEntity.productId", keyColumn = "product_id")
    int save(@Param("productEntity") ProductEntity productEntity, @Param("userId") Integer userId);

    @Update("UPDATE `product` SET " +
            "`name` = #{productEntity.name}, " +
            "`desc` = #{productEntity.desc}, " +
            "`img_url` = #{productEntity.imgUrl}, " +
            "`price` = #{productEntity.price}, " +
            "`stock_number` = #{productEntity.stockNumber}, " +
            "`tag` = #{productEntity.tag}, " +
            "`update_user` = #{userId}, " +
            "`version` = #{productEntity.version} " +
            "WHERE `product_id` = #{productEntity.productId}")
    void update(@Param("productEntity") ProductEntity productEntity, @Param("userId") Integer userId);

    @Update("UPDATE product SET stock_number = #{stockNumber} WHERE product_id = #{productId}")
    int updateStockNumber(@Param("stockNumber") Integer stockNumber, @Param("productId") Integer productId);

    @Update("<script>" +
            "UPDATE product SET delete_flg = 1 WHERE product_id IN " +
            "<foreach item='product' index='index' collection='productIds' open='(' separator=',' close=')'>#{product}</foreach>" +
            "</script>")
    void deleteProducts(@Param("productIds") Integer[] productIds);

}
