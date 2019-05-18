package com.app.dao;

import com.app.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.List;

public interface MessageDao extends JpaRepository<Message,Integer>{

    @Modifying
    @Transactional
    @Query(nativeQuery = true,value = "insert into message (uid,uuid,message,date,flag) values(?1,?2,?3,?4,?1)")
    void insertSendMessage(Integer uid, Integer uuid, String message, Timestamp date);

    @Modifying
    @Transactional
    @Query(nativeQuery = true,value = "insert into message (uid,uuid,message,date,flag) values(?1,?2,?3,?4,?2)")
    void insertSendedMessage(Integer uid, Integer uuid, String message, Timestamp date);

    @Query(nativeQuery = true,value = "select * from message where flag=?1 and is_delete = 0 order by date desc")
    List<Message> findByUid(Integer uid);

    @Query(nativeQuery = true,value = "select * from message where flag=?1 and (uuid=?2 or uid=?2) and is_delete = 0")
    List<Message> findByUidAndUUid(Integer uid,Integer uuid);
}
