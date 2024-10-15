package pl.edu.pjatk.MPR_Project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.pjatk.MPR_Project.model.Capybara;
import pl.edu.pjatk.MPR_Project.repository.CapybaraRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MyRestService {
    private CapybaraRepository repository;

    @Autowired
    public MyRestService(CapybaraRepository repository) {
        this.repository = repository;
        repository.save(new Capybara("Capy1", 2));
        repository.save(new Capybara("Capy2", 3));
        repository.save(new Capybara("Capy3", 4));
    }

    public void addCapybara(Capybara capybara) {
        this.repository.save(capybara);
    }

    public void patchCapybaraById(Capybara capybara, Long id) {
       Optional<Capybara> capybaraToReplace = repository.findById(id);
        if(capybaraToReplace.isPresent()) {
            capybaraToReplace.get().setName(capybara.getName());
            capybaraToReplace.get().setAge(capybara.getAge());
            repository.save(capybaraToReplace.get());

            Capybara updatedCapybara = repository.findById(id).get();

            updatedCapybara.setIdentification(updatedCapybara);
        }

    }

    public void deleteCapybaraById(Long id) {
        repository.deleteById(id);
    }

    public List<Capybara> getByName(String name) {
        return repository.findByName(name);
    }

    public List<Capybara> getByAge(int age) {
        return repository.findByAge(age);
    }

    public Optional<Capybara> getById(Long id) {
        return repository.findById(id);
    }
}

