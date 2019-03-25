package com.app.dao;

import com.app.model.Recipe;
import com.app.model.Types;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Map;

public interface RecipeDao extends JpaRepository<Recipe,Integer> {
    Recipe findByReid(Integer reid);
    @Query(nativeQuery = true,value = "SELECT t.* FROM recipe r , recipe_tags rt , tags t WHERE r.reid = rt.reid and rt.tag_id = t.tag_id and r.reid = ?1")
    List<Map<String,Object>> findTagsByReid(Integer reid);

    @Query(nativeQuery = true, value = "select r.* from recipe as r, user_collects as uc where r.reid = uc.reid and uc.uid = ?1 and uc.type = 0")
    List<Recipe> findCollectByUid(Integer uid);

    @Query(nativeQuery = true, value = "select r.* from recipe as r, user_info as u,user_collects as uc where r.reid = uc.reid and uc.uid = u.uid and uc.type = 0 and u.uid = ?1 and (r.title like %?2% or r.ings like %?2%)")
    List<Recipe> searchCollect(Integer uid, String keyword);

    @Query(nativeQuery = true, value = "select r.* from recipe as r where r.title like %?1% or r.ings like %?1%")
    List<Recipe> findByKeyword(String keyword);

    @Query(nativeQuery = true,value = "select re.* from recommend as r , recipe as re, user_info as u where r.reid=re.reid and r.uid=u.uid and r.uid=?1")
    List<Recipe> findRecipeByUid(Integer uid);

    @Query(nativeQuery = true, value = "select r.* from recipe as r")
    List<Recipe> findAllRecipe();

}
