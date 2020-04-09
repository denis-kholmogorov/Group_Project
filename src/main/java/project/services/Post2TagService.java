package project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.models.Post2Tag;
import project.repositories.Post2TagRepository;

import java.util.List;

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

    public List<Post2Tag> findAllTagsByPostId(Integer postId) {
        return post2TagRepository.findAllByPostId(postId);
    }

}
