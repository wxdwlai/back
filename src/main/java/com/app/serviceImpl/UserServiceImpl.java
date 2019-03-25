package com.app.serviceImpl;

import com.app.daoMapper.UserMapper;
import com.app.model.UserInfo;
import com.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public int save(UserInfo user) {
        return userMapper.insert(user);
    }

    @Override
    public int update(UserInfo user) {
        return userMapper.updateByPrimaryKey(user);
    }

    @Override
    public int remove(UserInfo user) {
        return userMapper.deleteByPrimaryKey(user.getUid());
    }

    @Override
    public UserInfo getUserById(Integer userId) {
        return userMapper.findByUid(userId);
    }
}
