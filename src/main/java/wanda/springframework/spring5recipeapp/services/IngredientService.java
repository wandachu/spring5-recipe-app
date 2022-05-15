package wanda.springframework.spring5recipeapp.services;

import wanda.springframework.spring5recipeapp.commands.IngredientCommand;

public interface IngredientService {
  IngredientCommand findByRecipeIdAndIngredientId(Long recipeId, Long ingredientId);
  IngredientCommand saveIngredientCommand(IngredientCommand command);
}
