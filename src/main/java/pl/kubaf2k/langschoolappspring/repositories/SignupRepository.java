package pl.kubaf2k.langschoolappspring.repositories;

import org.springframework.data.repository.CrudRepository;
import pl.kubaf2k.langschoolappspring.models.CourseSignup;

public interface SignupRepository extends CrudRepository<CourseSignup, Integer> {
}
