package project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.models.PostLike;
import project.repositories.PostLikeRepository;

import java.time.LocalDateTime;

@Service
public class PostLikeService {

    private PostLikeRepository postLikeRepository;

    @Autowired
    public PostLikeService(PostLikeRepository postLikeRepository) {
        this.postLikeRepository = postLikeRepository;
    }

    public Integer countLikesByPostId (Integer postId){
        return postLikeRepository.countAllByPostId(postId);
    }

    public PostLike addLike(Integer personId, Integer postId){
        PostLike postLike = new PostLike();
        postLike.setPersonId(personId);
        postLike.setPostId(postId);
        postLike.setTime(LocalDateTime.now());
        postLikeRepository.save(postLike);

        return postLike;
    }

    public Integer deleteLike(Integer postId, Integer personId){

        postLikeRepository.deleteByPostIdAndPersonId(postId, personId);

        return postLikeRepository.countAllByPostId(postId);
    }
}
