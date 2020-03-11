package project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.repositories.Post2TagRepository;

@Service
public class Post2TagService {

    private Post2TagRepository post2TagRepository;

    @Autowired
    public Post2TagService(Post2TagRepository post2TagRepository) {
        this.post2TagRepository = post2TagRepository;
    }


}
