package wanda.springframework.spring5recipeapp.converters;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import wanda.springframework.spring5recipeapp.commands.CategoryCommand;
import wanda.springframework.spring5recipeapp.domain.Category;

class CategoryCommandToCategoryTest {
  public static final Long ID_VALUE = 1L;
  public static final String DESCRIPTION = "description";
  CategoryCommandToCategory converter;

  @BeforeEach
  void setUp() {
    converter = new CategoryCommandToCategory();
  }

  @Test
  public void testNullObject() {
    assertNull(converter.convert(null));
  }

  @Test
  public void testEmptyObject() {
    assertNotNull(converter.convert(new CategoryCommand()));
  }

  @Test
  void convert() {
    //given
    CategoryCommand categoryCommand = new CategoryCommand();
    categoryCommand.setId(ID_VALUE);
    categoryCommand.setDescription(DESCRIPTION);

    //when
    Category category = converter.convert(categoryCommand);

    //then
    assertEquals(ID_VALUE, category.getId());
    assertEquals(DESCRIPTION, category.getDescription());
  }
}