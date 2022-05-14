package wanda.springframework.spring5recipeapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import wanda.springframework.spring5recipeapp.commands.RecipeCommand;
import wanda.springframework.spring5recipeapp.services.RecipeService;

@Controller
public class RecipeController {
  private final RecipeService recipeService;

  @Autowired
  public RecipeController(RecipeService recipeService) {
    this.recipeService = recipeService;
  }

  @RequestMapping("/recipe/{id}/show")
  public String showById(Model model, @PathVariable String id) {
    model.addAttribute("recipe", recipeService.findById(Long.valueOf(id)));
    return "recipe/show";
  }

  @RequestMapping("/recipe/new")
  public String newRecipe(Model model) {
    model.addAttribute("recipe", new RecipeCommand());
    return "recipe/recipeform";
  }

  @RequestMapping("/recipe/{id}/update")
  public String updateRecipe(Model model, @PathVariable String id) {
    model.addAttribute("recipe", recipeService.findCommandById(Long.valueOf(id)));
    return "recipe/recipeform";
  }


  @PostMapping
  @RequestMapping("recipe")
  public String saveOrUpdate(@ModelAttribute RecipeCommand command) {
    RecipeCommand savedCommand = recipeService.saveRecipeCommand(command);
    return "redirect:/recipe/" + savedCommand.getId() + "/show";
  }
}
