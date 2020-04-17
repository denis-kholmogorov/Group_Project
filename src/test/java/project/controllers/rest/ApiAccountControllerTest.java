package project.controllers.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import project.dto.requestDto.RegistrationRequestDto;
import project.services.email.EmailServiceImpl;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ApiAccountControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    private static final String basePath = "/api/v1/account/";

    @MockBean
    private EmailServiceImpl emailServiceImpl;

    @Test
    @Order(1)
    public void registerSuccessTest() throws Exception {
        RegistrationRequestDto requestContent = new RegistrationRequestDto(
            "newuser@gmail.com",
            "12345678",
            "12345678",
            "user",
            "new",
            "3675");

        mvc.perform(
            post(basePath + "register")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(requestContent)))
            .andExpect(getExpects());
    }

    @Test
    @Order(2)
    @Sql(scripts = "/api-account-controller-after.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void sendRecoveryEmailSuccessTest() throws Exception {
        mvc.perform(
            put(basePath + "password/recovery")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"newuser@gmail.com\"}"))
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
