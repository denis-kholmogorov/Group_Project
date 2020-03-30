package project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.models.Post2Tag;
import project.repositories.Post2TagRepository;

@Service
public class Post2TagService {

    private Post2TagRepository post2TagRepository;

    @Autowired
    public Post2TagService(Post2TagRepository post2TagRepository) {
        this.post2TagRepository = post2TagRepository;
    }

    public Post2Tag addNewPost2Tag(Post2Tag post2Tag) {
        return post2TagRepository.save(post2Tag);
    }

}
