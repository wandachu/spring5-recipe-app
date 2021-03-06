package wanda.springframework.spring5recipeapp.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import wanda.springframework.spring5recipeapp.commands.IngredientCommand;
import wanda.springframework.spring5recipeapp.commands.RecipeCommand;
import wanda.springframework.spring5recipeapp.commands.UnitOfMeasureCommand;
import wanda.springframework.spring5recipeapp.services.IngredientService;
import wanda.springframework.spring5recipeapp.services.RecipeService;
import wanda.springframework.spring5recipeapp.services.UnitOfMeasureService;

@Controller
@Slf4j
public class IngredientController {
  private final RecipeService recipeService;
  private final IngredientService ingredientService;
  private final UnitOfMeasureService unitOfMeasureService;

  public IngredientController(RecipeService recipeService, IngredientService ingredientService,
      UnitOfMeasureService unitOfMeasureService) {
    this.recipeService = recipeService;
    this.ingredientService = ingredientService;
    this.unitOfMeasureService = unitOfMeasureService;
  }

  @GetMapping("/recipe/{id}/ingredients")
  public String listIngredients(Model model, @PathVariable String id) {
    log.debug("Loading ingredients of recipe " + id);
    model.addAttribute("recipe", recipeService.findCommandById(Long.valueOf(id)));
    return "recipe/ingredient/list";
  }

  @GetMapping("/recipe/{recipeId}/ingredient/{id}/show")
  public String showRecipeIngredient(@PathVariable String recipeId, @PathVariable String id, Model model) {
    model.addAttribute("ingredient", ingredientService.findByRecipeIdAndIngredientId(Long.valueOf(recipeId), Long.valueOf(id)));
    return "recipe/ingredient/show";
  }

  @GetMapping("recipe/{recipeId}/ingredient/new")
  public String newIngredient(@PathVariable String recipeId, Model model) {
    // make sure we have a good id value
    RecipeCommand recipeCommand = recipeService.findCommandById(Long.valueOf(recipeId));

    // need to return parent id for hidden form property
    IngredientCommand ingredientCommand = new IngredientCommand();
    ingredientCommand.setRecipeId(recipeCommand.getId());
    model.addAttribute("ingredient", ingredientCommand);

    // init uom
    ingredientCommand.setUnitOfMeasure(new UnitOfMeasureCommand());
    model.addAttribute("uomList", unitOfMeasureService.listAllUoms());
    return "recipe/ingredient/ingredientform";
  }

  @GetMapping("/recipe/{recipeId}/ingredient/{id}/update")
  public String updateRecipeIngredient(@PathVariable String recipeId, @PathVariable String id, Model model) {
    model.addAttribute("ingredient", ingredientService.findByRecipeIdAndIngredientId(Long.valueOf(recipeId), Long.valueOf(id)));
    model.addAttribute("uomList", unitOfMeasureService.listAllUoms());
    return "recipe/ingredient/ingredientform";
  }

  @PostMapping("/recipe/{recipeId}/ingredient")
  public String saveOrUpdate(IngredientCommand command) {
    IngredientCommand savedCommand = ingredientService.saveIngredientCommand(command);
    log.debug("saved receipe id:" + savedCommand.getRecipeId());
    log.debug("saved ingredient id:" + savedCommand.getId());
    return "redirect:/recipe/" + savedCommand.getRecipeId() + "/ingredient/" + savedCommand.getId() + "/show";
  }

  @GetMapping("/recipe/{recipeId}/ingredient/{id}/delete")
  public String deleteIngredient(@PathVariable String recipeId, @PathVariable String id) {
    log.debug("deleting ingredient id: " + id);
    ingredientService.deleteByIngredientId(Long.valueOf(recipeId), Long.valueOf(id));
    return "redirect:/recipe/" + recipeId + "/ingredients";
  }
}
