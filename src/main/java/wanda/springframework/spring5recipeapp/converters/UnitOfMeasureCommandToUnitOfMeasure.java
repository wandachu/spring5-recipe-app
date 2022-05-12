package wanda.springframework.spring5recipeapp.converters;

import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import wanda.springframework.spring5recipeapp.commands.UnitOfMeasureCommand;
import wanda.springframework.spring5recipeapp.domain.UnitOfMeasure;

@Component
public class UnitOfMeasureCommandToUnitOfMeasure implements
    Converter<UnitOfMeasureCommand, UnitOfMeasure> {

  @Override
  @Synchronized
  @Nullable
  public UnitOfMeasure convert(UnitOfMeasureCommand source) {
    if (source == null) {
      return null;
    }
    final UnitOfMeasure unitOfMeasure = new UnitOfMeasure();
    unitOfMeasure.setId(source.getId());
    unitOfMeasure.setDescription(source.getDescription());
    return unitOfMeasure;
  }
}
