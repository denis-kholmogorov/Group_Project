package project.services;

import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.dto.responseDto.ListResponseDto;
import project.handlerExceptions.BadRequestException400;
import project.models.*;
import project.models.enums.FriendshipStatusCode;
import project.models.enums.NotificationTypeEnum;
import project.repositories.FriendshipRepository;
import project.repositories.NotificationRepository;
import project.repositories.NotificationTypeRepository;
import project.security.TokenProvider;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@Slf4j
public class FriendshipService {

    private FriendshipRepository friendshipRepository;
    private PersonService personService;
    private TokenProvider tokenProvider;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private NotificationTypeRepository notificationTypeRepository;
    @Autowired
    private EntityManager entityManager;

    @Autowired
    public FriendshipService(FriendshipRepository friendshipRepository, PersonService personService, TokenProvider tokenProvider) {
        this.friendshipRepository = friendshipRepository;
        this.personService = personService;
        this.tokenProvider = tokenProvider;
    }

    public void delete(Friendship friendship) {
        friendshipRepository.delete(friendship);
    }

    public void save(Friendship friendship) {
        friendshipRepository.save(friendship);
    }

    public Friendship findByFriendsCouple(Person firstFriend, Person secondFriend){
        return friendshipRepository.findByFriendsCouple(firstFriend, secondFriend).orElse(null);
    }

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


    // Метод Ильи для списка друзей
    public ListResponseDto<Person> getFriendList(String name, Integer offset, Integer itemPerPage, Person person) {

        List<Person> friendList = getFriendsList(person);

        if (name == null) {
            return new ListResponseDto<>((long) friendList.size(), offset, itemPerPage,
                    friendList.subList(offset, Math.min(friendList.size(), itemPerPage)));
        }
        List<Person> filterList = friendList.stream().filter(
                person1 -> person1.getFirstName().contains(name)
                        || person1.getLastName().contains(name))
                .collect(Collectors.toList());
        return new ListResponseDto<>((long) filterList.size(), offset, itemPerPage,
                filterList.subList(offset, Math.min(filterList.size(), itemPerPage)));

        //Sort sort = Sort.by(Sort.Direction.DESC, "time");
        //Pageable pageable = PageRequest.of(offset, itemPerPage);
//        int personId = person.getId();
//        List<Friendship> friendshipList =
//                friendshipRepository.findAllBySrcPersonOrDstPersonAndStatus(
//                        person, pageable);
//
//        List<Person> personFriendList = friendshipList.stream().map(friendship -> {
//            Person personFriend;
//            if (friendship.getSrcPerson().getId() != personId) {
//                personFriend = friendship.getSrcPerson();
//            }
//            else if (friendship.getDstPerson().getId() != personId) {
//                personFriend = friendship.getDstPerson();
//            }
//            else throw new BadRequestException400();
//            return personFriend;
//        }).collect(toList());
//
//        return new ListResponseDto((long) personFriendList.size(), offset, itemPerPage, personFriendList);
    }

    //====================================  FM  ===========================================================

    // Метод получения друзей после добавления связи Person - Friendship; кажется наиболее актуальным,
    // т.к. проверяет только те френдшипы, в которых присутствует данный пользователь
    public List<Person> getFriendsList(Person person){
        List<Person> friends = person.getSentFriendshipRequests().stream().map(friendship -> {
            Person friend = null;
            if (friendship.getStatus().getCode().equals(FriendshipStatusCode.FRIEND)){
                friend = friendship.getDstPerson();
            }
            return friend;
        }).filter(Objects::nonNull).collect(Collectors.toList());

        List<Person> friends1 = person.getReceivedFriendshipRequests().stream().map(friendship -> {
            Person friend = null;
            if (friendship.getStatus().getCode().equals(FriendshipStatusCode.FRIEND)){
                friend = friendship.getSrcPerson();
            }
            return friend;
        }).filter(Objects::nonNull).collect(Collectors.toList());

        friends.addAll(friends1);

        return friends;
    }

    //=======================

    // Метод, возвращающий имеющиеся у пользователя заявки в друзья от других пользователей
    public ListResponseDto getFriendRequests(String name, Integer offset, Integer itemPerPage, Person person){

        List<Person> friendRequests = person.getReceivedFriendshipRequests().stream().map(friendship -> {
            Person friendRequester = null;
            if (friendship.getStatus().getCode().equals(FriendshipStatusCode.REQUEST))
                friendRequester = friendship.getSrcPerson();
            return friendRequester;
        }).filter(Objects::nonNull).collect(Collectors.toList());

        //List<Person> friends = personRepository.findFriendRequests(person); // Старая версия
        return new ListResponseDto<>((long) friendRequests.size(), offset, itemPerPage, friendRequests);
    }

    //=========================

    @Transactional(rollbackFor = Exception.class)
    public void sendFriendshipRequest(Integer id, HttpServletRequest request) throws Exception {

        log.info("trig transac");
        Person src = tokenProvider.getPersonByRequest(request);
        Person dst = personService.findPersonById(id);
        if (findByFriendsCouple(src, dst) == null){

            Friendship friendship = new Friendship();
            FriendshipStatus fs = new FriendshipStatus();
            fs.setCode(FriendshipStatusCode.REQUEST);
            fs.setName(FriendshipStatusCode.REQUEST.getCode2Name());
            fs.setTime(new Date());
            friendship.setSrcPerson(src);
            friendship.setDstPerson(dst);
            friendship.setStatus(fs);
            save(friendship);

            Notification notification = new Notification();
            NotificationType notificationType = notificationTypeRepository.findByCode(NotificationTypeEnum.FRIEND_REQUEST);
            notification.setPerson(dst);
            notification.setContact("Contact");
            notification.setMainEntity(src);
            notification.setNotificationType(notificationType);
            notificationRepository.save(notification);
        } else {
            throw new Exception("Fuck you!");
        }
    }

    public void deleteFriend(Integer id, HttpServletRequest request) {

        Person firstFriend = tokenProvider.getPersonByRequest(request);
        Person secondFriend = personService.findPersonById(id);
        Friendship friendship = findByFriendsCouple(firstFriend, secondFriend);
        if (friendship != null){
            delete(friendship);
        }
    }

    // Метод одобрения заявки в друзья, пока не применить
    public void confirmFriendshipRequest(Person src, Person dst){

        Friendship friendship = findByFriendsCouple(src, dst);
        if (friendship != null) {
            friendship.getStatus().setCode(FriendshipStatusCode.FRIEND);
        }
    }

    //============================================================================================================
}
