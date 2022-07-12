package pl.kubaf2k.langschoolappspring.controllers;

import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.kubaf2k.langschoolappspring.models.User;
import pl.kubaf2k.langschoolappspring.repositories.RoleRepository;
import pl.kubaf2k.langschoolappspring.repositories.UserRepository;
import pl.kubaf2k.langschoolappspring.services.LangschoolUserDetails;
import pl.kubaf2k.langschoolappspring.validators.BasicInfo;
import pl.kubaf2k.langschoolappspring.validators.UserValidator;

import javax.validation.Valid;
import java.util.Objects;

@Controller
public class UserController {

//   TODO login register create

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public UserController(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @GetMapping("/user")
    public String index(@AuthenticationPrincipal LangschoolUserDetails userDetails,
                        Model model) {
        var user = userDetails.getUser();

        model.addAttribute(user);

        return "user/index";
    }

    @GetMapping("/user/edit")
    public String edit(Model model,
                       @AuthenticationPrincipal LangschoolUserDetails userDetails) {
        var user = userDetails.getUser();
        if (!model.containsAttribute("user"))
            model.addAttribute(user);

        return "user/edit";
    }

    @PostMapping("/user/edit")
    public String update(@ModelAttribute @Validated(BasicInfo.class) User user,
                         BindingResult result,
                         RedirectAttributes redirectAttributes,
                         @AuthenticationPrincipal LangschoolUserDetails userDetails) {

        var validator = new UserValidator(
                userDetails.getUser(),
                roleRepository.findByName("ROLE_TEACHER").orElseThrow(),
                roleRepository.findByName("ROLE_ADMIN").orElseThrow(),
                userRepository
        );
        validator.validate(user, result);

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.user", result);
            redirectAttributes.addFlashAttribute("user", user);
            return "redirect:/user/edit";
        }

        var originalUser = userRepository.findById(user.getId()).orElseThrow();
        originalUser.setName(user.getName());
        originalUser.setFirstName(user.getFirstName());
        originalUser.setLastName(user.getLastName());
        originalUser.setEmail(user.getEmail());

        userRepository.save(originalUser);
        redirectAttributes.addFlashAttribute("msg", "Zaktualizowano dane użytkownika!");

        if (Objects.equals(userDetails.getUsername(), user.getName()))
            return "redirect:/login?logout";

        return "redirect:/user";
    }

    @GetMapping("/admin")
    public String adminPanel() {
        throw new NotYetImplementedException();
    }

    @GetMapping("/teacher")
    public String teacherPanel() {
        throw new NotYetImplementedException();
    }
}
