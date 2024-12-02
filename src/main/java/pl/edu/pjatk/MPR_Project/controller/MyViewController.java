package pl.edu.pjatk.MPR_Project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import pl.edu.pjatk.MPR_Project.model.Capybara;
import pl.edu.pjatk.MPR_Project.service.MyRestService;

@Controller
public class MyViewController {
    private final MyRestService service;

    public MyViewController(MyRestService service) {
        this.service = service;
    }

    //Get all
    @GetMapping("view/all")
    public String viewAll(Model model) {
        model.addAttribute("capybaras", service.getAllCapybaraObjects());
        return "viewAll";
    }

    //add Capy
    @GetMapping("form/add")
    public String displayAddForm(Model model) {
        model.addAttribute("capybara", new Capybara());
        return "addForm";
    }

    @PostMapping("form/add")
    public String submitAddForm(@ModelAttribute Capybara newCapybara) {
        this.service.addCapybara(newCapybara);
        return "redirect:/view/all";
    }




}
