package pl.kubaf2k.langschoolappspring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.kubaf2k.langschoolappspring.models.Role;
import pl.kubaf2k.langschoolappspring.models.User;
import pl.kubaf2k.langschoolappspring.repositories.CourseRepository;
import pl.kubaf2k.langschoolappspring.repositories.LanguageRepository;
import pl.kubaf2k.langschoolappspring.repositories.RoleRepository;
import pl.kubaf2k.langschoolappspring.repositories.UserRepository;
import pl.kubaf2k.langschoolappspring.services.LangschoolUserDetails;
import pl.kubaf2k.langschoolappspring.validators.BasicInfo;
import pl.kubaf2k.langschoolappspring.validators.UserValidator;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

@Controller
public class UserController {

//   TODO login register create

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final LanguageRepository languageRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserRepository userRepository,
                          RoleRepository roleRepository,
                          LanguageRepository languageRepository,
                          PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.languageRepository = languageRepository;
        this.passwordEncoder = passwordEncoder;
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
                       @RequestParam int id,
                       @AuthenticationPrincipal LangschoolUserDetails userDetails) {
        if (!model.containsAttribute("user") || model.getAttribute("user") == null) {
            User user;
            if (id != 0 && userDetails.getUser().hasRole(roleRepository.findByName("ROLE_ADMIN").orElseThrow()))
                user = userRepository.findById(id).orElseThrow();
            else
                user = userDetails.getUser();
            model.addAttribute(user);
        }
        model.addAttribute("allRoles", roleRepository.findAll());
        model.addAttribute("allLanguages", languageRepository.findAll());

        return "user/edit";
    }

    @PostMapping("/user/edit")
    public String update(@ModelAttribute @Validated(BasicInfo.class) User user,
                         BindingResult result,
                         RedirectAttributes redirectAttributes,
                         @AuthenticationPrincipal LangschoolUserDetails userDetails) {

        var validator = new UserValidator(
                userDetails.getUser(),
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
        originalUser.setRoles(user.getRoles());
        originalUser.setLanguage(user.getLanguage());

        userRepository.save(originalUser);
        redirectAttributes.addFlashAttribute("msg", "Zaktualizowano dane użytkownika!");

        if (Objects.equals(userDetails.getUsername(), user.getName()))
            return "redirect:/login?logout";

        return "redirect:/user";
    }

    @GetMapping("/register")
    public String register(Model model) {
        if (!model.containsAttribute("user"))
            model.addAttribute(new User());

        return "user/register";
    }

    @PostMapping("/register")
    public String create(@ModelAttribute @Valid User user,
                         BindingResult result,
                         RedirectAttributes redirectAttributes,
                         @RequestParam String confirmPassword) {

        if (!Objects.equals(confirmPassword, user.getPassword()))
            result.rejectValue("password", "error.password.confirm_not_matching", "hasła nie są zgodne");

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        var validator = new UserValidator(user, roleRepository.findByName("ROLE_ADMIN").orElseThrow(), userRepository);
        validator.validate(user, result);

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.user", result);
            redirectAttributes.addFlashAttribute("user", user);
            return "redirect:/register";
        }

        List<Role> roles = new LinkedList<>();
        roles.add(roleRepository.findByName("ROLE_USER").orElseThrow());
        user.setRoles(roles);
        userRepository.save(user);
        redirectAttributes.addFlashAttribute("msg", "Zarejestrowano pomyślnie!");

        return "redirect:/login";
    }

    @PostMapping("/user/delete")
    public String delete(@RequestParam int id,
                         @AuthenticationPrincipal LangschoolUserDetails userDetails,
                         HttpServletRequest request) {
        var editor = userRepository.findByName(userDetails.getUsername()).orElseThrow();
        if (!editor.hasRole(roleRepository.findByName("ROLE_ADMIN").orElseThrow()))
            throw new AccessDeniedException("Nie masz uprawnień na wykonanie tej operacji");

        var user = userRepository.findById(id);

        user.ifPresent(userRepository::delete);

        return "redirect:" + request.getHeader("Referer");
    }

    @GetMapping("/user/changePassword")
    public String changePassword() {
        return "user/changePassword";
    }

    @PostMapping("/user/changePassword")
    public String updatePassword(@RequestParam("oldPassword") String oldPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 Model model,
                                 @AuthenticationPrincipal LangschoolUserDetails userDetails,
                                 RedirectAttributes redirectAttributes) {
        if (!passwordEncoder.matches(oldPassword, userDetails.getPassword())) {
            redirectAttributes.addFlashAttribute("errmsg", "Podane hasło się nie zgadza!");
            return "redirect:/user/changePassword";
        }

        var user = userRepository.findByName(userDetails.getUsername()).orElseThrow();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        redirectAttributes.addFlashAttribute("msg", "Zmieniono hasło!");
        return "redirect:/user/edit";
    }
    @GetMapping("/teacher")
    public String teacherPanel(Model model,
                               @AuthenticationPrincipal LangschoolUserDetails userDetails) {
        var user = userRepository.findByName(userDetails.getUsername()).orElseThrow();

        if (!user.hasRole(roleRepository.findByName("ROLE_TEACHER").orElseThrow()))
            throw new NoSuchElementException();

        var courses = user.getTaughtCourses();
        model.addAttribute("courses", courses);

        return "/user/teacherPanel";
    }

    @GetMapping("/admin")
    public String adminPanel(Model model) {
        var users = userRepository.findAll();
        var roles = roleRepository.findAll();
        var languages = languageRepository.findAll();
        model.addAttribute("users", users);
        model.addAttribute("roles", roles);
        model.addAttribute("languages", languages);

        return "/user/adminPanel";
    }

}
