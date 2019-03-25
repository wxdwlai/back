package com.app.dao;

import com.app.model.Steps;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StepsDao extends JpaRepository<Steps,Integer> {
    List<Steps> findByReid(Integer reid);
}
