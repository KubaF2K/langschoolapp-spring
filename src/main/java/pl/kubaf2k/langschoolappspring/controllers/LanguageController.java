package pl.kubaf2k.langschoolappspring.controllers;

import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LanguageController {

    @GetMapping("/language")
    public String index() {
        throw new NotYetImplementedException();
    }

    @GetMapping("/language/add")
    public String add() {
        throw new NotYetImplementedException();
    }

    @PostMapping("/language/add")
    public String create() {
        throw new NotYetImplementedException();
    }

    @GetMapping("/language/{id}/edit")
    public String edit(@PathVariable int id) {
        throw new NotYetImplementedException();
    }

    @PostMapping("/language/edit")
    public String update() {
        throw new NotYetImplementedException();
    }

    @PostMapping("/language/delete")
    public String delete() {
        throw new NotYetImplementedException();
    }
}
