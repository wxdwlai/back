package com.app.model;

import com.app.model.primarykey.MessageId;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.bouncycastle.util.Times;
import org.springframework.data.util.Lazy;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@IdClass(MessageId.class)
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer uid;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer uuid;

    @Id
    private int count;

    @Id
    private Integer flag;

    private String message;
    private Timestamp date;
    private boolean isDelete = false;

    @ManyToOne
    @JoinColumn(name = "uuid",insertable = false,updatable = false)
    @JsonIgnoreProperties(value = {"recipes","viewLogs","userCollects","comments","commentReplies","userTastes","receiveMessages"})
    private UserInfo receiverInfo;

    @ManyToOne
    @JoinColumn(name = "uid",insertable = false,updatable = false)
    @JsonIgnoreProperties(value = {"recipes","viewLogs","userCollects","comments","commentReplies","userTastes","sendMessages"})
    private UserInfo senderInfo;

    public UserInfo getReceiverInfo() {
        return receiverInfo;
    }

    public void setReceiverInfo(UserInfo receiverInfo) {
        this.receiverInfo = receiverInfo;
    }

    public UserInfo getSenderInfo() {
        return senderInfo;
    }

    public void setSenderInfo(UserInfo senderInfo) {
        this.senderInfo = senderInfo;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Integer getUuid() {
        return uuid;
    }

    public void setUuid(Integer uuid) {
        this.uuid = uuid;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }
}
