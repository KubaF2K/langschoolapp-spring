package pl.kubaf2k.langschoolappspring.controllers;

import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.kubaf2k.langschoolappspring.models.Language;
import pl.kubaf2k.langschoolappspring.repositories.LanguageRepository;
import pl.kubaf2k.langschoolappspring.repositories.UserRepository;

import javax.validation.Valid;

@Controller
public class LanguageController {

    private final LanguageRepository languageRepository;
    private final UserRepository userRepository;

    @Autowired
    public LanguageController(LanguageRepository languageRepository, UserRepository userRepository) {
        this.languageRepository = languageRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/language")
    public String index(Model model) {
        model.addAttribute("languages", languageRepository.findAll());

        return "language/index";
    }

    @GetMapping("/language/{id}/edit")
    public String edit(@PathVariable int id, Model model) {
        if (!model.containsAttribute("language"))
            model.addAttribute(languageRepository.findById(id).orElseThrow());

        return "language/edit";
    }

    @PostMapping("/language/edit")
    public String update(@ModelAttribute @Valid Language language,
                         BindingResult result,
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.language", result);
            redirectAttributes.addFlashAttribute("language", language);
            return "redirect:/language/" + language.getId() + "/edit";
        }

        languageRepository.save(language);

        redirectAttributes.addFlashAttribute("msg", "Zedytowano pomyślnie!");
        return "redirect:/language";
    }

    @GetMapping("/language/add")
    public String add(Model model) {
        if (!model.containsAttribute("language"))
            model.addAttribute(new Language());

        return "language/add";
    }

    @PostMapping("/language/add")
    public String create(@ModelAttribute @Valid Language language,
                         BindingResult result,
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.language", result);
            redirectAttributes.addFlashAttribute("language", language);
            return "redirect:/language/add";
        }

        languageRepository.save(language);

        redirectAttributes.addFlashAttribute("msg", "Dodano język!");
        return "redirect:/language";
    }

    @PostMapping("/language/delete")
    public String delete(@RequestParam int id, RedirectAttributes redirectAttributes) {
        var language = languageRepository.findById(id).orElseThrow();
        if (!language.getCourses().isEmpty()) {
            redirectAttributes.addFlashAttribute("errmsg", "Nie można usunąć języka z kursami!");
            return "redirect:/language";
        }
        for (var user : language.getUsers()) {
            user.setLanguage(null);
            userRepository.save(user);
        }
        languageRepository.delete(language);

        redirectAttributes.addFlashAttribute("msg", "Usunięto język!");
        return "redirect:/language";
    }
}
