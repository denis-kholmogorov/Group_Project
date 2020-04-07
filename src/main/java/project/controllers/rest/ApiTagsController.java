package project.controllers.rest;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.dto.TagDto;
import project.dto.requestDto.AddTag;
import project.dto.responseDto.ListResponseDto;
import project.dto.responseDto.MessageResponseDto;
import project.dto.responseDto.ResponseDto;
import project.models.Tag;
import project.services.TagService;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping(value = "/api/v1/tags/")
@AllArgsConstructor
public class ApiTagsController {

    private TagService tagService;

    @GetMapping
    public ResponseEntity<?> getTags(@RequestParam String query,
                                     @RequestParam(required = false) Integer offsetParam,
                                     @RequestParam(required = false) Integer limitParam)
    {
        List<Tag> tags = tagService.getAllTags(query, offsetParam, limitParam);

        List<ResponseDto<TagDto>> tagsResponseDtoList = tags.stream().map(tag -> new TagDto(tag.getId(), tag.getTag()))
                .map(ResponseDto::new).collect(toList());

        ListResponseDto<ResponseDto<TagDto>> tagListResponseDto = new ListResponseDto<>
                ((long)tagsResponseDtoList.size(), offsetParam, limitParam, tagsResponseDtoList);

        return ResponseEntity.ok(tagListResponseDto);
    }

    @PostMapping
    public ResponseEntity<?> addTag(@RequestBody AddTag tagName){

        TagDto tagDto = tagService.createTag(tagName.getTag());

        return ResponseEntity.ok(new ResponseDto<>(tagDto));
    }

    @DeleteMapping
    public ResponseEntity<?> deleteTag(@RequestParam Integer tagId){
        tagService.deleteTag(tagId);
        return ResponseEntity.ok(new ResponseDto<>(new MessageResponseDto()));
    }
}