package pl.edu.pjatk.MPR_Project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.edu.pjatk.MPR_Project.model.Capybara;
import pl.edu.pjatk.MPR_Project.services.MyRestService;

import java.util.List;

@RestController
public class MyRestController {
    private MyRestService myRestService;

    @Autowired
     public MyRestController(MyRestService myRestService) {
        this.myRestService = myRestService;
    }

    @GetMapping("capybara/all")
    public List<Capybara> getAllCapybaras() {
        return myRestService.getAll();
    }

    @PostMapping("capybara/")
    public void addCapybara(@RequestBody Capybara capybara) {
        myRestService.addCapybaraToList(capybara);
    }

    @RequestMapping(path="capybara/{id}/")
    public Capybara getCapybara(@PathVariable("id")int id) {
        return myRestService.getById(id);
    }

    @PutMapping("capybara/put/{id}/")
    public void updateCapybara(@RequestBody Capybara capybara, @PathVariable("id")int id) {
        myRestService.updateCapybaraById(capybara, id);
    }

    @DeleteMapping("capybara/delete/{id}/")
    public void deleteCapybara(@PathVariable("id") int id) {
        myRestService.deleteCapybaraById(id);
    }


}
