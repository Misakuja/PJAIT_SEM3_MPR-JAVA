package pl.edu.pjatk.MPR_Project.services;

import org.springframework.stereotype.Component;
import pl.edu.pjatk.MPR_Project.model.Capybara;

import java.util.ArrayList;
import java.util.List;

@Component
public class MyRestService {
    private List<Capybara> capybaraList = new ArrayList<>();

    public MyRestService() {
        capybaraList.add(new Capybara("Capy1", 2));
        capybaraList.add(new Capybara("Capy2", 3));
        capybaraList.add(new Capybara("Capy3", 4));

    }

    public List<Capybara> getAll() {
        return this.capybaraList;
    }

    public void addCapybaraToList(Capybara capybara) {
        this.capybaraList.add(capybara);
    }

    public Capybara getById(int id) {
        return capybaraList.get(id);
    }

    public void updateCapybaraById(Capybara capybara, int id) {
        capybaraList.set(id, capybara);
    }

    public void deleteCapybaraById(int id) {
        capybaraList.remove(id);
    }
}

