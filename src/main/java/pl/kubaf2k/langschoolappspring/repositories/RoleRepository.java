package pl.kubaf2k.langschoolappspring.repositories;

import org.springframework.data.repository.CrudRepository;
import pl.kubaf2k.langschoolappspring.models.Role;

import java.util.Optional;

public interface RoleRepository extends CrudRepository<Role, Integer> {
    Optional<Role> findByName(String name);
}
