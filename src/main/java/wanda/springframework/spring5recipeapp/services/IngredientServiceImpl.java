package wanda.springframework.spring5recipeapp.services;

import java.util.Optional;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import wanda.springframework.spring5recipeapp.commands.IngredientCommand;
import wanda.springframework.spring5recipeapp.converters.IngredientCommandToIngredient;
import wanda.springframework.spring5recipeapp.converters.IngredientToIngredientCommand;
import wanda.springframework.spring5recipeapp.domain.Ingredient;
import wanda.springframework.spring5recipeapp.domain.Recipe;
import wanda.springframework.spring5recipeapp.repositories.RecipeRepository;
import wanda.springframework.spring5recipeapp.repositories.UnitOfMeasureRepository;

@Slf4j
@Service
public class IngredientServiceImpl implements
    IngredientService {

  private final RecipeRepository recipeRepository;
  private final IngredientToIngredientCommand ingredientToIngredientCommand;
  private final IngredientCommandToIngredient ingredientCommandToIngredient;
  private final UnitOfMeasureRepository unitOfMeasureRepository;

  public IngredientServiceImpl(RecipeRepository recipeRepository,
      IngredientToIngredientCommand ingredientToIngredientCommand,
      IngredientCommandToIngredient ingredientCommandToIngredient,
      UnitOfMeasureRepository unitOfMeasureRepository) {
    this.recipeRepository = recipeRepository;
    this.ingredientToIngredientCommand = ingredientToIngredientCommand;
    this.ingredientCommandToIngredient = ingredientCommandToIngredient;
    this.unitOfMeasureRepository = unitOfMeasureRepository;
  }

  @Override
  public IngredientCommand findByRecipeIdAndIngredientId(Long recipeId, Long ingredientId) {
    Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(() -> new RuntimeException("Recipe id not found. Id: " + recipeId));
    return recipe.getIngredients().stream()
        .filter(ingredient -> ingredient.getId().equals(ingredientId))
        .map(ingredient -> ingredientToIngredientCommand.convert(ingredient))
        .findFirst().orElseThrow(() -> new RuntimeException("Ingredient id not found: " + ingredientId));
  }

  @Override
  @Transactional
  public IngredientCommand saveIngredientCommand(IngredientCommand command) {
    Optional<Recipe> recipeOptional = recipeRepository.findById(command.getRecipeId());

    if (!recipeOptional.isPresent()) {
      log.error("Recipe not found for id: " + command.getRecipeId());
      return new IngredientCommand();
    }
    Recipe recipe = recipeOptional.get();
    Optional<Ingredient> ingredientOptional = recipe.getIngredients().stream()
        .filter(ingredient -> ingredient.getId().equals(command.getId())).findFirst();
    if (ingredientOptional.isPresent()) { // update this ingredient found
      Ingredient ingredientFound = ingredientOptional.get();
      ingredientFound.setDescription(command.getDescription());
      ingredientFound.setAmount(command.getAmount());
      ingredientFound.setUnitOfMeasure(unitOfMeasureRepository.findById(command.getUnitOfMeasure().getId()).orElseThrow(() -> new RuntimeException("UOM NOT FOUND")));
    } else {
      recipe.addIngredient(ingredientCommandToIngredient.convert(command));
    }

    Recipe savedRecipe = recipeRepository.save(recipe);

    return ingredientToIngredientCommand
        .convert(savedRecipe.getIngredients().stream()
            .filter((recipeIngredients -> recipeIngredients.getId().equals(command.getId())))
            .findFirst().get());
  }
}
