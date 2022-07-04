package pl.kubaf2k.langschoolappspring.validators;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import pl.kubaf2k.langschoolappspring.models.Course;
import pl.kubaf2k.langschoolappspring.models.Role;
import pl.kubaf2k.langschoolappspring.models.User;

import javax.validation.Valid;

public class CourseValidator implements Validator {
    private final User user;
    private final Role teacher;
    private final Role admin;

    public CourseValidator(User user, Role teacher, Role admin) {
        this.user = user;
        this.teacher = teacher;
        this.admin = admin;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Course.class.equals(clazz);
    }

    @Override
    @Transactional
    public void validate(Object target, Errors errors) {
        Course course = (Course) target;
        if (!course.getTeacher().hasRole(teacher))
            errors.rejectValue("teacher", "Podany użytkownik nie jest prowadzącym!");
        if (course.getTeacher().getLanguage().getId() != course.getLanguage().getId())
            errors.rejectValue("language", "Podany prowadzący nie uczy podanego języka!");
        if (user.hasRole(admin))
            return;
        if (course.getTeacher().getId() != user.getId())
            errors.rejectValue("teacher", "Brak uprawnień na edycję cudzego kursu!");
        if (course.getLanguage().getId() != user.getLanguage().getId())
            errors.rejectValue("language", "Brak uprawnień na tworzenie kursu w innym języku!");
    }
}
