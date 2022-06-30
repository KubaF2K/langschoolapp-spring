package pl.kubaf2k.langschoolappspring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class CourseController {

    private final CourseRepository courseRepository;
    private final LanguageRepository languageRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public CourseController(CourseRepository courseRepository,
                            LanguageRepository languageRepository,
                            UserRepository userRepository,
                            RoleRepository roleRepository) {
        this.courseRepository = courseRepository;
        this.languageRepository = languageRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
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
        Role teacher = roleRepository.findByName("ROLE_TEACHER");
        Iterable<User> teachers = userRepository.findAllByRolesContaining(teacher);

        model.addAttribute("languages", languages);
        model.addAttribute("teachers", teachers);
        model.addAttribute("course", new Course());
        return "courses/add";
    }

//    TODO validation
    @PostMapping("/courses/add")
    public String create(@ModelAttribute @Valid Course course,
                         @RequestParam(name = "language_id") int languageId,
                         @RequestParam(name = "teacher_id") int teacherId,
                         Model model,
                         RedirectAttributes redirectAttributes,
                         BindingResult result) {
        //TODO returns 400 bad request instead of redirect
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.course", result);
            redirectAttributes.addFlashAttribute("course", course);
            return "redirect:/courses/add";
        }

        course.setLanguage(languageRepository.findById(languageId).orElseThrow());
        course.setTeacher(userRepository.findById(teacherId).orElseThrow());

        courseRepository.save(course);
        redirectAttributes.addFlashAttribute("msg", "Utworzono kurs!");
        return "redirect:/courses";
    }

    @GetMapping("/courses/{id}/edit")
    public String edit(@PathVariable int id, Model model) {
        Course course = courseRepository.findById(id).orElseThrow();
        Role teacherRole = roleRepository.findByName("ROLE_TEACHER");
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
                         RedirectAttributes redirectAttributes) {
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
