package project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.models.PostLike;
import project.repositories.PostLikeRepository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
        postLike.setTime(new Date());
        return postLikeRepository.save(postLike);
    }

    public Integer deleteLike(Integer postId, Integer personId){

        postLikeRepository.deleteByPostIdAndPersonId(postId, personId);

        return postLikeRepository.countAllByPostId(postId);
    }

    public List<Integer> getAllPersonIdWhoLikedPost(Integer postId){

       List<PostLike> postLikeList = postLikeRepository.findAllByPostId(postId);

       return postLikeList.stream().map(PostLike::getPersonId).collect(Collectors.toList());
    }
}
