package io.recruitment.assessment.api.repository;

import io.recruitment.assessment.api.entity.UserTokenEntity;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface UserTokenRepository {
    @Select("SELECT * FROM user_token WHERE token = #{token}")
    Integer findUserIdByToken(String token);

    @Select("SELECT token FROM user_token WHERE user_id = #{userId}")
    UserTokenEntity findTokenByUserId(Integer userId);


    @Insert("INSERT INTO user_token (user_id, token, expire_time)" +
            "VALUES ( #{userTokenEntity.userId}, #{userTokenEntity.token}, #{userTokenEntity.expireTime})")
    @Options(useGeneratedKeys = true, keyProperty = "userTokenEntity.userId", keyColumn = "user_id")
    void saveUserToken(@Param("userTokenEntity") UserTokenEntity userTokenEntity);

    @Update("UPDATE user_token SET " +
            "token = #{userTokenEntity.token}, expire_time = #{userTokenEntity.expireTime} " +
            "WHERE user_id = #{userTokenEntity.userId}")
    void updateUserToken(@Param("userTokenEntity") UserTokenEntity userTokenEntity);

    @Delete("DELETE FROM user_token WHERE user_id = #{userId}")
    void deleteUserToken(Integer userId);

}
