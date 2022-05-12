package wanda.springframework.spring5recipeapp.converters;

import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import wanda.springframework.spring5recipeapp.commands.RecipeCommand;
import wanda.springframework.spring5recipeapp.domain.Category;
import wanda.springframework.spring5recipeapp.domain.Recipe;

@Component
public class RecipeToRecipeCommand implements Converter<Recipe, RecipeCommand> {
  private final CategoryToCategoryCommand categoryConverter;
  private final IngredientToIngredientCommand ingredientConverter;
  private final NotesToNotesCommand notesConverter;

  public RecipeToRecipeCommand(CategoryToCategoryCommand categoryConverter,
      IngredientToIngredientCommand ingredientConverter, NotesToNotesCommand notesConverter) {
    this.categoryConverter = categoryConverter;
    this.ingredientConverter = ingredientConverter;
    this.notesConverter = notesConverter;
  }

  @Override
  @Synchronized
  @Nullable
  public RecipeCommand convert(Recipe source) {
    if (source == null) {
      return null;
    }

    final RecipeCommand command = new RecipeCommand();
    command.setId(source.getId());
    command.setCookTime(source.getCookTime());
    command.setPrepTime(source.getPrepTime());
    command.setDescription(source.getDescription());
    command.setDifficulty(source.getDifficulty());
    command.setDirections(source.getDirections());
    command.setServings(source.getServings());
    command.setSource(source.getSource());
    command.setUrl(source.getUrl());
    command.setNotes(notesConverter.convert(source.getNotes()));

    source.getCategories()
        .forEach((Category category) -> command.getCategories().add(categoryConverter.convert(category)));

    source.getIngredients()
        .forEach(ingredient -> command.getIngredients().add(ingredientConverter.convert(ingredient)));

    return command;
  }
}
