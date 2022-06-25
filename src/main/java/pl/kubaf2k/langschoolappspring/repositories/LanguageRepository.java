package pl.kubaf2k.langschoolappspring.repositories;

import org.springframework.data.repository.CrudRepository;
import pl.kubaf2k.langschoolappspring.models.Language;

public interface LanguageRepository extends CrudRepository<Language, Integer> {
    Language findByName(String name);

    Language findByCode(String code);
}
