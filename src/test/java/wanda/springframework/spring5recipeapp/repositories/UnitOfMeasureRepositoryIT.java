package wanda.springframework.spring5recipeapp.repositories;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import wanda.springframework.spring5recipeapp.domain.UnitOfMeasure;

@SpringBootTest
class UnitOfMeasureRepositoryIT {

  @Autowired
  private UnitOfMeasureRepository unitOfMeasureRepository;

  @BeforeEach
  void setUp() {
  }

  @Test
  void findByDescription() {
    Optional<UnitOfMeasure> uomOptional = unitOfMeasureRepository.findByDescription("Teaspoon");
    assertEquals("Teaspoon", uomOptional.get().getDescription());
  }

  @Test
  void findByDescription2() {
    Optional<UnitOfMeasure> uomOptional = unitOfMeasureRepository.findByDescription("Tablespoon");
    assertEquals("Tablespoon", uomOptional.get().getDescription());
  }
}