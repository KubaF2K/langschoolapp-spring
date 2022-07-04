package pl.kubaf2k.langschoolappspring.repositories;

import org.springframework.data.repository.CrudRepository;
import pl.kubaf2k.langschoolappspring.models.Language;

import java.util.Optional;

public interface LanguageRepository extends CrudRepository<Language, Integer> {
    Optional<Language> findByName(String name);

    Optional<Language> findByCode(String code);
}
