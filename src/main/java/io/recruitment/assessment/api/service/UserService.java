package io.recruitment.assessment.api.service;

import io.recruitment.assessment.api.entity.UserEntity;
import io.recruitment.assessment.api.entity.UserTokenEntity;
import io.recruitment.assessment.api.exception.UnauthorizedErrorException;
import io.recruitment.assessment.api.repository.UserRepository;
import io.recruitment.assessment.api.repository.UserTokenRepository;
import io.recruitment.assessment.api.utils.NumberUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserTokenRepository userTokenRepository;

    private static ModelMapper modelMapper = new ModelMapper();

    /**
     * @Description Login func
     * @param loginName
     * @param passwordMD5
     * @return
     */
    public String login(String loginName, String passwordMD5) {
        UserEntity userEntity = userRepository.findByUserNameAndPassword(loginName, passwordMD5);
        if (userEntity != null) {
            // Update token after login
            String token = NumberUtil.getNewToken(System.currentTimeMillis() + "", userEntity.getUserId());
            UserTokenEntity userTokenEntity = userTokenRepository.findTokenByUserId(userEntity.getUserId());
            // Current date time
            Date now = new Date();
            // Expire date time
            Date expireTime = new Date(now.getTime() + 2 * 24 * 3600 * 1000); //expire time is 48 hours
            if (userTokenEntity == null) {
                userTokenEntity = new UserTokenEntity();
                userTokenEntity.setUserId(userEntity.getUserId());
                userTokenEntity.setToken(token);
                userTokenEntity.setExpireTime(expireTime);
                userTokenRepository.saveUserToken(userTokenEntity);
                return token;
            } else {
                userTokenEntity.setToken(token);
                userTokenEntity.setExpireTime(expireTime);
                userTokenRepository.updateUserToken(userTokenEntity);
                return token;
            }
        } else {
            throw new UnauthorizedErrorException("Cannot find user.");
        }
    }

    /**
     * @Description Logout func
     * @param userId
     */
    public void logout(Integer userId) {
        userTokenRepository.deleteUserToken(userId);
    }

}
