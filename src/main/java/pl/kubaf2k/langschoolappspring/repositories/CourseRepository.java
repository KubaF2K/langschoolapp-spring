package pl.kubaf2k.langschoolappspring.repositories;

import org.springframework.data.repository.CrudRepository;
import pl.kubaf2k.langschoolappspring.models.Course;

public interface CourseRepository extends CrudRepository<Course, Integer> {
}
