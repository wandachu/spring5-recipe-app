package wanda.springframework.spring5recipeapp.converters;

import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import wanda.springframework.spring5recipeapp.commands.NotesCommand;
import wanda.springframework.spring5recipeapp.domain.Notes;

@Component
public class NotesCommandToNotes implements Converter<NotesCommand, Notes> {

  @Override
  @Nullable
  @Synchronized
  public Notes convert(NotesCommand source) {
    if (source == null) {
      return null;
    }
    final Notes notes = new Notes();
    notes.setId(source.getId());
    notes.setRecipeNotes(source.getRecipeNotes());
    return notes;
  }
}
