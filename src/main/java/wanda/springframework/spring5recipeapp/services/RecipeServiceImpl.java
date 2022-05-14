package wanda.springframework.spring5recipeapp.services;

import java.util.HashSet;
import java.util.Set;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import wanda.springframework.spring5recipeapp.commands.RecipeCommand;
import wanda.springframework.spring5recipeapp.converters.RecipeCommandToRecipe;
import wanda.springframework.spring5recipeapp.converters.RecipeToRecipeCommand;
import wanda.springframework.spring5recipeapp.domain.Recipe;
import wanda.springframework.spring5recipeapp.repositories.RecipeRepository;

@Slf4j
@Service
public class RecipeServiceImpl implements
    RecipeService {

  private final RecipeRepository recipeRepository;
  private final RecipeCommandToRecipe recipeCommandToRecipe;
  private final RecipeToRecipeCommand recipeToRecipeCommand;

  public RecipeServiceImpl(RecipeRepository recipeRepository,
      RecipeCommandToRecipe recipeCommandToRecipe, RecipeToRecipeCommand recipeToRecipeCommand) {
    this.recipeRepository = recipeRepository;
    this.recipeCommandToRecipe = recipeCommandToRecipe;
    this.recipeToRecipeCommand = recipeToRecipeCommand;
  }

  @Override
  public Recipe findById(Long id) {
    return recipeRepository.findById(id).orElseThrow(() -> new RuntimeException("Recipe Not Found!"));
  }

  @Override
  public Set<Recipe> getRecipes() {
    log.debug("I'm in the service");
    Set<Recipe> recipes = new HashSet<>();
    recipeRepository.findAll().iterator().forEachRemaining(recipes::add);
    return recipes;
  }

  @Override
  public RecipeCommand findCommandById(Long id) {
    return recipeToRecipeCommand.convert(findById(id));
  }

  @Override
  public void deleteById(Long id) {
    recipeRepository.deleteById(id);
  }

  @Override
  @Transactional
  public RecipeCommand saveRecipeCommand(RecipeCommand command) {
    Recipe detachedRecipe = recipeCommandToRecipe.convert(command);

    Recipe savedRecipe = recipeRepository.save(detachedRecipe);
    log.debug("Saved RecipeId:" + savedRecipe.getId());
    return recipeToRecipeCommand.convert(savedRecipe);
  }
}
