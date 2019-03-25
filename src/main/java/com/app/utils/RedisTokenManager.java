package com.app.utils;

import com.app.model.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RedisTokenManager implements TokenManaget {

//    @Autowired
    private RedisUtils redisUtils;
    @Override
    public String getToken(UserInfo userInfo) {
        return null;
    }

    @Override
    public void userLogout(String token) {

    }

    @Override
    public UserInfo getUserInfoByToken(String token) {
        return null;
    }

    @Override
    public void refreshUser(String token) {

    }
}
