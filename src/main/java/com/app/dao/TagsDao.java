package com.app.dao;

import com.app.model.Tags;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TagsDao extends JpaRepository<Tags, Integer> {

}
