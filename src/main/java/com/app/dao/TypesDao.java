package com.app.dao;

import com.app.model.Types;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TypesDao extends JpaRepository<Types,Integer> {
}
