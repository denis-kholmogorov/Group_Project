package project.services;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import project.models.Tag;
import project.repositories.TagRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Sql(value = {"/delete.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/insert.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/delete.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@RunWith(SpringRunner.class)
@SpringBootTest()
@ActiveProfiles("dev")
@TestPropertySource("/test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class TagServiceTest {

    @Autowired
    private TagService tagService;

    @Autowired
    private TagRepository tagRepository;

    @Test
    void getAllTags() {
        List<Tag> list = tagService.getAllTags("tag",0,10);
        assertEquals(list.size(), 2);
    }

    @Test
    void saveTag() {
        tagService.saveTag("newTag");
        Tag tag = tagRepository.findByTag("newTag").orElse(null);
        assertEquals(tag.getTag(), "newTag");
    }

    @Test
    void findByTagName() {
        Tag tag = tagService.findByTagName("tag1");
        assertEquals(tag.getTag(), "tag1");
    }

    @Test
    void deleteTag() {
//        tagService.deleteTag(200);
//        Tag tag = tagRepository.findById(200).orElse(null);
//        assertNull(tag);
    }
}