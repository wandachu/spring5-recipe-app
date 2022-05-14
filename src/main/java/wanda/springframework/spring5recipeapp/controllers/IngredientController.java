package wanda.springframework.spring5recipeapp.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import wanda.springframework.spring5recipeapp.services.RecipeService;

@Controller
@Slf4j
public class IngredientController {
  private final RecipeService recipeService;

  public IngredientController(RecipeService recipeService) {
    this.recipeService = recipeService;
  }

  @GetMapping
  @RequestMapping("/recipe/{id}/ingredients")
  public String getList(Model model, @PathVariable String id) {
    log.debug("Loading ingredients of recipe " + id);
    model.addAttribute("recipe", recipeService.findCommandById(Long.valueOf(id)));
    return "recipe/ingredient/list";
  }
}
