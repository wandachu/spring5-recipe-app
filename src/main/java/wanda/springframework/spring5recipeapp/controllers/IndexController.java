package wanda.springframework.spring5recipeapp.controllers;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import wanda.springframework.spring5recipeapp.domain.Category;
import wanda.springframework.spring5recipeapp.domain.UnitOfMeasure;
import wanda.springframework.spring5recipeapp.repositories.CategoryRepository;
import wanda.springframework.spring5recipeapp.repositories.UnitOfMeasureRepository;
import wanda.springframework.spring5recipeapp.service.RecipeService;

@Controller
public class IndexController {
  private final RecipeService recipeService;

  @Autowired
  public IndexController(RecipeService recipeService) {
    this.recipeService = recipeService;
  }

  @RequestMapping({"", "/", "/index"})
  public String getIndexPage(Model model) {
    model.addAttribute("recipes", recipeService.getRecipes());
    return "index";
  }
}
