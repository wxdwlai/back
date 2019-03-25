package com.app.serviceImpl;

import com.app.daoMapper.RecipeMapper;
import com.app.service.RecipeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class RecipeServiceImpl implements RecipeService {
    @Resource
    RecipeMapper recipeMapper;
    @Override
    public List<?> get(Integer reid) {
        return recipeMapper.getRecipeDetail(reid);
    }
}
