package project.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.models.Language;

import java.util.List;

@Repository
public interface LanguageRepository extends JpaRepository<Language, Integer> {

    List<Language> findByLanguageContaining(String language, Pageable pageable);

    long countByLanguageContaining(String language);
}