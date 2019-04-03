package com.app.dao;

import com.app.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.List;

public interface CommentDao extends JpaRepository<Comment, Integer> {

    @Query(nativeQuery = true,value = "select c.* from comment as c where c.reid = ?1 and c.puid!=?2 and c.is_delete=0")
    List<Comment> getPostMessageByReid(Integer reid,Integer uid);

    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "insert into comment(reid,puid,context,create_time,type) values(?1,?2,?3,?4,?5)")
    void insertComment(Integer reid, Integer uid, String context, Timestamp createTime,int type);

    //一级评论点赞
//    @Transactional
//    @Modifying
//    @Query(nativeQuery = true,value = "update comment set ups = ups + 1 where mid = ?1")
//    void addUps(Integer mid);
//
//    @Transactional
//    @Modifying
//    @Query(nativeQuery = true,value = "update comment set ups = up -1 where mid = ?1")
//    void deleteUps(Integer mid);
}
