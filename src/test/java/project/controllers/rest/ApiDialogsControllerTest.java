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
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import project.dto.dialog.request.CreateDialogDto;
import project.dto.dialog.request.MessageRequestDto;
import project.models.enums.ReadStatus;
import project.security.TokenProvider;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


//@Sql(value = {"/insert.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
//@Sql(value = {"/delete.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
@WithMockUser("ROLE_USER")
@TestPropertySource("/application_test.properties")
class ApiDialogsControllerTest {

    @Autowired
    private MockMvc mvc;

    private final String token;

    private static final ObjectMapper om = new ObjectMapper();

    @Autowired
    public ApiDialogsControllerTest(TokenProvider tokenProvider) {
        token = tokenProvider.createToken("test1@mail.ru");
    }

    @Test
    //@Sql(scripts = "classpath:/delete.sql")
    @Sql(scripts = {"classpath:/insert.sql"},
            config = @SqlConfig(encoding = "utf-8",
                    transactionMode = SqlConfig.TransactionMode.ISOLATED),
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void getAllDialogs() throws Exception {
        System.out.println(token);

        mvc.perform(get("/api/v1/dialogs/")
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total",is(1)))
                .andExpect(jsonPath("$.offset",is(0)))
                .andExpect(jsonPath("$.perPage",is(20)))
                .andExpect(jsonPath("$.data[0].id", is(1)))
                .andExpect(jsonPath("$.data[0].unread_count", is(1)))
                .andExpect(jsonPath("$.data[0].last_message.author.email", is("test1@mail.ru")))
                .andExpect(jsonPath("$.data[0].last_message.recipient.email", is("test2@mail.ru")))
                .andExpect(jsonPath("$.data[0].last_message.message_text", is("hello world")));
    }

    @Test
    void createDialog()  throws Exception{

        List<Integer> list = new ArrayList<>(1);
        list.add(4);
        CreateDialogDto dto = new CreateDialogDto(list);
        System.out.println(om.writeValueAsString(dto));

        mvc.perform(post("/api/v1/dialogs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(dto))
                .header("Authorization",token)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.data.id",is(3)));
    }


    @Test
    @WithMockUser("ROLE_USER")
    void countSentMessage() throws Exception {

        mvc.perform(get("/api/v1/dialogs/unreaded") .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.count", is(1)));
    }

    @Test
    void getDialogMessages() throws Exception {
        mvc.perform(get("/api/v1/dialogs/1/messages").accept(MediaType.APPLICATION_JSON)
                .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total",is(1)))
                .andExpect(jsonPath("$.offset",is(0)))
                .andExpect(jsonPath("$.perPage",is(20)))
                .andExpect(jsonPath("$.data.length()", is(1)))
                .andExpect(jsonPath("$.data[0].id", is(13)))
                .andExpect(jsonPath("$.data[0].author.email", is("test1@mail.ru")))
                .andExpect(jsonPath("$.data[0].recipient.email", is("test2@mail.ru")))
                .andExpect(jsonPath("$.data[0].isSentByMe", is(false)))
                .andExpect(jsonPath("$.data[0].read_status", is(ReadStatus.SENT.toString())));
    }

    @Test
    void sentMessage() throws Exception {

        MessageRequestDto dto = new MessageRequestDto("Haba haba");

        mvc.perform(post("/api/v1/dialogs/1/messages")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(dto))//"{\"message_text\":\"Haba haba\"}")
                .header("Authorization",token)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.data.recipientId", is(3)))
                .andExpect(jsonPath("$.data.authorId", is(1)))
                .andExpect(jsonPath("$.data.messageText", is("Haba haba")))
                .andExpect(jsonPath("$.data.readStatus", is("SENT")));

    }

    @Test
    void readMessage() {
    }
}


    /*@Test
    void getAllDialogs() throws Exception {
        mvc.perform(get("/api/v1/dialogs/")
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total",is(1)))
                .andExpect(jsonPath("$.offset",is(0)))
                .andExpect(jsonPath("$.perPage",is(20)))
                .andExpect(jsonPath("$.data[0].id", is(12)))
                .andExpect(jsonPath("$.data[0].unread_count", is(2)))
                .andExpect(jsonPath("$.data[0].last_message.author.email", is("zubayr_@live.com")))
                .andExpect(jsonPath("$.data[0].last_message.recipient.email", is("bot@mail.ru")))
                .andExpect(jsonPath("$.data[0].last_message.message_text", is("hi")));
    }

    @Test
    void createDialog()  throws Exception{

        List<Integer> list = new ArrayList<>(1);
        list.add(1);
        CreateDialogDto dto = new CreateDialogDto(list);
        System.out.println(om.writeValueAsString(dto));

        mvc.perform(post("/api/v1/dialogs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(dto))
                .header("Authorization","eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0MUBtYWlsLnJ1IiwiaWF0IjoxNTg3MDY5MDAwLCJleHAiOjE1ODc0MjkwMDB9.dCKyqLhoCkF5g7nmag5VZadez-vvGV8otVSW5SThsS8")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.data.id",is(1)));
    }


    @Test
    void countSentMessage() throws Exception {

        mvc.perform(get("/api/v1/dialogs/unreaded") .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.count", is(1)));
    }

    @Test
    void getDialogMessages() throws Exception {
        mvc.perform(get("/api/v1/dialogs/12/messages").accept(MediaType.APPLICATION_JSON)
                .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total",is(2)))
                .andExpect(jsonPath("$.offset",is(0)))
                .andExpect(jsonPath("$.perPage",is(20)))
                .andExpect(jsonPath("$.data.length()", is(2)))
                .andExpect(jsonPath("$.data[0].id", is(13)))
                .andExpect(jsonPath("$.data[0].time",is(1587023027000L)))
                .andExpect(jsonPath("$.data[0].author.email", is("bot@mail.ru")))
                .andExpect(jsonPath("$.data[0].recipient.email", is("zubayr_@live.com")))
                .andExpect(jsonPath("$.data[0].isSentByMe", is(false)))
                .andExpect(jsonPath("$.data[0].read_status", is(ReadStatus.SENT.toString())));
    }

    @Test
    void sentMessage() throws Exception {

        MessageRequestDto dto = new MessageRequestDto("Haba haba");

        mvc.perform(post("/api/v1/dialogs/1/messages")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(dto))//"{\"message_text\":\"Haba haba\"}")
                .header("Authorization",token)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.data.recipientId", is(1)))
                .andExpect(jsonPath("$.data.authorId", is(2)))
                .andExpect(jsonPath("$.data.messageText", is("Haba haba")))
                .andExpect(jsonPath("$.data.readStatus", is("SENT")));

    }

    @Test
    void readMessage() {
    }*/


