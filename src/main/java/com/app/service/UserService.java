package com.app.service;

import com.app.model.UserInfo;

public interface UserService {
    public int save(UserInfo user);

    public int update(UserInfo user);

    public int remove(UserInfo user);

    public UserInfo getUserById(Integer userId);
}
