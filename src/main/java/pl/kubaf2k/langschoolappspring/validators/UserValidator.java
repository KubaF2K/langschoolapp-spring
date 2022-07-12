package pl.kubaf2k.langschoolappspring.validators;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import pl.kubaf2k.langschoolappspring.models.Role;
import pl.kubaf2k.langschoolappspring.models.User;
import pl.kubaf2k.langschoolappspring.repositories.UserRepository;

import java.util.Objects;

public class UserValidator implements Validator {
    private final User editor;
    private final UserRepository userRepository;
    private final Role teacher;
    private final Role admin;

    public UserValidator(User user, Role teacher, Role admin, UserRepository userRepository) {
        editor = user;
        this.teacher = teacher;
        this.admin = admin;
        this.userRepository = userRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        var user = (User) target;

        var nameTestUser = userRepository.findByName(user.getName());
        if (nameTestUser.isPresent() && (nameTestUser.get().getId() != user.getId()))
            errors.rejectValue("name", "error.name.taken", "podana nazwa użytkownika jest już zajęta");

        var emailTestUser = userRepository.findByEmail(user.getEmail());
        if (emailTestUser.isPresent() && (emailTestUser.get().getId() != user.getId()))
            errors.rejectValue("email", "error.email.taken", "istnieje już użytkownik o podanym email");

        if (!editor.hasRole(admin) && (user.getId() != editor.getId()))
            errors.rejectValue("id", "error.user.unauthorized", "brak uprawnień na edycję innego użytkownika");
    }
}
