package wanda.springframework.spring5recipeapp.services;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import wanda.springframework.spring5recipeapp.commands.IngredientCommand;
import wanda.springframework.spring5recipeapp.converters.IngredientCommandToIngredient;
import wanda.springframework.spring5recipeapp.converters.IngredientToIngredientCommand;
import wanda.springframework.spring5recipeapp.converters.UnitOfMeasureCommandToUnitOfMeasure;
import wanda.springframework.spring5recipeapp.converters.UnitOfMeasureToUnitOfMeasureCommand;
import wanda.springframework.spring5recipeapp.domain.Ingredient;
import wanda.springframework.spring5recipeapp.domain.Recipe;
import wanda.springframework.spring5recipeapp.repositories.RecipeRepository;
import wanda.springframework.spring5recipeapp.repositories.UnitOfMeasureRepository;

class IngredientServiceImplTest {

  @Mock
  private RecipeRepository recipeRepository;

  @Mock
  private UnitOfMeasureRepository unitOfMeasureRepository;

  private IngredientToIngredientCommand ingredientToIngredientCommand;
  private IngredientCommandToIngredient ingredientCommandToIngredient;

  private IngredientService service;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    ingredientCommandToIngredient = new IngredientCommandToIngredient(new UnitOfMeasureCommandToUnitOfMeasure());
    ingredientToIngredientCommand = new IngredientToIngredientCommand(new UnitOfMeasureToUnitOfMeasureCommand());
    service = new IngredientServiceImpl(recipeRepository, ingredientToIngredientCommand, ingredientCommandToIngredient,
        unitOfMeasureRepository);
  }

  @Test
  void findByRecipeIdAndIngredientIdHappyPath() {
    // given
    Recipe recipe = new Recipe();
    recipe.setId(1L);

    Ingredient ingredient1 = new Ingredient();
    ingredient1.setId(1L);

    Ingredient ingredient2 = new Ingredient();
    ingredient2.setId(2L);

    Ingredient ingredient3 = new Ingredient();
    ingredient3.setId(3L);

    recipe.addIngredient(ingredient1);
    recipe.addIngredient(ingredient2);
    recipe.addIngredient(ingredient3);

    // when
    when(recipeRepository.findById(anyLong())).thenReturn(Optional.of(recipe));

    // then
    IngredientCommand ingredientCommand = service.findByRecipeIdAndIngredientId(1L, 3L);
    assertEquals(Long.valueOf(3L), ingredientCommand.getId());
    assertEquals(Long.valueOf(1L), ingredientCommand.getRecipeId());
    verify(recipeRepository, times(1)).findById(anyLong());
  }

  @Test
  void testSaveRecipeCommand() {
    // given
    IngredientCommand  command = new IngredientCommand();
    command.setId(3L);
    command.setRecipeId(2L);

    Optional<Recipe> recipeOptional = Optional.of(new Recipe());

    Recipe savedRecipe = new Recipe();
    savedRecipe.addIngredient(new Ingredient());
    savedRecipe.getIngredients().iterator().next().setId(3L);

    when(recipeRepository.findById(anyLong())).thenReturn(recipeOptional);
    when(recipeRepository.save(any())).thenReturn(savedRecipe);

    //when
    IngredientCommand savedCommand = service.saveIngredientCommand(command);

    //then
    assertEquals(Long.valueOf(3L), savedCommand.getId());
    verify(recipeRepository, times(1)).findById(anyLong());
    verify(recipeRepository, times(1)).save(any(Recipe.class));
  }
}