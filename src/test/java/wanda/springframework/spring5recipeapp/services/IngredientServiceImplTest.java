package wanda.springframework.spring5recipeapp.services;

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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import wanda.springframework.spring5recipeapp.commands.IngredientCommand;
import wanda.springframework.spring5recipeapp.converters.IngredientToIngredientCommand;
import wanda.springframework.spring5recipeapp.converters.UnitOfMeasureToUnitOfMeasureCommand;
import wanda.springframework.spring5recipeapp.domain.Ingredient;
import wanda.springframework.spring5recipeapp.domain.Recipe;
import wanda.springframework.spring5recipeapp.repositories.RecipeRepository;

class IngredientServiceImplTest {

  @Mock
  private RecipeRepository repository;

  private IngredientToIngredientCommand command;

  private IngredientService service;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    command = new IngredientToIngredientCommand(new UnitOfMeasureToUnitOfMeasureCommand());
    service = new IngredientServiceImpl(repository, command);
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
    when(repository.findById(anyLong())).thenReturn(Optional.of(recipe));

    // then
    IngredientCommand ingredientCommand = service.findByRecipeIdAndIngredientId(1L, 3L);
    assertEquals(Long.valueOf(3L), ingredientCommand.getId());
    assertEquals(Long.valueOf(1L), ingredientCommand.getRecipeId());
    verify(repository, times(1)).findById(anyLong());

  }
}