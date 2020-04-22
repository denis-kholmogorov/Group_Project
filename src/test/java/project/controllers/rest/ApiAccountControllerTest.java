package project.controllers.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import project.dto.requestDto.PasswordSetDto;
import project.dto.requestDto.RegistrationRequestDto;
import project.models.Person;
import project.models.VerificationToken;
import project.repositories.PersonRepository;
import project.repositories.VerificationTokenRepository;
import project.services.email.EmailServiceImpl;

import java.util.NoSuchElementException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
@TestPropertySource("/test.properties")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ApiAccountControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private PersonRepository personRepository;

    private static final String BASE_PATH = "/api/v1/account/";
    private static final String EMAIL = "newuser@gmail.com";
    private static final String PASSWORD = "12345678";

    @Value("${response.host}")
    private String host;

    @MockBean
    private EmailServiceImpl emailServiceImpl;

    @Test
    @Order(1)
    public void registerSuccessTest() throws Exception {
        RegistrationRequestDto requestContent = new RegistrationRequestDto(
            EMAIL,
            PASSWORD,
            PASSWORD,
            "user",
            "new",
            "3675");

        mvc.perform(
            post(BASE_PATH + "register")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(requestContent)))
            .andExpect(getExpects());
    }

    @Test
    @Order(2)
    public void sendRecoveryEmailSuccessTest() throws Exception {
        mvc.perform(
            put(BASE_PATH + "password/recovery")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"" + EMAIL + "\"}"))
            .andExpect(getExpects());
    }

    @Test
    @Order(3)
    @Sql(scripts = "/api-account-controller-after.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void setNewPasswordSuccessTest() throws Exception {
        Person person = personRepository.findPersonByEmail(EMAIL).orElseThrow(
            () -> new IllegalArgumentException("There is no email: " + EMAIL + " in database")
        );

        String token = "";
        for (VerificationToken verificationToken : verificationTokenRepository.findAll()) {
            if (verificationToken.getUserId() == person.getId()) {
                token = verificationToken.getUUID();
                break;
            }
        }

        if (token.isEmpty())
            throw new NoSuchElementException("Token not found for email: " + EMAIL);

        PasswordSetDto requestContent = new PasswordSetDto(token, PASSWORD);

        mvc.perform(
            put(BASE_PATH + "password/set")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("referer", "http://" + host + "/change-password?token=" + token)
                .content(mapper.writeValueAsString(requestContent)))
            .andExpect(getExpects());
    }

    private ResultMatcher getExpects() {
        return ResultMatcher.matchAll(
            status().isOk(),
            jsonPath("$.error").isEmpty(),
            jsonPath("$.timestamp").isNumber(),
            jsonPath("$.data.message").value("ok")
        );
    }
}
