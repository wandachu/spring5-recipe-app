package wanda.springframework.spring5recipeapp.commands;

import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import wanda.springframework.spring5recipeapp.domain.Difficulty;

@NoArgsConstructor
@Getter
@Setter
public class RecipeCommand {
  private Long id;
  private String description;
  private Integer prepTime;
  private Integer cookTime;
  private Integer servings;
  private String source;
  private String url;
  private String directions;
  private Set<IngredientCommand> ingredients = new HashSet<>();
  private Difficulty difficulty;
  private NotesCommand notes;
  private Set<CategoryCommand> categories = new HashSet<>();
}
