package wanda.springframework.spring5recipeapp.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CategoryTest {
  private Category category;

  @BeforeEach
  void setUp() {
    category = new Category();
  }

  @Test
  void getId() {
    Long idValue = 4L;
    category.setId(idValue);
    assertEquals(idValue, category.getId());
  }
}