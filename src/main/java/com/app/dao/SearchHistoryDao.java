package com.app.dao;

import com.app.model.SearchHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.jws.Oneway;
import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public interface SearchHistoryDao extends JpaRepository<SearchHistory,Integer> {

    /**
     * 查询用户所有搜索记录
     * @param uid
     * @return keywords
     */
    @Query(nativeQuery = true, value = "select distinct sh.keyword from search_history as sh where sh.uid = ?1 order by time desc limit 10")
    List<String> findAllKeyWord(Integer uid);

    List<SearchHistory> findTop10ByUid(Integer uid);

    @Query(nativeQuery = true,value = "select distinct sh.* from search_history as sh where sh.uid = ?1 order by time desc limit 10")
    List<SearchHistory> getKeyword(Integer uid);

//    @Query(nativeQuery = true,value = "select * from search_history as sh where sh.uid = ?1 and sh.keyword = ?2")
    SearchHistory findByUidAndKeyword(Integer uid, String keyword);

    /**
     * 更新关键词时间
     * @param uid
     * @param keyword
     * @param time
     */
    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "update search_history set time = ?3 where uid = ?1 and keyword = ?2")
    void updateTime(Integer uid, String keyword, Timestamp time);
    /**
     * 插入新的搜索记录
     * @param uid
     * @param keyword
     * @param time
     */
    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "insert into search_history(uid,keyword,time) VALUES (?1,?2,?3)")
    void insertKeyword(Integer uid, String keyword, Timestamp time);

    /**
     * 删除用户搜索记录
     */
    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "delete from search_history where uid=?1")
    void deleteByUid(Integer uid);
}
