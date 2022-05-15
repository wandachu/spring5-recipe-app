package wanda.springframework.spring5recipeapp.converters;

import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import wanda.springframework.spring5recipeapp.commands.IngredientCommand;
import wanda.springframework.spring5recipeapp.domain.Ingredient;

@Component
public class IngredientToIngredientCommand implements Converter<Ingredient, IngredientCommand> {
  private UnitOfMeasureToUnitOfMeasureCommand uomConverter;

  public IngredientToIngredientCommand(UnitOfMeasureToUnitOfMeasureCommand uomConverter) {
    this.uomConverter = uomConverter;
  }

  @Override
  @Nullable
  @Synchronized
  public IngredientCommand convert(Ingredient source) {
    if (source == null) {
      return null;
    }
    final IngredientCommand ingredientCommand = new IngredientCommand();
    ingredientCommand.setId(source.getId());
    ingredientCommand.setDescription(source.getDescription());
    if (source.getRecipe() != null) {
      ingredientCommand.setRecipeId(source.getRecipe().getId());
    }
    ingredientCommand.setAmount(source.getAmount());
    ingredientCommand.setUnitOfMeasure(this.uomConverter.convert(source.getUnitOfMeasure()));
    return ingredientCommand;
  }
}
