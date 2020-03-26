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
import project.handlerExceptions.BadRequestException400;
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

    public ListResponseDto<PostDto> findAllPosts(String name, Integer offset, Integer itemPerPage) throws BadRequestException400 {
        Sort sort = Sort.by(Sort.Direction.DESC, name == null ? "time" : "title");
        Pageable pageable = PageRequest.of(offset, itemPerPage, sort);
        List<Post> postList = name != null ?
                postRepository.findAllByTitleContainingAndTimeBeforeAndIsBlocked(
                        name, new Date(), false, pageable)
                :
                postRepository.findAllByTimeBeforeAndIsBlocked(new Date(), false, pageable);
        if (postList == null) throw new BadRequestException400();
        List<PostDto> postDtoList = postList.stream().map(post -> getPostDtoById(null, post)).collect(toList());

        return new ListResponseDto((long) postDtoList.size(), offset, itemPerPage, postDtoList);
    }

    public Post getPostById(Integer id) throws BadRequestException400 {
        Optional<Post> optionalPost = postRepository.findById(id);
        if (!optionalPost.isPresent()) throw new BadRequestException400();
        return optionalPost.orElse(null);
    }

    public ResponseDto<PostDto> editPostById(Integer id, Long publishDate, PostRequestBodyDto dto)
            throws BadRequestException400 {
        Post post = getPostById(id);
        if (post == null) throw new BadRequestException400();
        post.setTitle(dto.getTitle());
        post.setTime(publishDate == null ? new Date() : getDateFromLong(publishDate + ""));
        post.setPostText(dto.getPostText());
        Post postDB = postRepository.save(post);

        return new ResponseDto<>(getPostDtoById(null, postDB));
    }

    public ResponseDto<Integer> deletePostById(@PathVariable Integer id) {
        postRepository.deleteById(id);  //как правильно обработать 400?
        return new ResponseDto<>(id);
    }

    @SneakyThrows
    public PostDto getPostDtoById(Integer id, Post post2Dto) {
        Post post = post2Dto == null ? getPostById(id) : post2Dto;
        if (post == null) throw new BadRequestException400();
        Person person = personService.findPersonById(post.getAuthorId());

        Integer countLikes = postLikeService.countLikesByPostId(post.getId());

        List<CommentDto> comments = postCommentsService.getListCommentsDto(post.getId());

        return new PostDto(post.getId(), post.getTime(), person, post.getTitle(),
                post.getPostText(), post.getIsBlocked(), countLikes, comments);
    }


    public ResponseDto<PostDto> addNewWallPostByAuthorId(Integer authorId,
                                                         Long publishDate,
                                                         PostRequestBodyTagsDto dto) throws BadRequestException400 {
        Post post = new Post();
        post.setAuthorId(authorId);
        post.setTime(publishDate == null ? new Date() : getDateFromLong(publishDate + ""));
        post.setTitle(dto.getTitle());
        post.setPostText(dto.getPostText());
        post.setIsBlocked(false);
        Post finalPost = postRepository.save(post);
        if (finalPost == null) throw new BadRequestException400();

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

        return new ResponseDto<>(getPostDtoById(null, finalPost));
    }

    public ListResponseDto findAllByAuthorId(
            Integer authorId, Integer offset, Integer limit, Integer compareId)
            throws BadRequestException400 {
        Sort sort = Sort.by(Sort.Direction.DESC, "time");
        Pageable pageable = PageRequest.of(offset, limit, sort);
        List<Post> wallPostList = !authorId.equals(compareId) ?
                postRepository.findAllByAuthorIdAndTimeBeforeAndIsBlocked(
                        authorId, new Date(), false, pageable)
                :
                postRepository.findAllByAuthorIdAndIsBlocked(authorId, false, pageable);
        if (wallPostList == null) throw new BadRequestException400();

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
            personsWallPostDto.setType(wallPost.getTime().before(new Date()) ? PostTypeEnum.POSTED.getType()
                    : PostTypeEnum.QUEUED.getType());
            return personsWallPostDto;
        }).collect(toList());

        return new ListResponseDto((long) personsWallPostDtoList.size(), offset, limit, personsWallPostDtoList);
    }

    @SneakyThrows
    public List<Post> getPostsByTitleAndDate(
            String title, String dateFrom, String dateTo, Integer offset, Integer limit) {  //как тут обрабоать ошибку я не понял)
        Pageable pageable = PageRequest.of(offset, limit);

        Date startDate = getDateFromLong(dateFrom);
        Date endDate = getDateFromLong(dateTo);

        if (!title.isEmpty() && startDate != null && endDate != null) {
            return postRepository.findAllByTitleContainingAndTimeBetweenAndIsBlocked(title, startDate,
                    endDate, false, pageable);
        }

        if (!title.isEmpty() && startDate != null) {
            return postRepository
                    .findAllByTitleContainingAndTimeAfterAndIsBlocked(title, startDate, false, pageable);
        }

        if (!title.isEmpty() && endDate != null) {
            return postRepository
                    .findAllByTitleContainingAndTimeBeforeAndIsBlocked(title, endDate, false, pageable);
        }

        if (!title.isEmpty()) {
            return postRepository.findAllByTitleContainingAndIsBlocked(title, false, pageable);
        }

        if (startDate != null && endDate != null) {
            return postRepository.findAllByTimeBetweenAndIsBlocked(startDate, endDate, false, pageable);
        }

        if (startDate != null) {
            return postRepository.findAllByTimeAfterAndIsBlocked(startDate, false, pageable);
        }

        if (endDate != null) {
            return postRepository.findAllByTimeBeforeAndIsBlocked(endDate, false, pageable);
        }

        return postRepository.findAllByIsBlocked(false, pageable);
    }

    public void deleteAllPostsByAuthorId(Integer id) {
        postRepository.deleteAllByAuthorId(id);
    }

    public Date getDateFromLong(String date) {
        if (!date.isEmpty()) {
            Calendar calendar = Calendar.getInstance();
            long dateLong = Long.parseLong(date);
            calendar.setTimeInMillis(dateLong);
            return calendar.getTime();
        }
        return null;
    }
}
