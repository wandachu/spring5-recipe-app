package wanda.springframework.spring5recipeapp.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import wanda.springframework.spring5recipeapp.commands.RecipeCommand;
import wanda.springframework.spring5recipeapp.services.ImageService;
import wanda.springframework.spring5recipeapp.services.RecipeService;

class ImageControllerTest {

  @Mock
  private ImageService imageService;

  @Mock
  private RecipeService recipeService;

  private ImageController controller;

  MockMvc mockMvc;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    controller = new ImageController(imageService, recipeService);
    mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
  }

  @Test
  void showUploadForm() throws Exception { // handle image get request
    // given
    RecipeCommand recipeCommand = new RecipeCommand();
    recipeCommand.setId(1L);

    when(recipeService.findCommandById(anyLong())).thenReturn(recipeCommand);

    mockMvc.perform(get("/recipe/1/image"))
        .andExpect(status().isOk())
        .andExpect(model().attributeExists("recipe"));

    verify(recipeService, times(1)).findCommandById(anyLong());
  }

  @Test
  void handleImagePost() throws Exception {
    MockMultipartFile multiPartFile = new MockMultipartFile("imagefile", "testing.text", "text/plain", "Spring Framework".getBytes(
        StandardCharsets.UTF_8));

    mockMvc.perform(multipart("/recipe/1/image").file(multiPartFile))
        .andExpect(status().is3xxRedirection())
        .andExpect(header().string("Location", "/recipe/1/show"));
    verify(imageService, times(1)).saveImage(anyLong(), any());
  }

  @Test
  void renderImageFromDB() throws Exception {
    // given
    RecipeCommand command = new RecipeCommand();
    command.setId(1L);

    String s = "fake image text";
    byte[] bytes = new byte[s.getBytes().length];

    int i = 0;
    for (byte b : s.getBytes()) {
      bytes[i++] = b;
    }
    command.setImage(bytes);

    when(recipeService.findCommandById(anyLong())).thenReturn(command);

    // when
    MockHttpServletResponse response = mockMvc.perform(get("/recipe/1/recipeimage")).andExpect(status().isOk()).andReturn().getResponse();
    byte[] responseBytes = response.getContentAsByteArray();
    assertEquals(s.getBytes().length, responseBytes.length);
  }
}