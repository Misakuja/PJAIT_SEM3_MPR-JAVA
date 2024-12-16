package pl.edu.pjatk.MPR_Project.service;

import jakarta.servlet.http.HttpServletResponse;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import pl.edu.pjatk.MPR_Project.exception.CapybaraAlreadyExists;
import pl.edu.pjatk.MPR_Project.exception.CapybaraNotFoundException;
import pl.edu.pjatk.MPR_Project.exception.InvalidInputCapybaraException;
import pl.edu.pjatk.MPR_Project.model.Capybara;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MyRestService {
    private final StringService stringService;
    private static final List<Field> CAPYBARA_FIELDS;
    private final RestClient restClient;

    static {
        CAPYBARA_FIELDS = List.of(Capybara.class.getDeclaredFields());
        CAPYBARA_FIELDS.forEach(field -> field.setAccessible(true));
    }

    @Autowired
    public MyRestService(StringService stringService) {
        this.stringService = stringService;
        this.restClient = RestClient.create("http://localhost:8082/");
    }

    public void addCapybara(Capybara capybara) {

        if(capybara.getName().isBlank() || capybara.getAge() == 0) {
            throw new InvalidInputCapybaraException();
        }

//        if(capybaraRepository.findByIdentification(identification).isPresent()) {
//            throw new CapybaraAlreadyExists();
//        }

        ResponseEntity<Void> response = restClient.post()
                .uri("/form/add")
                .body(capybara)
                .retrieve()
                .toBodilessEntity();
    }

    public void patchCapybaraById(Capybara capybara, Long id) { //todo
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

    public void deleteCapybaraById(Long id) { //todo
         String result = restClient.get()
                 .uri("/form/delete")
                 .retrieve()
                 .body(String.class);
//        if (repository.findById(id).isEmpty()) {
//            throw new CapybaraNotFoundException();
//        }
//        repository.deleteById(id);
    }

    public List<Capybara> getByName(String name) { //todo
        List<Capybara> capybaraListToLower = repository.findByName(name.toUpperCase());
        capybaraListToLower.forEach(capybara -> capybara.setName(stringService.lowercase(capybara.getName())));

        if (capybaraListToLower.isEmpty()) {
            throw new CapybaraNotFoundException();
        }
        return capybaraListToLower;
    }

    public List<Capybara> getByAge(int age) { //todo
        List<Capybara> capybaraListToLower = repository.findByAge(age);
        capybaraListToLower.forEach(capybara -> capybara.setName(stringService.lowercase(capybara.getName())));

        if (capybaraListToLower.isEmpty()) {
            throw new CapybaraNotFoundException();
        }
        return capybaraListToLower;
    }

    public Capybara getById(Long id) { //todo
        Optional<Capybara> capybaraToLower = repository.findById(id);

        if (capybaraToLower.isEmpty()) {
            throw new CapybaraNotFoundException();
        }
        capybaraToLower.get().setName(stringService.lowercase(capybaraToLower.get().getName()));

        return capybaraToLower.get();
    }

    public List<Capybara> getAllCapybaraObjects() {
        return restClient.get()
                .uri("/")
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
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

