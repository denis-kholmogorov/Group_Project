package project.services;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import project.dto.CommentDto;
import project.dto.PersonsWallPostDto;
import project.dto.PostDto;
import project.dto.requestDto.PostRequestBodyDto;
import project.dto.requestDto.PostRequestBodyTagsDto;
import project.dto.responseDto.ListResponseDto;
import project.dto.responseDto.ResponseDto;
import project.models.Person;
import project.models.Post;
import project.models.Post2Tag;
import project.models.Tag;
import project.models.enums.PostTypeEnum;
import project.repositories.PostRepository;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class PostService {
    private PostRepository postRepository;
    private TagService tagService;
    private Post2TagService post2TagService;
    private PostLikeService postLikeService;
    private PersonService personService;
    private PostCommentsService postCommentsService;

    public Post getPostById(Integer id) {
        Optional<Post> optionalPost = postRepository.findById(id);
        return optionalPost.orElse(null);
    }

    public ResponseDto<PostDto> editPostById(Integer id, Long publishDate, PostRequestBodyDto dto) {
        Post post = getPostById(id);
        if (post != null) {
            post.setTitle(dto.getTitle());
            post.setTime(publishDate == null ? new Date() : getDateFromLong(publishDate + ""));
            post.setPostText(dto.getPostText());
            Post postDB = postRepository.save(post);

            return new ResponseDto<>(getPostDtoById(postDB.getId()));
        }
        return null;
    }

    public ResponseDto<Integer> deletePostById(@PathVariable Integer id) {
        postRepository.deleteById(id);
        return new ResponseDto<>(id);
    }

    public PostDto getPostDtoById(Integer id) {
        Post post = getPostById(id);
        Person person = personService.findPersonById(post.getAuthorId());

        Integer countLikes = postLikeService.countLikesByPostId(post.getId());

        List<CommentDto> comments = postCommentsService.getListCommentsDto(post.getId());

        return new PostDto(post.getId(), post.getTime(), person, post.getTitle(),
                post.getPostText(), post.getIsBlocked(), countLikes, comments);
    }


    public ResponseDto<PostDto> addNewWallPostByAuthorId(Integer authorId, Long publishDate, PostRequestBodyTagsDto dto) {
        Post post = new Post();
        post.setAuthorId(authorId);
        post.setTime(publishDate == null ? new Date() : getDateFromLong(publishDate + ""));
        post.setTitle(dto.getTitle());
        post.setPostText(dto.getPostText());
        post.setIsBlocked(false);
        Post finalPost = postRepository.save(post);

        List<String> tags = dto.getTags();
        if (tags.size() > 0) {
            tags.stream().forEach(tag -> {
                Tag tag2DB = new Tag();
                tag2DB.setTag(tag);
                int tagId = tagService.addNewTag(tag2DB).getId();
                Post2Tag post2Tag = new Post2Tag();
                post2Tag.setPostId(finalPost.getId());
                post2Tag.setTagId(tagId);
                post2TagService.addNewPost2Tag(post2Tag);
            });
        }

        return new ResponseDto<>(getPostDtoById(finalPost.getId()));
    }

    public ListResponseDto findAllByAuthorId(Integer authorId, Integer offset, Integer limit) {
        Sort sort = Sort.by(Sort.Direction.DESC, "time");
        Pageable pageable = PageRequest.of(offset, limit, sort);
        List<Post> wallPostList = postRepository.findAllByAuthorId(authorId, pageable);
        List<PersonsWallPostDto> personsWallPostDtoList = wallPostList.stream().map(wallPost -> {
            PersonsWallPostDto personsWallPostDto = new PersonsWallPostDto();
            personsWallPostDto.setId(wallPost.getId());
            personsWallPostDto.setTime(wallPost.getTime());
            personsWallPostDto.setAuthor(personService.findPersonById(wallPost.getAuthorId()));
            personsWallPostDto.setTitle(wallPost.getTitle());
            personsWallPostDto.setPostText(wallPost.getPostText());
            personsWallPostDto.setIsBlocked(wallPost.getIsBlocked());
            personsWallPostDto.setLikes(postLikeService.countLikesByPostId(wallPost.getId()));
            personsWallPostDto.setComments(postCommentsService.getListCommentsDto(wallPost.getId()));
            personsWallPostDto.setType(wallPost.getTime().before(new Date()) ? PostTypeEnum.POSTED.getType() : PostTypeEnum.QUEUED.getType());
            return personsWallPostDto;
        }).collect(toList());

        return new ListResponseDto(personsWallPostDtoList.size(), offset, limit, personsWallPostDtoList);
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
