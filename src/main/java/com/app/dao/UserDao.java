package com.app.dao;

import com.app.model.Recipe;
import com.app.model.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface UserDao extends JpaRepository<UserInfo, Integer> {
    UserInfo findByUid(Integer uid);

//    @Query(nativeQuery = true, value = "select u.* from user_info as u where u.uid =?1 and u.password = ?2")
    UserInfo findByUserName(String userName);

    UserInfo findByUserNameAndPassword(String userName, String password);

    /**
     * function：插入新用户
     * @param uid
     * @param name
     */
    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "insert into user_info(user_name,password) values(?1,?2)")
    void insertUser(String name, String password);

    UserInfo getUserInfoByToken(String token);

    /**
     * 修改用户的资料
     */
    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "update user_info set user_name=?2,sex=?3, intro=?4, image=?5 where uid = ?1")
    void updateInfo(Integer uid,String name,int sex,String intro,String image);

    /**
     * 图片未更改时
     */
    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "update user_info set user_name=?2,sex=?3, intro=?4 where uid = ?1")
    void updateInfoImage(Integer uid,String name,int sex,String intro);

    /**
     * 查找所用的用户信息
     */
    @Query(nativeQuery = true, value = "select u.* from user_info as u")
    List<UserInfo> findAllUser();

    /**
     * 用户注册
     */
//    @Modifying
//    @Transactional
//    @Query(nativeQuery = true,value = "insert into user_info(user_name,password) VALUES (?1,?2)")
//    void insertUser(String name,String password);
}
