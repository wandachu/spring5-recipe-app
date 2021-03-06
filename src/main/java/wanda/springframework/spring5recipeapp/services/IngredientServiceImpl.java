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
    } else { // add new Ingredient
      Ingredient ingredient = ingredientCommandToIngredient.convert(command);
      ingredient.setRecipe(recipe);
      recipe.addIngredient(ingredient);
    }

    Recipe savedRecipe = recipeRepository.save(recipe);

    Optional<Ingredient> savedIngredientOptional = savedRecipe.getIngredients().stream()
        .filter(recipeIngredients -> recipeIngredients.getId().equals(command.getId())).findFirst();

    // check by description
    if (!savedIngredientOptional.isPresent()) {
      savedIngredientOptional = savedRecipe.getIngredients().stream().filter(recipeIngredients -> recipeIngredients.getDescription().equals(command.getDescription()))
          .filter(recipeIngredients -> recipeIngredients.getAmount().equals(command.getAmount()))
          .filter(recipeIngredients -> recipeIngredients.getUnitOfMeasure().getId().equals(command.getUnitOfMeasure().getId()))
          .findFirst();
    }

    return ingredientToIngredientCommand
        .convert(savedIngredientOptional.get());
  }

  @Override
  public void deleteByIngredientId(Long recipeId, Long ingredientId) {
    log.debug("deleting ingredient: " + recipeId + ": " + ingredientId);
    Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);

    if (!recipeOptional.isPresent()) {
      log.debug("Cannot find recipe id: " + recipeId);
      return;
    }
    Recipe recipe = recipeOptional.get();
    log.debug("Found recipe");
    Optional<Ingredient> ingredientOptional = recipe.getIngredients()
        .stream()
        .filter(ingredient -> ingredient.getId().equals(ingredientId))
        .findFirst();

    if (ingredientOptional.isPresent()) {
      log.debug("Found ingredient");
      Ingredient ingredientToDelete = ingredientOptional.get();
      ingredientToDelete.setRecipe(null);
      recipe.getIngredients().remove(ingredientToDelete);
      recipeRepository.save(recipe);
    }
  }
}
