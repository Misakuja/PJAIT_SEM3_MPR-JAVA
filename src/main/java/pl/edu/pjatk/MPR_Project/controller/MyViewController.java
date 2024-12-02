package pl.edu.pjatk.MPR_Project.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.edu.pjatk.MPR_Project.model.Capybara;
import pl.edu.pjatk.MPR_Project.service.MyRestService;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

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
        return "displayCapybaraList";
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

    //delete
    @GetMapping("form/delete")
    public String displayDeleteForm(Model model) {
        model.addAttribute("capybara", new Capybara());
        return "deleteForm";
    }

    @PostMapping("form/delete")
    public String submitDeleteForm(Capybara capybara) {
        Long inputId = capybara.getId();
        this.service.deleteCapybaraById(inputId);
        return "redirect:/view/all";
    }

    //patch
    @GetMapping("form/update")
    public String displayUpdateForm(Model model) {
        model.addAttribute("capybara", new Capybara());
        return "updateForm";
    }

    @PostMapping("form/update")
    public String submitUpdateForm(@ModelAttribute Capybara capybara) {
        Long id = capybara.getId();

        this.service.patchCapybaraById(capybara, id);
        return "redirect:/view/all";
    }

    //find by id
    @GetMapping("form/find/byId")
    public String findByIdForm(Model model) {
        model.addAttribute("capybara", new Capybara());
        return "findByIdForm";
    }

    @PostMapping("form/find/byId")
    public String viewById(@ModelAttribute Capybara capybara, Model model) {
        Long inputId = capybara.getId();
        model.addAttribute("capybaras", List.of(this.service.getById(inputId)));
        return "displayCapybaraList";
    }

    //find by name
    @GetMapping("form/find/byName")
    public String findByNameForm(Model model) {
        model.addAttribute("capybara", new Capybara());
        return "findByNameForm";
    }

    @PostMapping("form/find/byName")
    public String viewByName(@ModelAttribute Capybara capybara, Model model) {
        String name = capybara.getName();
        model.addAttribute("capybaras", this.service.getByName(name));
        return "displayCapybaraList";
    }

    //find by age
    @GetMapping("form/find/byAge")
    public String findByAgeForm(Model model) {
        model.addAttribute("capybara", new Capybara());
        return "findByAgeForm";
    }

    @PostMapping("form/find/byAge")
    public String viewByAge(@ModelAttribute Capybara capybara, Model model) {
        int age = capybara.getAge();
        model.addAttribute("capybaras", this.service.getByAge(age));
        return "displayCapybaraList";
    }

    //give pdf
    @GetMapping("form/find/showPdf")
    public String showCapybaraInPdfForm(Model model) {
        model.addAttribute("capybara", new Capybara());
        return "findByIdForPdfForm";
    }

    @PostMapping("form/find/showPdf")
    public void viewPdf(@ModelAttribute Capybara capybara, HttpServletResponse response) {
        Long id = capybara.getId();
        PDDocument document = this.service.getInformationOfCapybaraById(id, response);

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=capybara.pdf");

        try {
            OutputStream out = response.getOutputStream();
            document.save(out);
            out.flush();
            document.close();
        } catch (IOException e) {
            throw new RuntimeException("Error while generating PDF", e);
        }
    }
}
