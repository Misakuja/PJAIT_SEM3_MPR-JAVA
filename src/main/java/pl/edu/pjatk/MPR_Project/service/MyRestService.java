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
    private StringService stringService;

    @Autowired
    public MyRestService(CapybaraRepository repository, StringService stringService) {
        this.repository = repository;
        this.stringService = stringService;

        Capybara capy1 = new Capybara("Capy1", 2);
        Capybara capy2 = new Capybara("Capy2", 3);
        Capybara capy3 = new Capybara("Capy3", 4);

        capy1.setIdentification(capy1);
        capy2.setIdentification(capy2);
        capy3.setIdentification(capy3);

        repository.save(capy1);
        repository.save(capy2);
        repository.save(capy3);
    }

    public void addCapybara(Capybara capybara) {
        capybara.setName(stringService.uppercase(capybara.getName()));

        capybara.setIdentification(capybara);
        this.repository.save(capybara);
    }

    public void patchCapybaraById(Capybara capybara, Long id) {
       Optional<Capybara> capybaraToReplace = repository.findById(id);
        if(capybaraToReplace.isPresent()) {
            capybaraToReplace.get().setName(stringService.uppercase(capybara.getName()));
            capybaraToReplace.get().setAge(capybara.getAge());

            Capybara updatedCapybara = repository.findById(id).get();

            updatedCapybara.setIdentification(updatedCapybara);

            repository.save(capybaraToReplace.get());
        }

    }

    public void deleteCapybaraById(Long id) {
        repository.deleteById(id);
    }

    public List<Capybara> getByName(String name) {
        List<Capybara> capybaraListToLower = repository.findByName(name);
        capybaraListToLower.forEach(capybara -> capybara.setName(stringService.lowercase(capybara.getName())));

        return capybaraListToLower;
    }

    public List<Capybara> getByAge(int age) {
        List<Capybara> capybaraListToLower = repository.findByAge(age);
        capybaraListToLower.forEach(capybara -> capybara.setName(stringService.lowercase(capybara.getName())));

        return capybaraListToLower;
    }

    public Optional<Capybara> getById(Long id) {
        Optional<Capybara> capybaraToLower = repository.findById(id);
        capybaraToLower.ifPresent(capybara -> capybara.setName(stringService.lowercase(capybara.getName())));

        return capybaraToLower;
    }
}

