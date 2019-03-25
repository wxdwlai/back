package com.app.daoMapper;

import com.app.model.Recipe;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface RecipeMapper {
    @Select("SELECT r.reid,r.title,t.type_id,t.type_name FROM recipe r , recipe_types rT , types t WHERE r.reid = rT.reid and rT.type_id = t.type_id and r.reid = #{reid}")
    @Results({
            @Result(property = "reid", column = "reid"),
            @Result(property = "title", column = "title"),
            @Result(property = "typeId", column = "type_id"),
            @Result(property = "typeName", column = "type_name")
    })
    List<Object> getRecipeDetail(Integer reid);
    @Select("select * from recipe where reid = #{reid}")
    Recipe findByReid(@Param("reid") Integer reid);
}
