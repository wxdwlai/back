package com.app.utils;

import com.app.model.UserInfo;

/**
 * author： by dell
 * date：2019/2/26
 * function：token接口
 */
public interface TokenManaget {

    /**
     * 获取token
     * @param userInfo
     * @return
     */
    String getToken(UserInfo userInfo);

    /**
     * 用户退出登录
     * @param token
     */
    void userLogout(String token);

    /**
     * 根据token获取用户信息
     * @param token
     * @return
     */
    UserInfo getUserInfoByToken(String token);

    /**
     * 刷新用户
     * @param token
     */
    void refreshUser(String token);
}
