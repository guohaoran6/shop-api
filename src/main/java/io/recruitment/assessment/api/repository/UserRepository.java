package io.recruitment.assessment.api.repository;

import io.recruitment.assessment.api.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface UserRepository {

    @Select("SELECT * FROM user WHERE user_id = #{userId} and delete_flg = 0")
    UserEntity findById(Integer userId);

    @Select("SELECT * FROM user WHERE user_name = #{userName} and password_md5 = #{passwordMd5} and delete_flg = 0")
    UserEntity findByUserNameAndPassword(String userName, String passwordMd5);


}
