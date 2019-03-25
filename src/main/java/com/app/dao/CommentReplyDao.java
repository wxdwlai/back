package com.app.dao;

import com.app.model.CommentReply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.List;

public interface CommentReplyDao extends JpaRepository<CommentReply,Integer> {
    @Query(nativeQuery = true,value = "select cr.* from comment_reply as cr where cr.mmid = ?1")
    List<CommentReply> getReMessageByReid(Integer mmid);

    /**
     * 判断comment的puid和comment_reply表的ruid是否一致，同时mmid的值也必须是正确的，否则的话则添加评论失败
     * @param mmid
     * @return
     */
    @Query(nativeQuery = true,value = "select c.puid from comment_reply as cr, comment as c where cr.mmid = c.mid and cr.ruid = c.puid and cr.reid = c.reid and cr.mmid = ?1")
    int findRuidByMmid(int mmid);

    /**
     * 用户判断二级评论和一级评论的的reid是否一致，一致才能加入二级评论
     * @param mmid
     * @return
     */
    @Query(nativeQuery = true,value = "select c.reid from comment_reply as cr, comment as c where cr.mmid = c.mid and cr.mmid = ?1 and cr.ruid = c.puid and cr.reid = c.reid")
    int findReidByMmid(int mmid);

    /**
     * 添加二级评论
     * @param mmid
     * @param reid
     * @param puid
     * @param ruid
     * @param context
     * @param createTime
     * @param type
     */
    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "insert into comment_reply(mmid,reid,puid,ruid,context,create_time,type) values(?1,?2,?3,?4,?5,?6,?7)")
    void insertComment(Integer mmid,Integer reid,Integer puid, Integer ruid, String context, Timestamp createTime, int type);
    /**
     * 查找评论是否存在
     */
    CommentReply findByRuidAndMid(Integer ruid,Integer mid);
    /**
     * 查找用户收到的评论消息回复
     * @param ruid
     * @return
     */
    @Query(nativeQuery = true, value = "select cr.* from comment_reply as cr where cr.ruid = ?1 and cr.is_show=1")
    List<CommentReply> findByRuid(Integer ruid);

    /**
     * 删除评论
     */
    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "update comment_reply set is_show=0 where mid=?1")
    void deleteMessage(Integer mid);
}
