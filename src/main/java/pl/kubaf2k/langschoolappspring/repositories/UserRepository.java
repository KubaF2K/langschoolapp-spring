package pl.kubaf2k.langschoolappspring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kubaf2k.langschoolappspring.models.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByName(String name);
}
