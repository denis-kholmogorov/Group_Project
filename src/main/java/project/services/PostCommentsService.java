package project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.repositories.PostCommentsRepository;

@Service
public class PostCommentsService {

    private PostCommentsRepository postCommentsRepository;

    @Autowired
    public PostCommentsService(PostCommentsRepository postCommentsRepository) {
        this.postCommentsRepository = postCommentsRepository;
    }
}
