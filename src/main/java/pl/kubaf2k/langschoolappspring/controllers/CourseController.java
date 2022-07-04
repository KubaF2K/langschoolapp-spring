package pl.kubaf2k.langschoolappspring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.kubaf2k.langschoolappspring.models.Course;
import pl.kubaf2k.langschoolappspring.models.Language;
import pl.kubaf2k.langschoolappspring.models.Role;
import pl.kubaf2k.langschoolappspring.models.User;
import pl.kubaf2k.langschoolappspring.repositories.CourseRepository;
import pl.kubaf2k.langschoolappspring.repositories.LanguageRepository;
import pl.kubaf2k.langschoolappspring.repositories.RoleRepository;
import pl.kubaf2k.langschoolappspring.repositories.UserRepository;
import pl.kubaf2k.langschoolappspring.services.LangschoolUserDetails;
import pl.kubaf2k.langschoolappspring.validators.BasicInfo;
import pl.kubaf2k.langschoolappspring.validators.CourseValidator;

import java.util.Optional;

@Controller
public class CourseController {

    private final CourseRepository courseRepository;
    private final LanguageRepository languageRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final Role adminRole;
    private final Role teacherRole;

    @Autowired
    public CourseController(CourseRepository courseRepository,
                            LanguageRepository languageRepository,
                            UserRepository userRepository,
                            RoleRepository roleRepository) {
        this.courseRepository = courseRepository;
        this.languageRepository = languageRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        adminRole = roleRepository.findByName("ROLE_ADMIN");
        teacherRole = roleRepository.findByName("ROLE_TEACHER");
    }

    @GetMapping("/courses")
    public String index(Model model) {
        Iterable<Language> languages = languageRepository.findAll();

        model.addAttribute("languages", languages);
        return "courses/index";
    }

    @GetMapping("/courses/add")
    public String add(Model model) {
        Iterable<Language> languages = languageRepository.findAll();
        Iterable<User> teachers = userRepository.findAllByRolesContaining(teacherRole);

        model.addAttribute("languages", languages);
        model.addAttribute("teachers", teachers);
        if (!model.containsAttribute("course"))
            model.addAttribute("course", new Course());
        return "courses/add";
    }

//    TODO validation
    @PostMapping("/courses/add")
    public String create(@ModelAttribute @Validated(BasicInfo.class) Course course,
                         @RequestParam(name = "language_id") int languageId,
                         @RequestParam(name = "teacher_id") int teacherId,
                         Model model,
                         RedirectAttributes redirectAttributes,
                         BindingResult result,
                         @AuthenticationPrincipal LangschoolUserDetails userDetails) {
        Optional<Language> language = languageRepository.findById(languageId);
        Optional<User> teacher = userRepository.findById(teacherId);

        if (language.isPresent())
            course.setLanguage(language.get());
        else
            result.rejectValue("language", "Podany język nie istnieje!");
        if (teacher.isPresent())
            course.setTeacher(teacher.get());
        else
            result.rejectValue("teacher", "Podany prowadzący nie istnieje!");

        CourseValidator validator = new CourseValidator(userDetails.getUser(), teacherRole, adminRole);
        validator.validate(course, result);

        //TODO returns 400 bad request instead of redirect
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.course", result);
            redirectAttributes.addFlashAttribute("course", course);
            return "redirect:/courses/add";
        }

        courseRepository.save(course);
        redirectAttributes.addFlashAttribute("msg", "Utworzono kurs!");
        return "redirect:/courses";
    }

    @GetMapping("/courses/{id}/edit")
    public String edit(@PathVariable int id, Model model, LangschoolUserDetails userDetails) {
        Course course = courseRepository.findById(id).orElseThrow();

        if(!userDetails.getUser().getRoles().contains(adminRole) && course.getTeacher() != userDetails.getUser())
            throw new AccessDeniedException("Brak uprawnień na edycję cudzych kursów");

        Iterable<User> teachers = userRepository.findAllByLanguageAndRolesContaining(course.getLanguage(), teacherRole);

        model.addAttribute("teachers", teachers);
        model.addAttribute("course", course);
        return "courses/edit";
    }

    //TODO Validation
    @PostMapping("/courses/edit")
    public String update(@ModelAttribute Course course,
                         @RequestParam(name = "teacher_id") int teacherId,
                         Model model,
                         RedirectAttributes redirectAttributes,
                         BindingResult result,
                         LangschoolUserDetails userDetails) {
        if (!userDetails.getUser().getRoles().contains(adminRole) &&
                userDetails.getUser().getRoles().contains(teacherRole) &&
                teacherId != userDetails.getUser().getId())
                result.rejectValue("teacher_id", "Brak uprawnień na edycję kursu innego prowadzącego!");

        course.setTeacher(userRepository.findById(teacherId).orElseThrow());
        course.setLanguage(courseRepository.findById(course.getId()).orElseThrow().getLanguage());

        courseRepository.save(course);
        redirectAttributes.addFlashAttribute("msg", "Zedytowano kurs!");
        return "redirect:/courses";
    }

    @PostMapping("/courses/delete")
    public String delete(@RequestParam(name = "id") int id,
                         Model model,
                         RedirectAttributes redirectAttributes) {

        courseRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("msg", "Usunięto kurs!");
        return "redirect:/courses";
    }
}
