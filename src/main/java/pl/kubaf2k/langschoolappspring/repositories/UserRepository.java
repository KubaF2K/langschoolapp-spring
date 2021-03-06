package pl.kubaf2k.langschoolappspring.repositories;

import org.springframework.data.repository.CrudRepository;
import pl.kubaf2k.langschoolappspring.models.Language;
import pl.kubaf2k.langschoolappspring.models.Role;
import pl.kubaf2k.langschoolappspring.models.User;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Integer> {
    Optional<User> findByName(String name);
    Optional<User> findByEmail(String email);
    Iterable<User> findAllByRolesContaining(Role role);
    Iterable<User> findAllByLanguageAndRolesContaining(Language language, Role role);
}
