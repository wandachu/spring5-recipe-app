package wanda.springframework.spring5recipeapp.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class ImageServiceImpl implements
    ImageService {

  @Override
  public void saveImage(Long recipeId, MultipartFile file) {
    log.debug("Received a file");
  }
}
