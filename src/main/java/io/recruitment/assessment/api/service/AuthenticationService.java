package io.recruitment.assessment.api.service;

import io.recruitment.assessment.api.dto.User;
import io.recruitment.assessment.api.entity.UserEntity;
import io.recruitment.assessment.api.exception.UnauthorizedErrorException;
import io.recruitment.assessment.api.repository.UserRepository;
import io.recruitment.assessment.api.repository.UserTokenRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    @Autowired
    UserTokenRepository userTokenRepository;
    @Autowired
    UserRepository userRepository;

    private static ModelMapper modelMapper = new ModelMapper();


    /**
     * @Description Authenticate User by Token
     * @param token
     * @return
     * @throws UnauthorizedErrorException
     */
    public User authenticateUser(String token) throws UnauthorizedErrorException {
        if (token.isEmpty()) {
            throw new UnauthorizedErrorException("User token missing.");
        }
        Integer userId = userTokenRepository.findUserIdByToken(token);
        UserEntity userEntity = userRepository.findById(userId);
        return modelMapper.map(userEntity, User.class);

    }
}
