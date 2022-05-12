package wanda.springframework.spring5recipeapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import wanda.springframework.spring5recipeapp.services.RecipeService;

@Controller
public class RecipeController {
  private final RecipeService recipeService;

  @Autowired
  public RecipeController(RecipeService recipeService) {
    this.recipeService = recipeService;
  }

  @RequestMapping("/recipe/show/{id}")
  public String showById(Model model, @PathVariable String id) {
    model.addAttribute("recipe", recipeService.findById(Long.valueOf(id)));
    return "recipe/show";
  }
}
