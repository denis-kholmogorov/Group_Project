package project.services;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import project.dto.PersonsWallPostDto;
import project.dto.PostDto;
import project.dto.requestDto.PostRequestBodyDto;
import project.dto.responseDto.ListResponseDto;
import project.dto.responseDto.ResponseDto;
import project.models.Post;
import project.models.Post2Tag;
import project.models.Tag;
import project.models.enums.PostTypeEnum;
import project.repositories.PersonRepository;
import project.repositories.Post2TagRepository;
import project.repositories.PostRepository;
import project.repositories.TagRepository;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class PostService {
    private PostRepository postRepository;
    private PersonRepository personRepository;
    private TagRepository tagRepository;
    private Post2TagRepository post2TagRepository;
    private PostLikeService postLikeService;
    PostCommentsService postCommentsService;

    public Post getPostById(Integer id) {
        Optional<Post> optionalPost = postRepository.findById(id);
        return optionalPost.orElse(null);
    }


    public ResponseDto<Post> addNewWallPostByAuthorId(Integer authorId, Long publishDate, PostRequestBodyDto dto) {
        Post post = new Post();
        post.setAuthorId(authorId);
        post.setTime(publishDate == null ? new Date() : getDateFromLong(publishDate + ""));
        post.setTitle(dto.getTitle());
        post.setPostText(dto.getPostText());
        post.setIsBlocked(false);
        int postId = postRepository.save(post).getId();

        List<String> tags = dto.getTags();
        if (tags.size() > 0) {
            tags.stream().forEach(tag -> {
                Tag tag2DB = new Tag();
                tag2DB.setTag(tag);
                int tagId = tagRepository.save(tag2DB).getId();
                Post2Tag post2Tag = new Post2Tag();
                post2Tag.setPostId(postId);
                post2Tag.setTagId(tagId);
                post2TagRepository.save(post2Tag);
            });
        }

        return new ResponseDto<>(post);
    }

    public ListResponseDto<PersonsWallPostDto> findAllByAuthorId(Integer authorId, Integer offset, Integer limit) {
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

        return new ListResponseDto<>(personsWallPostDtoList.size(), offset, limit, personsWallPostDtoList);
    }

    @SneakyThrows
    public List<Post> getPostsByTitleAndDate(String title, String dateFrom, String dateTo, Integer offset, Integer limit){
        Pageable pageable = PageRequest.of(offset, limit);

        Date startDate = getDateFromLong(dateFrom);
        Date endDate = getDateFromLong(dateTo);

        if(!title.isEmpty() && startDate != null && endDate != null){
            return postRepository.findAllByTitleContainingAndTimeBetween(title, startDate, endDate, pageable);
        }

        if(!title.isEmpty() && startDate != null){
            return postRepository.findAllByTitleContainingAndTimeAfter(title, startDate, pageable);
        }

        if(!title.isEmpty() && endDate != null){
            return postRepository.findAllByTitleContainingAndTimeBefore(title, endDate, pageable);
        }

        if(!title.isEmpty()){
            return postRepository.findAllByTitleContaining(title, pageable);
        }

        if(startDate != null && endDate != null){
            return postRepository.findAllByTimeBetween(startDate, endDate, pageable);
        }

        if(startDate != null){
            return postRepository.findAllByTimeAfter(startDate, pageable);
        }

        if(endDate != null){
            return postRepository.findAllByTimeBefore(endDate, pageable);
        }

        return postRepository.findAll(pageable);
    }

    public Date getDateFromLong(String date){
        if(!date.isEmpty()){
            Calendar calendar = Calendar.getInstance();
            long dateLong = Long.parseLong(date);
            calendar.setTimeInMillis(dateLong);
            return calendar.getTime();
        }
        return null;
    }
}
