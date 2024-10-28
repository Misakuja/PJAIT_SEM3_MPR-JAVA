package pl.edu.pjatk.MPR_Project;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import pl.edu.pjatk.MPR_Project.model.Capybara;
import pl.edu.pjatk.MPR_Project.repository.CapybaraRepository;
import pl.edu.pjatk.MPR_Project.service.MyRestService;
import pl.edu.pjatk.MPR_Project.service.StringService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class MyRestServiceTest {
    StringService stringService;
    CapybaraRepository capybaraRepository;
    MyRestService myRestService;

    @BeforeEach
    public void setup() {
        this.capybaraRepository = Mockito.mock(CapybaraRepository.class);
        this.stringService = Mockito.mock(StringService.class);
        this.myRestService = new MyRestService(capybaraRepository, stringService);
    }

    @Test
    public void getCapybaraByIdTest() {
        Capybara repoCapybara = new Capybara("TEST", 2);
        when(capybaraRepository.findById(2L)).thenReturn(Optional.of(repoCapybara));
        when(stringService.lowercase("TEST")).thenReturn("Test");

        myRestService.getById(2L);

        verify(capybaraRepository).findById(2L);
        verify(stringService).lowercase("TEST");
    }

    @Test
    public void getCapybaraByAgeTest() {
        List<Capybara> repoCapybara = new ArrayList<>();
        repoCapybara.add(new Capybara("TEST", 2));

        when(capybaraRepository.findByAge(2)).thenReturn(repoCapybara.stream().toList());
        when(stringService.lowercase("TEST")).thenReturn("Test");

        myRestService.getByAge(2);

        verify(capybaraRepository).findByAge(2);
        verify(stringService).lowercase("TEST");
    }

    @Test
    public void getCapybaraByNameTest() {
        List<Capybara> repoCapybara = new ArrayList<>();
        repoCapybara.add(new Capybara("TEST", 2));

        when(capybaraRepository.findByName("TEST")).thenReturn(repoCapybara.stream().toList());
        when(stringService.lowercase("TEST")).thenReturn("Test");

        myRestService.getByName("TEST");

        verify(capybaraRepository).findByName("TEST");
        verify(stringService).lowercase("TEST");
    }

    @Test
    public void deleteCapybaraByIdTest() {
        Capybara repoCapybara = new Capybara("Test", 2);
        when(capybaraRepository.findById(2L)).thenReturn(Optional.of(repoCapybara));

        myRestService.deleteCapybaraById(capybaraRepository.findById(2L).get().getId());

        verify(capybaraRepository).findById(2L);
    }

    @Test
    public void patchCapybaraByIdTest() {
        Capybara repoCapybara1 = new Capybara("Test", 2);

        when(capybaraRepository.findById(2L)).thenReturn(Optional.of(repoCapybara1));
        when(stringService.uppercase("Test")).thenReturn("TEST");

        myRestService.patchCapybaraById(repoCapybara1, 2L);

        verify(capybaraRepository, times(2)).findById(2L);
        verify(stringService).uppercase("Test");
        verify(capybaraRepository).save(repoCapybara1);

    }

    @Test
    public void addCapybaraTest() {
        Capybara repoCapybara = new Capybara("Test", 2);
        when(capybaraRepository.findById(2L)).thenReturn(Optional.of(repoCapybara));

        myRestService.addCapybara(repoCapybara);
        repoCapybara.setIdentification(repoCapybara);

        verify(stringService).uppercase("Test");
        verify(capybaraRepository).save(repoCapybara);
    }


}
