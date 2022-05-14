package wanda.springframework.spring5recipeapp.services;

import java.util.Set;
import wanda.springframework.spring5recipeapp.commands.RecipeCommand;
import wanda.springframework.spring5recipeapp.domain.Recipe;

public interface RecipeService {
  Set<Recipe> getRecipes();
  Recipe findById(Long id);
  RecipeCommand findCommandById(Long id);
  RecipeCommand saveRecipeCommand(RecipeCommand command);
  void deleteById(Long id);
}
