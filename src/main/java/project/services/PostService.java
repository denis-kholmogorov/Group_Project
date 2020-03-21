package project.services;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import project.dto.PersonsWallPostDto;
import project.dto.responseDto.ListResponseDto;
import project.models.Post;
import project.repositories.PersonRepository;
import project.repositories.PostRepository;

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
            PersonsWallPostDto personsWallPostDto = new PersonsWallPostDto();
            personsWallPostDto.setId(wallPost.getId());
            personsWallPostDto.setTime(wallPost.getTime());
            personsWallPostDto.setAuthor(personRepository.findById(wallPost.getAuthorId()).orElse(null));
            personsWallPostDto.setTitle(wallPost.getTitle());
            personsWallPostDto.setPostText(wallPost.getPostText());
            personsWallPostDto.setIsBlocked(wallPost.getIsBlocked());
            personsWallPostDto.setLikes(postLikeService.countLikesByPostId(wallPost.getId()));
            personsWallPostDto.setComments(postCommentsService.getListCommentsDto(wallPost.getId()));
            return personsWallPostDto;
        }).collect(toList());

        return new ListResponseDto(personsWallPostDtoList.size(), offset, limit, personsWallPostDtoList);
    }
}
