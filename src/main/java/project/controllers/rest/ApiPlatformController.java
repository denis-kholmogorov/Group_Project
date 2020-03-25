package project.controllers.rest;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import project.dto.responseDto.ListResponseDto;
import project.models.Language;
import project.services.PlatformService;

import javax.validation.constraints.Max;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/platform/")
@AllArgsConstructor
@Validated
public class ApiPlatformController {

    private PlatformService platformService;

    @GetMapping("languages")
    ResponseEntity<?> languages(@RequestParam(required = false) String name,
                                @RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer offset,
                                @RequestParam(required = false, defaultValue = "20") @Positive @Max(value = 20) Integer itemPerPage) {
        long total;
        List<Language> list;
        if (name == null || name.isEmpty()) {
            total = platformService.languageCount();
            list = platformService.getLanguages(offset, itemPerPage);
        } else {
            total = platformService.languageCount(name);
            list = platformService.getLanguages(name, offset, itemPerPage);
        }

        return ResponseEntity.ok(new ListResponseDto<>(Long.valueOf(total).intValue(), offset, itemPerPage, list));
    }
}