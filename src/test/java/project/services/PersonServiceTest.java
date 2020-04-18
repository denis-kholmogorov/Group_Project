package project.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import project.dto.requestDto.LoginRequestDto;
import project.dto.responseDto.PersonDtoWithToken;
import project.dto.responseDto.ResponseDto;
import project.handlerExceptions.BadRequestException400;
import project.models.Person;
import project.repositories.PersonRepository;
import project.security.TokenProvider;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("dev")
@TestPropertySource("/application-test.properties")
public class PersonServiceTest {

    @Autowired
    private PersonService personService;

    @MockBean
    private PersonRepository personRepository;

    private static final ObjectMapper om = new ObjectMapper();

    @Test
    public void login() throws Exception {
        LoginRequestDto loginRequestDto = new LoginRequestDto("ilyxa043@gmail.com", "qweasdzxc");

        Person person = new Person();
        person.setEmail("ilyxa043@gmail.com");
        personRepository.save(person);

        ResponseDto responseDto = personService.login(loginRequestDto);

        //PersonDtoWithToken person = (PersonDtoWithToken) responseDto.getData();
        //ResponseDto<PersonDtoWithToken> person = new ResponseDto<>();

        Mockito.verify(personRepository, Mockito.times(1))
                .findPersonByEmail(loginRequestDto.getEmail());
    }

    @Test(expected = BadRequestException400.class)
    public void loginError() {
        LoginRequestDto loginRequestDto = new LoginRequestDto("ilyxa@gmail.com", "qweasdzxc");

        ResponseDto responseDto = personService.login(loginRequestDto);
    }
}