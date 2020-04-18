package project.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import project.dto.requestDto.LoginRequestDto;
import project.dto.responseDto.ResponseDto;
import project.handlerExceptions.BadRequestException400;
import project.repositories.PersonRepository;
import project.security.TokenProvider;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PersonServiceTest {

    @Autowired
    private PersonService personService;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private TokenProvider tokenProvider;

    private static final ObjectMapper om = new ObjectMapper();

    @Test
    public void login() throws Exception {
        LoginRequestDto loginRequestDto = new LoginRequestDto("ilyxa043@gmail.com", "qweasdzxc");

        ResponseDto responseDto = personService.login(loginRequestDto);

        String json = om.writeValueAsString(responseDto);


    }

    @Test(expected = BadRequestException400.class)
    public void loginError() {
        LoginRequestDto loginRequestDto = new LoginRequestDto("ilyxa@gmail.com", "qweasdzxc");

        ResponseDto responseDto = personService.login(loginRequestDto);
    }
}