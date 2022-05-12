package wanda.springframework.spring5recipeapp.converters;

import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import wanda.springframework.spring5recipeapp.commands.IngredientCommand;
import wanda.springframework.spring5recipeapp.domain.Ingredient;

@Component
public class IngredientCommandToIngredient implements Converter<IngredientCommand, Ingredient> {
  private final UnitOfMeasureCommandToUnitOfMeasure uomConverter;

  public IngredientCommandToIngredient(UnitOfMeasureCommandToUnitOfMeasure uomConverter) {
    this.uomConverter = uomConverter;
  }

  @Override
  @Synchronized
  @Nullable
  public Ingredient convert(IngredientCommand source) {
    if (source == null) {
      return null;
    }
    final Ingredient ingredient = new Ingredient();
    ingredient.setId(source.getId());
    ingredient.setDescription(source.getDescription());
    ingredient.setAmount(source.getAmount());
    ingredient.setUnitOfMeasure(this.uomConverter.convert(source.getUnitOfMeasure()));
    return ingredient;
  }
}
