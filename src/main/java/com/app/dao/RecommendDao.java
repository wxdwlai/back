package com.app.dao;

import com.app.model.Recipe;
import com.app.model.Recommend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.List;

public interface RecommendDao extends JpaRepository<Recommend,Integer>{
    List<Recommend> findByUid(Integer uid);

    @Query(nativeQuery = true,value = "select re.* from recommend as re where re.reid=?1 and re.uid=?2")
    Recommend findByUidAndReid(Integer reid, Integer uid);

    //删除一条推荐记录
    @Modifying
    @Transactional
    @Query(nativeQuery = true,value = "delete from recommend where uid=?1 order by derive_time limit 1")
    void deleteByReidAndUid(Integer uid);

    //插入一条推荐记录
    @Modifying
    @Transactional
    @Query(nativeQuery = true,value = "insert into recommend(reid,uid,derive_time) values(?1,?2,?3)")
    void insertByReidAndUid(Integer reid, Integer uid, Timestamp timestamp);

    //更新时间
    @Modifying
    @Transactional
    @Query(nativeQuery = true,value = "update recommend set  derive_time=?3  where reid=?1 and uid=?2")
    void updateTime(Integer reid, Integer uid, Timestamp timestamp);
}
