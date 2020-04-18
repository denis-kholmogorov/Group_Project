package project.controllers.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import project.dto.requestDto.LoginRequestDto;

import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
class ApiAuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private static final ObjectMapper om = new ObjectMapper();

    @Test
    void login() throws Exception {
        LoginRequestDto dto = new LoginRequestDto("ilyxa043@gmail.com", "qweasdzxc");
        String json = om.writeValueAsString(dto);
        System.out.println(json);

        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id", is(1)));
    }

    @Test
    @WithMockUser("ROLE_USER")
    void logout() throws Exception {
        mockMvc.perform(post("/api/v1/auth/logout")
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization",
                        "eyJhbGciOiJIUzI1NiJ9" +
                        ".eyJzdWIiOiJpbHl4YTA0M0BnbWFpbC5jb20iLCJpYXQiOjE1ODcxMTY0NzYsImV4cCI6MTU5MDcxNjQ3Nn0" +
                        ".puucO74gPBqV5OOFhBbafLnLFWrDC6igrGZdeBKJ8NQ"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$data.message", is("ok")));
    }
}