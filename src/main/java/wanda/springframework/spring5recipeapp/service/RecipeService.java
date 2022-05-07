package wanda.springframework.spring5recipeapp.service;

import java.util.Set;
import wanda.springframework.spring5recipeapp.domain.Recipe;

public interface RecipeService {
  Set<Recipe> getRecipes();
}
