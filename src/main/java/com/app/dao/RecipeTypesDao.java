package com.app.dao;

import com.app.model.RecipeTypes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecipeTypesDao extends JpaRepository<RecipeTypes,Integer> {
    List<RecipeTypes> getAllByTypeId(Integer typeId);
}
