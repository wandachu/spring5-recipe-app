package wanda.springframework.spring5recipeapp.services;

import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import wanda.springframework.spring5recipeapp.domain.Recipe;
import wanda.springframework.spring5recipeapp.repositories.RecipeRepository;

@Service
@Slf4j
public class ImageServiceImpl implements
    ImageService {

  private final RecipeRepository recipeRepository;

  public ImageServiceImpl(RecipeRepository recipeRepository) {
    this.recipeRepository = recipeRepository;
  }

  @Override
  public void saveImage(Long recipeId, MultipartFile file) {
    Recipe recipe = recipeRepository.findById(recipeId).get();
    try {
      byte[] byteObjects = new byte[file.getBytes().length];
      int i = 0;
      for (byte b : file.getBytes()) {
        byteObjects[i++] = b;
      }
      recipe.setImage(byteObjects);
      recipeRepository.save(recipe);
    } catch (IOException e) {
      log.error("Error occurred while reading bytes");
      e.printStackTrace();
    }
  }
}
