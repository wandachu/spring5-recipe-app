package wanda.springframework.spring5recipeapp.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.HashSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import wanda.springframework.spring5recipeapp.commands.IngredientCommand;
import wanda.springframework.spring5recipeapp.commands.RecipeCommand;
import wanda.springframework.spring5recipeapp.services.IngredientService;
import wanda.springframework.spring5recipeapp.services.RecipeService;
import wanda.springframework.spring5recipeapp.services.UnitOfMeasureService;

class IngredientControllerTest {

  @Mock
  RecipeService recipeService;

  @Mock
  IngredientService ingredientService;

  @Mock
  UnitOfMeasureService unitOfMeasureService;

  IngredientController controller;

  MockMvc mockMvc;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    controller = new IngredientController(recipeService, ingredientService, unitOfMeasureService);
    mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
  }

  @Test
  void testListIngredients() throws Exception {
    // Given
    RecipeCommand recipeCommand = new RecipeCommand();
    when(recipeService.findCommandById(anyLong())).thenReturn(recipeCommand);

    // When
    mockMvc.perform(get("/recipe/1/ingredients"))
        .andExpect(status().isOk())
        .andExpect(view().name("recipe/ingredient/list"))
        .andExpect(model().attributeExists("recipe"));

    // Then
    verify(recipeService, times(1)).findCommandById(anyLong());
  }

  @Test
  void testShowIngredient() throws Exception {
    // Given
    IngredientCommand ingredientCommand = new IngredientCommand();

    // When
    when(ingredientService.findByRecipeIdAndIngredientId(anyLong(), anyLong())).thenReturn(ingredientCommand);

    // Then
    mockMvc.perform(get("/recipe/1/ingredient/2/show"))
        .andExpect(status().isOk()).andExpect(view().name("recipe/ingredient/show"))
        .andExpect(model().attributeExists("ingredient"));
  }

  @Test
  void testNewIngredientForm() throws Exception {
    // given
    RecipeCommand recipeCommand = new RecipeCommand();
    recipeCommand.setId(1L);

    // when
    when(recipeService.findCommandById(anyLong())).thenReturn(recipeCommand);
    when(unitOfMeasureService.listAllUoms()).thenReturn(new HashSet<>());

    // then
    mockMvc.perform(get("/recipe/1/ingredient/new"))
        .andExpect(status().isOk())
        .andExpect(view().name("recipe/ingredient/ingredientform"))
        .andExpect(model().attributeExists("ingredient"))
        .andExpect(model().attributeExists("uomList"));
    verify(recipeService, times(1)).findCommandById(anyLong());
  }

  @Test
  void testUpdateIngredientForm() throws Exception {
    // given
    IngredientCommand ingredientCommand = new IngredientCommand();

    // when
    when(ingredientService.findByRecipeIdAndIngredientId(anyLong(), anyLong())).thenReturn(ingredientCommand);
    when(unitOfMeasureService.listAllUoms()).thenReturn(new HashSet<>());

    // then
    mockMvc.perform(get("/recipe/1/ingredient/2/update"))
        .andExpect(status().isOk())
        .andExpect(view().name("recipe/ingredient/ingredientform"))
        .andExpect(model().attributeExists("ingredient"))
        .andExpect(model().attributeExists("uomList"));
  }

  @Test
  void testSaveOrUpdate() throws Exception {
    // given
    IngredientCommand command = new IngredientCommand();
    command.setId(3L);
    command.setRecipeId(2L);

    // when
    when(ingredientService.saveIngredientCommand(any())).thenReturn(command);

    // then
    mockMvc.perform(post("/recipe/2/ingredient"))
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name("redirect:/recipe/2/ingredient/3/show"));
  }

  @Test
  void testDeleteIngredient() throws Exception {
    mockMvc.perform(get("/recipe/2/ingredient/3/delete"))
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name("redirect:/recipe/2/ingredients"));
    verify(ingredientService, times(1)).deleteByIngredientId(anyLong(), anyLong());
  }
}