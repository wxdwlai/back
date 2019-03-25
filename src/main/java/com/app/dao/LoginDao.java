package com.app.dao;

import com.app.model.UserPassword;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginDao extends JpaRepository<UserPassword, Integer> {
    public UserPassword findByUserName(String userName);
}
