package com.app.dao;

import com.app.model.UserTaste;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.print.attribute.standard.MediaSize;
import java.util.List;

public interface UserTasteDao extends JpaRepository<UserTaste,Integer> {

    /**
     * 根据用户uid查询用户的口味偏好
     * @param uid
     * @return
     */
    @Query(nativeQuery = true,value = "select ut.* from user_taste as ut where ut.uid = ?1")
    public List<UserTaste> findByUid(Integer uid);

    @Query(nativeQuery = true,value = "select ut.* from user_taste as ut where ut.uid = ?1 and ut.tag_Id = ?2")
    public UserTaste findByUidAndTagId(Integer uid,Integer tagId);
}
