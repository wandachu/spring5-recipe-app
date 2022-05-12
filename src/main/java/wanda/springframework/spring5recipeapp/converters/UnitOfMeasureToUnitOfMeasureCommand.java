package wanda.springframework.spring5recipeapp.converters;

import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import wanda.springframework.spring5recipeapp.commands.UnitOfMeasureCommand;
import wanda.springframework.spring5recipeapp.domain.UnitOfMeasure;

@Component
public class UnitOfMeasureToUnitOfMeasureCommand implements
    Converter<UnitOfMeasure, UnitOfMeasureCommand> {

  @Override
  @Synchronized
  @Nullable
  public UnitOfMeasureCommand convert(UnitOfMeasure source) {
    if (source == null) {
      return null;
    }
    final UnitOfMeasureCommand unitOfMeasureCommand = new UnitOfMeasureCommand();
    unitOfMeasureCommand.setId(source.getId());
    unitOfMeasureCommand.setDescription(source.getDescription());
    return unitOfMeasureCommand;
  }
}
