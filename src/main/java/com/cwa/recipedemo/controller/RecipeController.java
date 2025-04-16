package com.cwa.recipedemo.controller;

import com.cwa.recipedemo.model.Recipe;
import com.cwa.recipedemo.repository.RecipeRepository;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.util.Objects.nonNull;

@RestController
@RequestMapping("/api/recipes")
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeRepository recipeRepository;

    @GetMapping
    public ResponseEntity<Page<Recipe>> getRecipes(
            @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize
    ) {
        final var pageable = PageRequest.of(pageNumber, pageSize);

        return ResponseEntity.ok(recipeRepository.findAll(pageable));
    }

    @GetMapping("/filter")
    public ResponseEntity<List<Recipe>> getFilteredRecipes(
            @RequestParam(value = "cuisine", required = false) String cuisine,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "ingredients", required = false) String ingredients,
            @RequestParam(value = "maxCookingTime", required = false) Integer maxCookingTime
    ) {
        List<Recipe> recipes = recipeRepository.findAll();

        if (nonNull(cuisine) && !StringUtils.isEmpty(cuisine)) {
            recipes = recipes.stream()
                    .filter(r -> r.getCuisine().equalsIgnoreCase(cuisine))
                    .toList();
        }

        if (nonNull(title) && !StringUtils.isEmpty(title)) {
            recipes = recipes.stream()
                    .filter(r -> r.getTitle().toLowerCase().contains(title.toLowerCase()))
                    .toList();
        }

        if (nonNull(ingredients) && !StringUtils.isEmpty(ingredients)) {
            recipes = recipes.stream()
                    .filter(r -> r.getIngredients().toLowerCase().contains(ingredients.toLowerCase()))
                    .toList();
        }

        if (nonNull(maxCookingTime) && !maxCookingTime.equals(0)) {
            recipes = recipes.stream()
                    .filter(r -> r.getCookingTime() <= maxCookingTime)
                    .toList();
        }

        return ResponseEntity.ok(recipes);
    }
}
