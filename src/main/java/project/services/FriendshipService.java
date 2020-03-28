package project.services;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project.dto.responseDto.ListResponseDto;
import project.handlerExceptions.BadRequestException400;
import project.models.Friendship;
import project.models.Person;
import project.repositories.FriendshipRepository;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class FriendshipService {
    private FriendshipRepository friendshipRepository;
    private PersonService personService;

//    @PostConstruct
//    public void init() {
//        Friendship friendship = new Friendship();
//        friendship.setStatusId(14);
//        friendship.setPersonIdWhoSendFriendship(2);
//        friendship.setPersonIdWhoTakeFriendship(4);
//
//        friendshipRepository.save(friendship);
//
//        Friendship friendship1 = new Friendship();
//        friendship1.setStatusId(15);
//        friendship1.setPersonIdWhoSendFriendship(16);
//        friendship1.setPersonIdWhoTakeFriendship(2);
//
//        friendshipRepository.save(friendship1);
//    }

    public ListResponseDto getFriendList(String name, Integer offset, Integer itemPerPage, Person person) {
        //Sort sort = Sort.by(Sort.Direction.DESC, "time");
        Pageable pageable = PageRequest.of(offset, itemPerPage);
        int personId = person.getId();
        List<Friendship> friendshipList =
                friendshipRepository.findAllByPersonIdWhoSendFriendshipOrPersonIdWhoTakeFriendshipAndStatus(
                        personId, pageable);

        List<Person> personFriendList = friendshipList.stream().map(friendship -> {
            Person personFriend;
            if (friendship.getPersonIdWhoSendFriendship() != personId) {
                personFriend = personService.findPersonById(friendship.getPersonIdWhoSendFriendship());
            }
            else if (friendship.getPersonIdWhoTakeFriendship() != personId) {
                personFriend = personService.findPersonById(friendship.getPersonIdWhoTakeFriendship());
            }
            else throw new BadRequestException400();
            return personFriend;
        }).collect(toList());

        return new ListResponseDto((long) personFriendList.size(), offset, itemPerPage, personFriendList);
    }
}
