package wanda.springframework.spring5recipeapp.controllers;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import javax.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import wanda.springframework.spring5recipeapp.commands.RecipeCommand;
import wanda.springframework.spring5recipeapp.services.ImageService;
import wanda.springframework.spring5recipeapp.services.RecipeService;

@Controller
public class ImageController {
  private final ImageService imageService;
  private final RecipeService recipeService;

  public ImageController(ImageService imageService, RecipeService recipeService) {
    this.imageService = imageService;
    this.recipeService = recipeService;
  }

  @GetMapping("/recipe/{id}/image")
  public String showUploadForm(@PathVariable String id, Model model) {
    model.addAttribute("recipe", recipeService.findCommandById(Long.valueOf(id)));
    return "recipe/imageuploadform";
  }

  @PostMapping("/recipe/{id}/image")
  public String handleImagePost(@PathVariable String id, @RequestParam("imagefile") MultipartFile file) {
    imageService.saveImage(Long.valueOf(id), file);
    return "redirect:/recipe/" + id + "/show";
  }

  @GetMapping("/recipe/{id}/recipeimage")
  public void renderImageFromDB(@PathVariable String id, HttpServletResponse response) throws IOException {
    RecipeCommand recipeCommand = recipeService.findCommandById(Long.valueOf(id));
    byte[] byteArray;

    if (recipeCommand.getImage() == null) { // Guard recipeCommand doesn't have an image
      String defaultImagePath = "src/main/resources/static/images/guacamole400x400WithX.jpg";
      String validPath = defaultImagePath.replaceAll("[/\\\\]", "\\" + File.separator);
      byteArray = Files.readAllBytes(new File(validPath).toPath());
    } else {
      byteArray = new byte[recipeCommand.getImage().length];
      int i = 0;
      for (byte b : recipeCommand.getImage()) {
        byteArray[i++] = b;
      }
    }

    response.setContentType("image/jpeg");
    InputStream is = new ByteArrayInputStream(byteArray);
    IOUtils.copy(is, response.getOutputStream());
  }
}
