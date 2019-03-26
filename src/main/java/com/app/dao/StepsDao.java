package com.app.dao;

import com.app.model.Steps;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface StepsDao extends JpaRepository<Steps,Integer> {
    List<Steps> findByReid(Integer reid);

    @Modifying
    @Transactional
    @Query(nativeQuery = true,value = "insert into steps(reid,step_id,steps,step_imgs) values (?1,?2,?3,?4)")
    void inserStep(Integer reid,Integer stepId,String step,String image);
}
