package project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project.dto.TagDto;
import project.models.Tag;
import project.repositories.TagRepository;

import java.util.List;

@Service
public class TagService {

    private TagRepository tagRepository;

    @Autowired
    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }


    public List<Tag> getAllTags(String tagName, Integer offset, Integer limit){

        Pageable pageable = PageRequest.of(offset, limit);

        return tagRepository.findAllByTagContaining(tagName, pageable);
    }

    public TagDto createTag(String tagName){
        Tag tag = new Tag();
        tag.setTag(tagName);
        tagRepository.save(tag);

        return new TagDto(tag.getId(), tag.getTag());
    }

    public void deleteTag(Integer tagId){
        tagRepository.deleteById(tagId);
    }
}
