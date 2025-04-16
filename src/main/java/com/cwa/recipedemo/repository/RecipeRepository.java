package com.cwa.recipedemo.repository;

import com.cwa.recipedemo.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    List<Recipe> findByCuisineContainingIgnoreCase(String cuisine);
    List<Recipe> findByIngredientsContainingIgnoreCase(String ingredient);
    List<Recipe> findByTitleContainingIgnoreCase(String title);
}
