package com.app.dao;

import com.app.model.ViewLogs;
import org.bouncycastle.util.Times;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.List;

public interface ViewLogsDao extends JpaRepository<ViewLogs,Integer> {
    List<ViewLogs> findAllByReid(Integer reid);
    List<ViewLogs> findAllByUid(Integer uid);

    /**
     * 根据uid和reid查找是否有浏览记录
     * @param uid
     * @param reid
     * @return
     */
    @Query(nativeQuery = true,value = "select vl.* from view_logs as vl where vl.uid=?1 and vl.reid=?2 and vl.prefer_degree=?3")
    ViewLogs findByUidAndReidAndPreferDegree(Integer uid, Integer reid,int type);

    /**
     * 添加新的浏览记录
     * @param uid
     * @param reid
     * @param time
     * @param degree
     */
    @Modifying
    @Transactional
    @Query(nativeQuery = true,value = "insert into view_logs(uid,reid,view_time,prefer_degree) values (?1,?2,?3,?4)")
    void addLogs(Integer uid, Integer reid, Timestamp time,int degree);

    /**
     * 删除点赞浏览记录
     */
    @Modifying
    @Transactional
    @Query(nativeQuery = true,value = "delete from view_logs where uid=?1 and reid=?2 and prefer_degree = ?3")
    void deleteLogs(Integer uid,Integer reid,int n);
    /**
     * 更新浏览时间
     */
    @Modifying
    @Transactional
    @Query(nativeQuery = true,value = "update view_logs set view_time=?2,visited_times=visited_times+1 where vid=?1")
    void updateTime(Integer vid, Timestamp newTime);

    @Query(nativeQuery = true, value = "select vl.* from view_logs as vl where vl.uid=?1 and vl.prefer_degree=1 order by view_time desc")
    List<ViewLogs> findByUid(Integer uid);

}
