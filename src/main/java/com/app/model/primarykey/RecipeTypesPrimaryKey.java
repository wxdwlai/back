package com.app.model.primarykey;

import java.io.Serializable;

/**
 * recipeTypes表的主键class
 * 注意：需要implements Serializable 否则会报错
 */
public class RecipeTypesPrimaryKey implements Serializable{
    private Integer reid;
    private Integer typeId;
}
