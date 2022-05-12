package wanda.springframework.spring5recipeapp.commands;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class IngredientCommand {
  private Long id;
  private String description;
  private BigDecimal amount;
  private UnitOfMeasureCommand unitOfMeasure;
}
