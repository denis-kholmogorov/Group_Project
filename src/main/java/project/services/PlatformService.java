package project.services;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project.models.Language;
import project.repositories.LanguageRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@AllArgsConstructor
public class PlatformService {

    private LanguageRepository languageRepository;

    public List<Language> getLanguages(Integer offset, Integer limit) {
        Pageable pageable = PageRequest.of(offset, limit);
        Iterable<Language> iterable = languageRepository.findAll(pageable);
        return StreamSupport.stream(iterable.spliterator(), false).collect(Collectors.toList());
    }

    public List<Language> getLanguages(String language, Integer offset, Integer limit) {
        Pageable pageable = PageRequest.of(offset, limit);
        Iterable<Language> iterable = languageRepository.findByLanguageContaining(language, pageable);
        return StreamSupport.stream(iterable.spliterator(), false).collect(Collectors.toList());
    }

    public long languageCount() {
        return languageRepository.count();
    }

    public long languageCount(String language) {
        return languageRepository.countByLanguageContaining(language);
    }
}