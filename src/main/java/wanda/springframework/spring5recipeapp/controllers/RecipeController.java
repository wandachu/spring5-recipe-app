package wanda.springframework.spring5recipeapp.controllers;

import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import wanda.springframework.spring5recipeapp.commands.RecipeCommand;
import wanda.springframework.spring5recipeapp.exceptions.NotFoundException;
import wanda.springframework.spring5recipeapp.services.RecipeService;

@Controller
@Slf4j
@RequestMapping("/recipe")
public class RecipeController {
  private static final String RECIPE_RECIPEFORM_URL = "recipe/recipeform";
  private final RecipeService recipeService;

  @Autowired
  public RecipeController(RecipeService recipeService) {
    this.recipeService = recipeService;
  }

  @GetMapping("/{id}/show")
  public String showById(Model model, @PathVariable String id) {
    model.addAttribute("recipe", recipeService.findById(Long.valueOf(id)));
    return "recipe/show";
  }

  @GetMapping("/new")
  public String newRecipe(Model model) {
    model.addAttribute("recipe", new RecipeCommand());
    return RECIPE_RECIPEFORM_URL;
  }

  @PostMapping("/save")
  public String saveOrUpdate(@Valid @ModelAttribute("recipe") RecipeCommand command, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      bindingResult.getAllErrors().forEach(objectError -> {
        log.debug(objectError.toString());
      });
      return RECIPE_RECIPEFORM_URL;
    }
    RecipeCommand savedCommand = recipeService.saveRecipeCommand(command);
    return "redirect:/recipe/" + savedCommand.getId() + "/show";
  }

  @GetMapping("/{id}/update")
  public String updateRecipe(Model model, @PathVariable String id) {
    model.addAttribute("recipe", recipeService.findCommandById(Long.valueOf(id)));
    return RECIPE_RECIPEFORM_URL;
  }

  @GetMapping("/{id}/delete")
  public String deleteById(@PathVariable String id) {
    log.debug("Loading index page....");
    recipeService.deleteById(Long.valueOf(id));
    return "redirect:/";
  }

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(NotFoundException.class)
  public ModelAndView handleNotFound(Exception exception) {
    log.error("Handling not found exception");
    log.error(exception.getMessage());

    ModelAndView modelAndView = new ModelAndView();
    modelAndView.setViewName("404error");
    modelAndView.addObject("exception", exception);
    return modelAndView;
  }
}
