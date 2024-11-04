package pl.edu.pjatk.MPR_Project;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import pl.edu.pjatk.MPR_Project.exception.CapybaraAlreadyExists;
import pl.edu.pjatk.MPR_Project.exception.CapybaraNotFoundException;
import pl.edu.pjatk.MPR_Project.exception.InvalidInputCapybaraException;
import pl.edu.pjatk.MPR_Project.model.Capybara;
import pl.edu.pjatk.MPR_Project.repository.CapybaraRepository;
import pl.edu.pjatk.MPR_Project.service.MyRestService;
import pl.edu.pjatk.MPR_Project.service.StringService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class MyRestServiceTest {
    private StringService stringService;
    private CapybaraRepository capybaraRepository;
    private MyRestService myRestService;

    @BeforeEach
    public void setup() {
        this.capybaraRepository = Mockito.mock(CapybaraRepository.class);
        this.stringService = Mockito.mock(StringService.class);
        this.myRestService = new MyRestService(capybaraRepository, stringService);
    }

    @Test
    public void getCapybaraByIdTest() {
        Capybara repoCapybara = new Capybara("TEST", 2);
        when(capybaraRepository.findById(Long.valueOf(2))).thenReturn(Optional.of(repoCapybara));
        myRestService.getById(2L);

        verify(capybaraRepository).findById(Long.valueOf(2));
        verify(stringService).lowercase("TEST");
    }

    @Test
    public void getCapybaraByAgeTest() {
        List<Capybara> repoCapybara = new ArrayList<>();
        repoCapybara.add(new Capybara("TEST", 2));

        when(capybaraRepository.findByAge(2)).thenReturn(repoCapybara.stream().toList());
        myRestService.getByAge(2);

        verify(capybaraRepository).findByAge(2);
        verify(stringService).lowercase("TEST");
    }

    @Test
    public void getCapybaraByNameTest() {
        List<Capybara> repoCapybara = new ArrayList<>();
        repoCapybara.add(new Capybara("TEST", 2));

        when(capybaraRepository.findByName("TEST")).thenReturn(repoCapybara.stream().toList());
        myRestService.getByName("TEST");

        verify(capybaraRepository).findByName("TEST");
        verify(stringService).lowercase("TEST");
    }

    @Test
    public void deleteCapybaraByIdTest() {
        Capybara repoCapybara = new Capybara("Test", 2);
        when(capybaraRepository.findById(any())).thenReturn(Optional.of(repoCapybara));

        myRestService.deleteCapybaraById(capybaraRepository.findById(Long.valueOf(2)).get().getId());

        verify(capybaraRepository).findById(Long.valueOf(2));
    }

    @Test
    public void patchCapybaraByIdTest() {
        Capybara repoCapybara1 = new Capybara("Test", 2);
        Capybara repoCapybara2 = new Capybara("TestChanged", 3);

        when(capybaraRepository.findById(Long.valueOf(2))).thenReturn(Optional.of(repoCapybara1));
        when(stringService.uppercase("TestChanged")).thenReturn("TESTCHANGED");

        myRestService.patchCapybaraById(repoCapybara2, 2L);

        assertEquals(3, repoCapybara1.getAge());
        assertEquals("TESTCHANGED", repoCapybara1.getName());

        verify(capybaraRepository).findById(Long.valueOf(2));
        verify(stringService).uppercase("TestChanged");
        verify(capybaraRepository).save(repoCapybara1);
    }

    @Test
    public void addCapybaraTest() {
        Capybara repoCapybara = new Capybara("Test", 2);
        when(capybaraRepository.findById(Long.valueOf(2))).thenReturn(Optional.of(repoCapybara));
        when(stringService.uppercase("Test")).thenReturn("TEST");

        myRestService.addCapybara(repoCapybara);

        verify(stringService).uppercase("Test");
        verify(capybaraRepository).save(repoCapybara);
    }

    @Test
    public void getAllCapybaraObjectsTest() {
        List<Capybara> repoCapybaraList = new ArrayList<>();
        Capybara repoCapybara1 = new Capybara("Test1", 2);
        Capybara repoCapybara2 = new Capybara("Test2", 3);

        repoCapybaraList.add(repoCapybara1);
        repoCapybaraList.add(repoCapybara2);

        when(capybaraRepository.findAll()).thenReturn(repoCapybaraList);
        when(stringService.lowercase("Test1")).thenReturn("Test1");
        when(stringService.lowercase("Test2")).thenReturn("Test2");

        List<Capybara> result = myRestService.getAllCapybaraObjects();

        assertEquals(2, result.size());
        assertEquals("Test1", result.get(0).getName());
        assertEquals("Test2", result.get(1).getName());

        verify(capybaraRepository).findAll();
        verify(stringService).lowercase("Test1");
        verify(stringService).lowercase("Test2");
    }

    @Test
    public void addCapybaraThrowsExceptionWhenTheInputIsInvalidDueToAgeTest() {
        Capybara repoCapybara = new Capybara("Test", 0);

        when(capybaraRepository.findById(Long.valueOf(2))).thenReturn(Optional.of(repoCapybara));
        when(stringService.uppercase("Test")).thenReturn("TEST");
        when(stringService.lowercase("TEST")).thenReturn("Test");

        assertThrows(InvalidInputCapybaraException.class, () -> this.myRestService.addCapybara(repoCapybara));

    }

    @Test
    public void addCapybaraThrowsExceptionWhenTheInputIsInvalidDueToNameTest() {
        Capybara repoCapybara = new Capybara("", 2);

        when(capybaraRepository.findById(Long.valueOf(2))).thenReturn(Optional.of(repoCapybara));
        when(stringService.uppercase("")).thenReturn("");
        when(stringService.lowercase("")).thenReturn("");

        assertThrows(InvalidInputCapybaraException.class, () -> this.myRestService.addCapybara(repoCapybara));
    }

    @Test
    public void addCapybaraThrowsExceptionCapybaraAlreadyExistsTest() {
        Capybara repoCapybara = new Capybara("Test", 2);

        when(stringService.uppercase("Test")).thenReturn("TEST");

        repoCapybara.setIdentification();
        long existingIdentification = repoCapybara.getIdentification();

        when(capybaraRepository.findByIdentification(existingIdentification)).thenReturn(Optional.of(repoCapybara));

        assertThrows(CapybaraAlreadyExists.class, () -> this.myRestService.addCapybara(repoCapybara));
    }

    @Test
    public void patchCapybaraThrowsExceptionCapybaraNotFoundTest() {
        Capybara capybaraToPatch = new Capybara();

        when(capybaraRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(CapybaraNotFoundException.class, () -> this.myRestService.patchCapybaraById(capybaraToPatch, 2L));
    }

    @Test
    public void patchCapybaraThrowsExceptionInvalidInputCapybaraDueToNameTest() {
        Capybara capybaraToSwitchTo = new Capybara("", 2);
        Capybara existingCapybara = new Capybara("Test2", 3);

        when(capybaraRepository.findById(Long.valueOf(2))).thenReturn(Optional.of(existingCapybara));

        assertThrows(InvalidInputCapybaraException.class, () -> myRestService.patchCapybaraById(capybaraToSwitchTo, 2L));
    }

    @Test
    public void patchCapybaraThrowsExceptionInvalidInputCapybaraDueToAgeTest() {
        Capybara capybaraToSwitchTo = new Capybara("Test", 0);
        Capybara existingCapybara = new Capybara("Test", 3);

        when(capybaraRepository.findById(Long.valueOf(2))).thenReturn(Optional.of(existingCapybara));

        assertThrows(InvalidInputCapybaraException.class, () -> myRestService.patchCapybaraById(capybaraToSwitchTo, 2L));
    }

    @Test
    public void patchCapybaraThrowsExceptionCapybaraAlreadyExistsTest() {
        Capybara capybara = new Capybara("Test", 2);
        Capybara existingCapybara = new Capybara("Test", 2);

        when(capybaraRepository.findById(Long.valueOf(2))).thenReturn(Optional.of(existingCapybara));
        when(capybaraRepository.findByIdentification(102)).thenReturn(Optional.of(existingCapybara));
        when(stringService.uppercase("Test")).thenReturn("TEST");

        assertThrows(CapybaraAlreadyExists.class, () -> myRestService.patchCapybaraById(capybara, 2L));
    }

    @Test
    public void deleteCapybaraThrowsExceptionCapybaraNotFoundTest() {
        when(capybaraRepository.findById(Long.valueOf(2))).thenReturn(Optional.empty());

        assertThrows(CapybaraNotFoundException.class, () -> myRestService.deleteCapybaraById(2L));
    }

    @Test
    public void getByNameThrowsExceptionCapybaraNotFoundTest() {
        when(capybaraRepository.findById(Long.valueOf(2))).thenReturn(Optional.empty());
        when(stringService.lowercase("TEST")).thenReturn("Test");

        assertThrows(CapybaraNotFoundException.class, () -> myRestService.getByName("TEST"));
    }

    @Test
    public void getByAgeThrowsExceptionCapybaraNotFoundTest() {
        when(capybaraRepository.findById(Long.valueOf(2))).thenReturn(Optional.empty());

        assertThrows(CapybaraNotFoundException.class, () -> myRestService.getByAge(2));
    }

    @Test
    public void getByIdThrowsExceptionCapybaraNotFoundTest() {
        when(capybaraRepository.findById(Long.valueOf(2))).thenReturn(Optional.empty());

        assertThrows(CapybaraNotFoundException.class, () -> myRestService.getById(2L));

    }

    @Test
    public void getAllCapybaraObjectsThrowsExceptionCapybaraNotFoundTest() {
        when(capybaraRepository.findAll()).thenReturn(Collections.emptyList());

        assertThrows(CapybaraNotFoundException.class, () -> myRestService.getAllCapybaraObjects());
    }
}

