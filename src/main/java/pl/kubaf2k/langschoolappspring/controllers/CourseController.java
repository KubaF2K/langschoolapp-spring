package pl.kubaf2k.langschoolappspring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.kubaf2k.langschoolappspring.models.Course;
import pl.kubaf2k.langschoolappspring.models.Language;
import pl.kubaf2k.langschoolappspring.repositories.CourseRepository;
import pl.kubaf2k.langschoolappspring.repositories.LanguageRepository;

import java.util.ArrayList;
import java.util.List;

@Controller
public class CourseController {

    private final CourseRepository courseRepository;
    private final LanguageRepository languageRepository;

    @Autowired
    public CourseController(CourseRepository courseRepository, LanguageRepository languageRepository) {
        this.courseRepository = courseRepository;
        this.languageRepository = languageRepository;
    }

    @GetMapping("/courses")
    public String index(Model model) {
        Iterable<Language> languages = languageRepository.findAll();

        model.addAttribute("languages", languages);
        return "courses/index";
    }
}
