package project.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import project.repositories.FriendshipStatusRepository;

@Service
@AllArgsConstructor
public class FriendshipStatusService {
    private FriendshipStatusRepository friendshipStatusRepository;
}
