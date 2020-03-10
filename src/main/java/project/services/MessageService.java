package project.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import project.repositories.MessageRepository;

@Service
@AllArgsConstructor
public class MessageService {
    private MessageRepository messageRepository;
}
