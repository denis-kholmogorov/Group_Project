package project.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import project.models.Image;
import project.repositories.ImageRepository;

@Service
@AllArgsConstructor
public class GeneralService {

    private ImageRepository imageRepository;

    public Integer saveImage(byte[] arrayImage) {
        Image image = new Image();
        image.setImage(arrayImage);
        Image imageSaved = imageRepository.save(image);
        return imageSaved.getId();
    }

    public Image getImage(Integer id) {
        return imageRepository.findById(id).orElse(null);
    }
 }
