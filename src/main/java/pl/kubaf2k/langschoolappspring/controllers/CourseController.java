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
        adminRole = roleRepository.findByName("ROLE_ADMIN").orElseThrow();
        teacherRole = roleRepository.findByName("ROLE_TEACHER").orElseThrow();
    }

    @GetMapping("/courses")
    public String index(Model model) {
        Iterable<Language> languages = languageRepository.findAll();

        model.addAttribute("languages", languages);
        return "courses/index";
    }

    @GetMapping("/courses/add")
    public String add(Model model) {
        var languages = languageRepository.findAll();
        var teachers = userRepository.findAllByRolesContaining(teacherRole);

        model.addAttribute("languages", languages);
        model.addAttribute("teachers", teachers);
        if (!model.containsAttribute("course"))
            model.addAttribute("course", new Course());
        return "courses/add";
    }

//    TODO validation
    @PostMapping("/courses/add")
    public String create(@ModelAttribute @Validated(BasicInfo.class) Course course,
                         BindingResult result,
                         @RequestParam(name = "language_id") int languageId,
                         @RequestParam(name = "teacher_id") int teacherId,
                         Model model,
                         RedirectAttributes redirectAttributes,
                         @AuthenticationPrincipal LangschoolUserDetails userDetails) {
        var language = languageRepository.findById(languageId);
        var teacher = userRepository.findById(teacherId);

        if (language.isPresent())
            course.setLanguage(language.get());
        else
            result.rejectValue("language", "error.language.not_exists", "podany język nie istnieje");
        if (teacher.isPresent())
            course.setTeacher(teacher.get());
        else
            result.rejectValue("teacher", "error.teacher.not_exists", "podany prowadzący nie istnieje");

        var validator = new CourseValidator(userDetails.getUser(), teacherRole, adminRole, courseRepository);
        validator.validate(course, result);

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
    public String edit(@PathVariable int id, Model model, @AuthenticationPrincipal LangschoolUserDetails userDetails) {
        var course = courseRepository.findById(id).orElseThrow();

        if(!userDetails.getUser().hasRole(adminRole) && course.getTeacher().getId() != userDetails.getUser().getId())
            throw new AccessDeniedException("Brak uprawnień na edycję cudzych kursów");

        var teachers = userRepository.findAllByLanguageAndRolesContaining(course.getLanguage(), teacherRole);

        model.addAttribute("teachers", teachers);
        if (!model.containsAttribute("course"))
            model.addAttribute("course", course);

        return "courses/edit";
    }

    //TODO Validation
    @PostMapping("/courses/edit")
    public String update(@ModelAttribute @Validated(BasicInfo.class) Course course,
                         BindingResult result,
                         @RequestParam(name = "teacher_id") int teacherId,
                         Model model,
                         RedirectAttributes redirectAttributes,
                         @AuthenticationPrincipal LangschoolUserDetails userDetails) {
        Optional<User> teacher = userRepository.findById(teacherId);

        if (teacher.isPresent())
            course.setTeacher(teacher.get());
        else
            result.rejectValue("teacher", "error.teacher.not_exists", "podany prowadzący nie istnieje");

        var validator = new CourseValidator(userDetails.getUser(), teacherRole, adminRole, courseRepository);
        validator.validate(course, result);

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.course", result);
            redirectAttributes.addFlashAttribute("course", course);
            return "redirect:/courses/"+course.getId()+"/edit";
        }

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
