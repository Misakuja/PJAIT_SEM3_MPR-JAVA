package pl.edu.pjatk.MPR_Project.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.edu.pjatk.MPR_Project.model.Capybara;

import java.util.List;
import java.util.Optional;

@Repository
public interface CapybaraRepository extends CrudRepository<Capybara, Long> {
    List<Capybara> findByName(String name);

    List<Capybara> findByAge(int age);

    Optional<Capybara> findById(Long id);

    void addCapybara(Capybara capybara);

    void patchCapybara(Capybara capybara, Long id);

    void deleteCapybara(Long id);
}
