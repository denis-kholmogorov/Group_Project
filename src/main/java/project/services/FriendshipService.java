package project.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import project.repositories.FriendshipRepository;

@Service
@AllArgsConstructor
public class FriendshipService {
    private FriendshipRepository friendshipRepository;
}
