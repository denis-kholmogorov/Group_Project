package project.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import project.dto.responseDto.ListResponseDto;
import project.repositories.FriendshipRepository;

@Service
@AllArgsConstructor
public class FriendshipService {
    private FriendshipRepository friendshipRepository;

    public ListResponseDto getFriendList(String name, Integer offset, Integer itemPerPage) {
        /*//Sort sort = Sort.by(Sort.Direction.DESC, "time");
        Pageable pageable = PageRequest.of(offset, itemPerPage);
        List<Friendship> friendshipList = friendshipRepository.(authorId, pageable);
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
            personsWallPostDto.setType(wallPost.getTime().before(new Date()) ? PostTypeEnum.POSTED.getType() : PostTypeEnum.QUEUED.getType());
            return personsWallPostDto;
        }).collect(toList());

        return new ListResponseDto(personsWallPostDtoList.size(), offset, limit, personsWallPostDtoList);*/
        return null;
    }
}
