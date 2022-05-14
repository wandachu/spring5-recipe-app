package wanda.springframework.spring5recipeapp.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
import wanda.springframework.spring5recipeapp.services.RecipeService;

class IngredientControllerTest {

  @Mock
  RecipeService recipeService;

  IngredientController controller;

  MockMvc mockMvc;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    controller = new IngredientController(recipeService);
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
}