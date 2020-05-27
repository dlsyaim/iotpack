package com.iotpack.api.service.impl;


import com.iotpack.api.dto.auth.LoginDto;
import com.iotpack.api.entity.group.GroupEntity;
import com.iotpack.api.entity.group.GroupRepository;
import com.iotpack.api.entity.user.TokenEntity;
import com.iotpack.api.entity.user.TokenRepository;
import com.iotpack.api.entity.user.UserEntity;
import com.iotpack.api.entity.user.UserRepository;
import com.iotpack.api.enums.BusinessExceptionEnum;
import com.iotpack.api.exception.BusinessException;
import com.iotpack.api.form.auth.LoginForm;
import com.iotpack.api.form.auth.LogoutForm;
import com.iotpack.api.service.UserService;
import com.iotpack.api.utils.PasswordUtils;
import com.iotpack.api.utils.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class UserServiceImpl implements UserService {


    @Autowired
    GroupRepository groupRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TokenRepository tokenRepository;

    @Override
    public LoginDto login(LoginForm loginForm) {

        LoginDto loginDto = new LoginDto();
        log.info("用户登录");

        GroupEntity g= groupRepository.findByName(loginForm.getGroup())
                .orElseThrow(()->new BusinessException("找不到"));
        loginDto.setGroup(g);
        UserEntity u = userRepository.findByAccountAndPasswordAndGroupId(loginForm.getAccount(), PasswordUtils.getMd5(loginForm.getPassword()), g.getId())
                .orElseThrow(()->new BusinessException("找不到"));;
        loginDto.setUser(u);

        TokenEntity tokenEntity=new TokenEntity();
        tokenEntity.setUserId(u.getId());
        tokenEntity.setToken(TokenUtils.getToken());
        tokenRepository.save(tokenEntity);

        loginDto.setToken(tokenEntity.getToken());

        return loginDto;
    }

    @Override
    public Object logout(LogoutForm logoutForm) {
        return null;
    }
}