package pl.kubaf2k.langschoolappspring.repositories;

import org.springframework.data.repository.CrudRepository;
import pl.kubaf2k.langschoolappspring.models.Course;

import java.util.Optional;

public interface CourseRepository extends CrudRepository<Course, Integer> {
    Iterable<Course> findByLanguageId(int languageId);
    Optional<Course> findByName(String name);
    boolean existsByName(String name);
    int countByName(String name);
}
