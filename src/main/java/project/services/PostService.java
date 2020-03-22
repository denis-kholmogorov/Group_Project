package project.services;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import project.dto.PersonsWallPostDto;
import project.dto.PostDto;
import project.dto.responseDto.ListResponseDto;
import project.models.Post;
import project.models.ResponseModel;
import project.models.enums.PostTypeEnum;
import project.repositories.PersonRepository;
import project.repositories.PostRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class PostService {
    private PostRepository postRepository;
    private PersonRepository personRepository;
    private PostLikeService postLikeService;
    PostCommentsService postCommentsService;

    public Post getPostById(Integer id){
        Optional<Post> optionalPost = postRepository.findById(id);
        return optionalPost.orElse(null);
    }

    public ListResponseDto findAllByAuthorId(Integer authorId, Integer offset, Integer limit) {
        Sort sort = Sort.by(Sort.Direction.DESC, "time");
        Pageable pageable = PageRequest.of(offset, limit, sort);
        List<Post> wallPostList = postRepository.findAllByAuthorId(authorId, pageable);
        List<PersonsWallPostDto> personsWallPostDtoList = wallPostList.stream().map(wallPost -> {
            PostDto postDto = new PostDto();
            postDto.setId(wallPost.getId());
            postDto.setTime(wallPost.getTime());
            postDto.setAuthor(personRepository.findById(wallPost.getAuthorId()).orElse(null));
            postDto.setTitle(wallPost.getTitle());
            postDto.setPostText(wallPost.getPostText());
            postDto.setIsBlocked(wallPost.getIsBlocked());
            postDto.setLikes(postLikeService.countLikesByPostId(wallPost.getId()));
            postDto.setComments(postCommentsService.getListCommentsDto(wallPost.getId()));
            return new PersonsWallPostDto(postDto, wallPost.getTime().before(new Date()) ? PostTypeEnum.POSTED.getType() : PostTypeEnum.QUEUED.getType());
        }).collect(toList());

        return new ListResponseDto(new ResponseModel(), personsWallPostDtoList.size(), offset, limit, personsWallPostDtoList);
    }
}
