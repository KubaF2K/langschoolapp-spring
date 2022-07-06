package pl.kubaf2k.langschoolappspring.repositories;

import org.springframework.data.repository.CrudRepository;
import pl.kubaf2k.langschoolappspring.models.CourseStatus;

public interface StatusRepository extends CrudRepository<CourseStatus, Integer> {
}
