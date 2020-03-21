package project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.dto.CommentDto;
import project.dto.CommentModelDto;
import project.models.PostComment;
import project.repositories.PostCommentsRepository;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class PostCommentsService {

    private PostCommentsRepository postCommentsRepository;

    @Autowired
    public PostCommentsService(PostCommentsRepository postCommentsRepository) {
        this.postCommentsRepository = postCommentsRepository;
    }

    public List<CommentDto> getListCommentsDto(Integer postId){
        List<PostComment> postComments = postCommentsRepository.findAllByPostIdAndIsBlocked(postId, false);
        return postComments.stream().map(comment ->{
            CommentModelDto commentModelDto = new CommentModelDto(comment.getParentId(), comment.getComment());
            return new CommentDto(commentModelDto, comment.getId(), comment.getPostId(),
                    comment.getTime(), comment.getAuthorId(), comment.getIsBlocked());
        }).collect(toList());
    }
}
