package wanda.springframework.spring5recipeapp.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import wanda.springframework.spring5recipeapp.converters.RecipeCommandToRecipe;
import wanda.springframework.spring5recipeapp.converters.RecipeToRecipeCommand;
import wanda.springframework.spring5recipeapp.domain.Recipe;
import wanda.springframework.spring5recipeapp.repositories.RecipeRepository;

class RecipeServiceImplTest {

  private RecipeServiceImpl recipeService;

  @Mock
  RecipeRepository recipeRepository;

  @Mock
  RecipeToRecipeCommand recipeToRecipeCommand;

  @Mock
  RecipeCommandToRecipe recipeCommandToRecipe;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    recipeService = new RecipeServiceImpl(recipeRepository, recipeCommandToRecipe, recipeToRecipeCommand);
  }

  @Test
  void getRecipeByIdTest() {
    Recipe recipe = new Recipe();
    recipe.setId(1L);
    Optional<Recipe> recipeOptional = Optional.of(recipe);

    when(recipeRepository.findById(anyLong())).thenReturn(recipeOptional);
    Recipe recipeReturned = recipeService.findById(1L);

    assertNotNull(recipeReturned);
    assertEquals(1L, recipeReturned.getId());
    verify(recipeRepository, times(1)).findById(anyLong());
    verify(recipeRepository, never()).findAll();
  }

  @Test
  void getRecipes() {
    Recipe recipe = new Recipe();
    HashSet<Recipe> recipeData = new HashSet<>();
    recipeData.add(recipe);

    when(recipeRepository.findAll()).thenReturn(recipeData); // mock the repository
    assertEquals(1, recipeService.getRecipes().size());
    verify(recipeRepository, times(1)).findAll(); // only run once
  }
}