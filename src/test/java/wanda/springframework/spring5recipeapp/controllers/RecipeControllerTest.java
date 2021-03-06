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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import wanda.springframework.spring5recipeapp.commands.RecipeCommand;
import wanda.springframework.spring5recipeapp.domain.Recipe;
import wanda.springframework.spring5recipeapp.exceptions.NotFoundException;
import wanda.springframework.spring5recipeapp.services.RecipeService;

class RecipeControllerTest {
  private RecipeController recipeController;

  @Mock
  private RecipeService recipeService;

  MockMvc mockMvc;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    recipeController = new RecipeController(recipeService);
    mockMvc = MockMvcBuilders.standaloneSetup(recipeController).build();
  }

  @Test
  void showById() throws Exception {
    Recipe recipe = new Recipe();
    recipe.setId(1L);

    when(recipeService.findById(anyLong())).thenReturn(recipe);

    mockMvc.perform(get("/recipe/1/show"))
        .andExpect(status().isOk())
        .andExpect(view().name("recipe/show"))
        .andExpect(model().attributeExists("recipe"));
  }

  @Test
  void testGetRecipeNotFound() throws Exception {
    when(recipeService.findById(anyLong())).thenThrow(NotFoundException.class);
    mockMvc.perform(get("/recipe/1/show"))
        .andExpect(status().isNotFound())
        .andExpect(view().name("404error"));
  }

  @Test
  void testGetNewRecipe() throws Exception {
    mockMvc.perform(get("/recipe/new"))
        .andExpect(status().isOk())
        .andExpect(view().name("recipe/recipeform"))
        .andExpect(model().attributeExists("recipe"));
  }

  @Test
  void testPostNewRecipeForm() throws Exception {
    RecipeCommand command = new RecipeCommand();
    command.setId(2L);

    when(recipeService.saveRecipeCommand(any())).thenReturn(command);

    mockMvc.perform(post("/recipe")
        .param("id", "")
        .param("description", "some string")
        .param("directions", "some directions"))
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name("redirect:/recipe/2/show"));
  }

  @Test
  void testPostNewRecipeFormValidationFail() throws Exception {
    RecipeCommand command = new RecipeCommand();
    command.setId(2L);

    when(recipeService.saveRecipeCommand(any())).thenReturn(command);

    mockMvc.perform(post("/recipe")
        .param("id", ""))
        .andExpect(status().isOk()) // not redirect because we fail two validation
        .andExpect(model().attributeExists("recipe"))
        .andExpect(view().name("recipe/recipeform")); // back to this form since we fail
  }

  @Test
  void testGetUpdateView() throws Exception {
    RecipeCommand command = new RecipeCommand();
    command.setId(1L);

    when(recipeService.findCommandById(anyLong())).thenReturn(command);

    mockMvc.perform(
            get("/recipe/1/update"))
           .andExpect(status().isOk())
           .andExpect(view().name("recipe/recipeform"))
           .andExpect(model().attributeExists("recipe"));
  }

  @Test
  void testDeleteAction() throws Exception {
    mockMvc.perform(get("/recipe/1/delete"))
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name("redirect:/"));
    verify(recipeService, times(1)).deleteById(anyLong());
  }
}