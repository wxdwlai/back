package com.app.dao;

import com.app.model.RecipeTypes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface RecipeTypesDao extends JpaRepository<RecipeTypes,Integer> {
    List<RecipeTypes> getAllByTypeId(Integer typeId);

    @Modifying
    @Transactional
    @Query(nativeQuery = true,value = "insert into recipe_types(reid,type_id) values(?1,?2)")
    void insert(Integer reid,Integer typeId);

    @Query(nativeQuery = true,value = "select * from recipe_types where reid=?1 and type_id=?2")
    RecipeTypes findByReidAndTypeId(Integer reid,Integer typeId);
}
