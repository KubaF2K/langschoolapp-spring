package pl.kubaf2k.langschoolappspring.validators;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import pl.kubaf2k.langschoolappspring.models.Course;
import pl.kubaf2k.langschoolappspring.models.Role;
import pl.kubaf2k.langschoolappspring.models.User;
import pl.kubaf2k.langschoolappspring.repositories.CourseRepository;

import javax.validation.Valid;

public class CourseValidator implements Validator {
    private final User user;
    private final Role teacher;
    private final Role admin;
    private final CourseRepository courseRepository;

    public CourseValidator(User user, Role teacher, Role admin, CourseRepository courseRepository) {
        this.user = user;
        this.teacher = teacher;
        this.admin = admin;
        this.courseRepository = courseRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Course.class.equals(clazz);
    }

    @Override
    @Transactional
    public void validate(Object target, Errors errors) {
        Course course = (Course) target;

        if (course.getId() != 0) {
            if (courseRepository.countByName(course.getName()) > 1)
                errors.rejectValue("name", "error.name.taken", "podana nazwa już istnieje");
            else {
                var dbCourse = courseRepository.findByName(course.getName());
                if (dbCourse.isPresent() && dbCourse.get().getId() != course.getId())
                    errors.rejectValue("name", "error.name.taken", "podana nazwa już istnieje");
            }
        } else if (courseRepository.existsByName(course.getName()))
            errors.rejectValue("name", "error.name.taken", "podana nazwa już istnieje");

        if (!course.getTeacher().hasRole(teacher))
            errors.rejectValue("teacher", "error.teacher.not_teacher" ,"podany użytkownik nie jest prowadzącym");
        if (course.getTeacher().getLanguage().getId() != course.getLanguage().getId())
            errors.rejectValue("language", "error.teacher.wrong_language" ,"podany prowadzący nie uczy podanego języka");
        if (user.hasRole(admin))
            return;
        if (course.getTeacher().getId() != user.getId())
            errors.rejectValue("teacher", "error.teacher.unauthorized" ,"brak uprawnień na edycję cudzego kursu");
        if (course.getLanguage().getId() != user.getLanguage().getId())
            errors.rejectValue("language", "error.language.unauthorized" ,"brak uprawnień na tworzenie kursu w innym języku");
    }
}
