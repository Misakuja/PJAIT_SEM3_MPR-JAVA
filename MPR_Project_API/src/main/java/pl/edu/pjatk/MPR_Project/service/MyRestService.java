package pl.edu.pjatk.MPR_Project.service;

import jakarta.servlet.http.HttpServletResponse;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.pjatk.MPR_Project.exception.CapybaraAlreadyExists;
import pl.edu.pjatk.MPR_Project.exception.CapybaraNotFoundException;
import pl.edu.pjatk.MPR_Project.exception.InvalidInputCapybaraException;
import pl.edu.pjatk.MPR_Project.model.Capybara;
import pl.edu.pjatk.MPR_Project.repository.CapybaraRepository;
import pl.edu.pjatk.MPR_Project.service.StringService;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MyRestService {
    private final CapybaraRepository capybaraRepository;
    private final CapybaraRepository repository;
    private final StringService stringService;

    @Autowired
    public MyRestService(CapybaraRepository repository, StringService stringService) {
        this.repository = repository;
        this.stringService = stringService;


        Capybara capy1 = new Capybara("CAPY1", 2);
        Capybara capy2 = new Capybara("CAPY2", 3);
        Capybara capy3 = new Capybara("CAPY3", 4);

        capy1.setIdentification();
        capy2.setIdentification();
        capy3.setIdentification();

        repository.save(capy1);
        repository.save(capy2);
        repository.save(capy3);
        this.capybaraRepository = repository;
    }

    public void addCapybara(Capybara capybara) {
        capybara.setName(stringService.uppercase(capybara.getName()));

        capybara.setIdentification();

        long identification = capybara.getIdentification();

        if(capybara.getName().isBlank() || capybara.getAge() == 0) {
            throw new InvalidInputCapybaraException();
        }

        if(capybaraRepository.findByIdentification(identification).isPresent()) {
            throw new CapybaraAlreadyExists();
        }

        this.repository.save(capybara);
    }

    public void patchCapybaraById(Capybara capybara, Long id) {
        Optional<Capybara> capybaraToReplaceOptional = repository.findById(id);

        if (capybaraToReplaceOptional.isEmpty()) {
            throw new CapybaraNotFoundException();
        }
        if(capybara.getName().isBlank() || capybara.getAge() <= 0) {
            throw new InvalidInputCapybaraException();
        }

        Capybara capybaraToReplace = capybaraToReplaceOptional.get();

        capybaraToReplace.setName(stringService.uppercase(capybara.getName()));
        capybaraToReplace.setAge(capybara.getAge());

        capybaraToReplace.setIdentification();

        long identification = capybaraToReplace.getIdentification();
        if(capybaraRepository.findByIdentification(identification).isPresent()) {
            throw new CapybaraAlreadyExists();
        }

        repository.save(capybaraToReplace);
    }

    public void deleteCapybaraById(Long id) {
        if (repository.findById(id).isEmpty()) {
            throw new CapybaraNotFoundException();
        }
        repository.deleteById(id);
    }

    public List<Capybara> getByName(String name) {
        List<Capybara> capybaraListToLower = repository.findByName(name.toUpperCase());
        capybaraListToLower.forEach(capybara -> capybara.setName(stringService.lowercase(capybara.getName())));

        if (capybaraListToLower.isEmpty()) {
            throw new CapybaraNotFoundException();
        }
        return capybaraListToLower;
    }

    public List<Capybara> getByAge(int age) {
        List<Capybara> capybaraListToLower = repository.findByAge(age);
        capybaraListToLower.forEach(capybara -> capybara.setName(stringService.lowercase(capybara.getName())));

        if (capybaraListToLower.isEmpty()) {
            throw new CapybaraNotFoundException();
        }
        return capybaraListToLower;
    }

    public Capybara getById(Long id) {
        Optional<Capybara> capybaraToLower = repository.findById(id);

        if (capybaraToLower.isEmpty()) {
            throw new CapybaraNotFoundException();
        }
        capybaraToLower.get().setName(stringService.lowercase(capybaraToLower.get().getName()));

        return capybaraToLower.get();
    }

    public List<Capybara> getAllCapybaraObjects() {
        List<Capybara> capybaraListToLower = (List<Capybara>) repository.findAll();
        capybaraListToLower.forEach(capybara -> capybara.setName(stringService.lowercase(capybara.getName())));

        if (capybaraListToLower.isEmpty()) {
            throw new CapybaraNotFoundException();
        }
        return capybaraListToLower;
    }

    public PDDocument getInformationOfCapybaraById(Long id, HttpServletResponse response) {
        Optional<Capybara> capybaraToLower = repository.findById(id);
        if (capybaraToLower.isEmpty()) {
            throw new CapybaraNotFoundException();
        }
        Capybara capybara = capybaraToLower.get();
        try {
            PDDocument document = new PDDocument();
            PDPage addPage = new PDPage();
            document.addPage(addPage);
            PDPage page = document.getPage(0);
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.beginText();
            contentStream.newLineAtOffset(25, 700);
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.TIMES_ROMAN), 12);
            contentStream.setLeading(14.5f);
            Class<? extends Capybara> clazz = capybara.getClass();
            List<Field> fields = new ArrayList<>(List.of(clazz.getDeclaredFields()));
            for (Field field : fields) {
                field.setAccessible(true);
                String fieldText = field.getName().toUpperCase() + ": " + field.get(capybara).toString();
                contentStream.showText(fieldText);
                contentStream.newLine();
            }
            contentStream.endText();
            contentStream.close();
            return document;
        } catch (IOException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}

