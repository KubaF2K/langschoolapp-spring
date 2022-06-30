package pl.kubaf2k.langschoolappspring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kubaf2k.langschoolappspring.models.Language;
import pl.kubaf2k.langschoolappspring.models.Role;
import pl.kubaf2k.langschoolappspring.models.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByName(String name);
    List<User> findAllByRolesContaining(Role role);
    List<User> findAllByLanguageAndRolesContaining(Language language, Role role);
}
