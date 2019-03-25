package com.app.dao;

import com.app.model.Recipe;
import com.app.model.UserCollects;
import org.apache.ibatis.annotations.Delete;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface UserCollectsDao extends JpaRepository<UserCollects, Integer> {
    UserCollects findByReidAndUidAndType(Integer reid, Integer uid, Boolean type);
    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "delete from user_collects where reid = ?1 and uid = ?2 and type = 0")
    void deleteUserCollectsByReidAndUid(Integer reid, Integer uid);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "delete from user_collects where reid = ?1 and uid = ?2 and type = 1")
    void deleteUserLikesByReidAndUid(Integer reid, Integer uid);

    //根据reid查找收藏菜谱数量
    @Query(nativeQuery = true, value = "select uc.* from user_collects as uc where uc.reid = ?1 and uc.type = 0 ")
    List<UserCollects> findUserCollectsByReid(Integer reid);

    //根据reid查找点赞
    @Query(nativeQuery = true, value = "select uc.* from user_collects as uc where uc.reid = ?1 and uc.type = 1 ")
    List<UserCollects> findUserLikesByReid(Integer reid);

}
