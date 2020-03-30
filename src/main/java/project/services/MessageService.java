package project.services;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import project.dto.requestDto.OffsetLimitDto;
import project.handlerExceptions.BadRequestException400;
import project.models.Message;
import project.models.Person;
import project.repositories.MessageRepository;
import project.repositories.PersonRepository;
import project.security.TokenProvider;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

    private MessageRepository messageRepository;

    private TokenProvider tokenProvider;

    private PersonRepository personRepository;

    public ResponseEntity getAllMessages(String query, OffsetLimitDto dto, HttpServletRequest request) throws BadRequestException400 {

        String email = tokenProvider.getUserEmail(tokenProvider.resolveToken(request));
        Optional<Person> personOptional = personRepository.findPersonByEmail(email);
        if(!personOptional.isPresent()) throw new BadRequestException400();
        Person person = personOptional.get();

        List<Message> messageList = messageRepository.findAllByAuthorIdOrRecipientId(person.getId());
        return null;
    }
}
