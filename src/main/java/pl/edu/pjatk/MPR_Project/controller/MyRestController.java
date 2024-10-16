package pl.edu.pjatk.MPR_Project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.edu.pjatk.MPR_Project.model.Capybara;
import pl.edu.pjatk.MPR_Project.service.MyRestService;

import java.util.List;
import java.util.Optional;

@RestController
public class MyRestController {
    private MyRestService myRestService;

    @Autowired
     public MyRestController(MyRestService myRestService) {
        this.myRestService = myRestService;
    }

    @GetMapping("capybara/find/name/{name}/")
    public List<Capybara> findByName(@PathVariable("name") String name) {
        return this.myRestService.getByName(name);
    }

    @GetMapping("capybara/find/age/{age}/")
    public List<Capybara> findByAge(@PathVariable("age") int age) {
        return this.myRestService.getByAge(age);
    }

    @GetMapping("capybara/find/id/{id}/")
    public Optional<Capybara> findById(@PathVariable("id") Long id) {
        return this.myRestService.getById(id);
    }

    @PostMapping("capybara/add/")
    public void addCapybara(@RequestBody Capybara capybara) {
        myRestService.addCapybara(capybara);
    }


    @PutMapping("capybara/patch/{id}/")
    public void updateCapybara(@RequestBody Capybara capybara, @PathVariable("id") Long id) {
        myRestService.patchCapybaraById(capybara, id);
    }

    @DeleteMapping("capybara/delete/{id}/")
    public void deleteCapybara(@PathVariable("id") Long id) {
        myRestService.deleteCapybaraById(id);
    }


}
