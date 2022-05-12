package wanda.springframework.spring5recipeapp.controllers;

import static org.mockito.ArgumentMatchers.anyLong;
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
import wanda.springframework.spring5recipeapp.domain.Recipe;
import wanda.springframework.spring5recipeapp.services.RecipeService;

class RecipeControllerTest {
  private RecipeController recipeController;

  @Mock
  private RecipeService recipeService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    recipeController = new RecipeController(recipeService);
  }

  @Test
  void showById() throws Exception {
    Recipe recipe = new Recipe();
    recipe.setId(1L);

    MockMvc mockMvc = MockMvcBuilders.standaloneSetup(recipeController).build();

    when(recipeService.findById(anyLong())).thenReturn(recipe);

    mockMvc.perform(get("/recipe/show/1"))
        .andExpect(status().isOk())
        .andExpect(view().name("recipe/show"))
        .andExpect(model().attributeExists("recipe"));
  }
}