package pl.edu.pjatk.MPR_Project.service;

import jakarta.servlet.http.HttpServletResponse;
import org.apache.coyote.Response;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.io.RandomAccessReadBuffer;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import pl.edu.pjatk.MPR_Project.exception.CapybaraAlreadyExists;
import pl.edu.pjatk.MPR_Project.exception.CapybaraNotFoundException;
import pl.edu.pjatk.MPR_Project.exception.InvalidInputCapybaraException;
import pl.edu.pjatk.MPR_Project.model.Capybara;


import java.util.List;
import java.util.Optional;


@Service
public class MyRestService {
    private final StringService stringService;
    private static final Logger logger = LoggerFactory.getLogger(MyRestService.class);

    @Autowired
    RestClient restClient;

    public MyRestService(StringService stringService, RestClient restClient) {
        this.stringService = stringService;
        this.restClient = restClient;
    }

    public void addCapybara(Capybara capybara) {
        logger.info("Attempting to add capybara with values: {}", capybara);

        Optional<Capybara> existingCapybara = restClient.get()
                .uri("/capybara/find/identification/" + capybara.getIdentification())
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });

        if (existingCapybara.isPresent()) {
            throw new CapybaraAlreadyExists();
        }

        try {
            restClient.post()
                    .uri("/capybara/add")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(capybara)
                    .retrieve()
                    .toBodilessEntity();
        } catch (Exception e) {
            logger.error("Failed to add capybara. {}", String.valueOf(e));
            throw e;
        }
        logger.info("Successfully added capybara with values: {}", capybara);
    }

    public void patchCapybaraById(Capybara capybara, Long id) {
        logger.info("Attempting to update capybara with ID: {} with values: {}", id, capybara);

        Optional<Capybara> existingCapybara = restClient.get()
                .uri("/capybara/find/identification/" + capybara.getIdentification())
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });

        if (existingCapybara.isPresent()) {
            throw new CapybaraAlreadyExists();
        }

        if (capybara.getName().isBlank() || capybara.getAge() <= 0) {
            logger.error("Invalid Input. Failed.");
            throw new InvalidInputCapybaraException();
        }
        try {
            restClient.patch()
                    .uri("/capybara/patch/{id}", id)
                    .retrieve()
                    .toBodilessEntity();
        } catch (Exception e) {
            logger.error("Failed to patch capybara. {}", String.valueOf(e));
            throw e;
        }
        logger.info("Successfully updated capybara with ID: {} with values: {}", id, capybara);
    }

    public void deleteCapybaraById(Long id) {
        logger.info("Attempting to delete capybara with ID: {}", id);

        ResponseEntity<Capybara> response = restClient.get()
                .uri("/capybara/find/id/{id}", id)
                .retrieve()
                .toEntity(Capybara.class);

        if(response.getStatusCode() == HttpStatus.NOT_FOUND) {
            logger.error("Failed to delete capybara. Capybara not found");
            throw new CapybaraNotFoundException();
        }

        try {
            restClient.delete()
                    .uri("/capybara/delete/{id}", id)
                    .retrieve()
                    .toBodilessEntity();
        } catch (Exception e) {
            logger.error("Failed to delete capybara. {}", String.valueOf(e));
            throw e;
        }
        logger.info("Successfully deleted capybara with ID: {}", id);
    }

    public List<Capybara> getByName(String name) {
        logger.info("Attempting to get capybara with name: {}", name);

        ResponseEntity<Capybara> response = restClient.get()
                .uri("/capybara/find/name/{name}", name)
                .retrieve()
                .toEntity(Capybara.class);

        if(response.getStatusCode() == HttpStatus.NOT_FOUND) {
            logger.error("Failed to get capybara by name. Capybara not found");
            throw new CapybaraNotFoundException();
        }

        try {
            List<Capybara> capybaraList = restClient.get()
                    .uri("/capybara/find/name/{name}", name)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {});
            logger.info("Successfully got capybara with name: {}", name);
            return capybaraList;
        } catch (Exception e) {
            logger.error("Failed to get capybara by name. {}", String.valueOf(e));
            throw e;
        }
    }

    public List<Capybara> getByAge(int age) {
        logger.info("Attempting to get capybara with age: {}", age);
        try {
            List<Capybara> capybaraList = restClient.get()
                    .uri("/capybara/find/age/{age}", age)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {
                    });
            logger.info("Successfully got capybara with age: {}", age);
            return capybaraList;
        } catch (Exception e) {
            logger.error("Failed to get capybara by age. {}", String.valueOf(e));
            throw e;
        }
    }

    public Capybara getById(Long id) {
        logger.info("Attempting to get capybara with ID: {}", id);
        try {
            Capybara capybara = restClient.get()
                    .uri("/capybara/find/id/{id}", id)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {
                    });
            logger.info("Successfully got capybara with ID: {}", id);
            return capybara;
        } catch (Exception e) {
            logger.error("Failed to get capybara by ID. {}", String.valueOf(e));
            throw e;
        }
    }

    public List<Capybara> getAllCapybaraObjects() {
        logger.info("Attempting to get all capybaras");
        try {
            List<Capybara> capybaraList = restClient.get()
                    .uri("/")
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {
                    });
            logger.info("Successfully got all capybaras");
            return capybaraList;
        } catch (Exception e) {
            logger.error("Failed to get all capybaras. {}", String.valueOf(e));
            throw e;
        }
    }

    public PDDocument getInformationOfCapybaraById(Long id, HttpServletResponse response) {
        logger.info("Attempting to get PDF of capybara with ID: {}", id);
        byte[] pdfBytes = restClient.get()
                .uri("/capybara/get/information/{id}", id)
                .accept(MediaType.APPLICATION_PDF)
                .retrieve()
                .body(byte[].class);
        try {
            assert pdfBytes != null;
            RandomAccessReadBuffer buffer = new RandomAccessReadBuffer(pdfBytes);

            logger.info("Successfully got capybara PDF");
            return Loader.loadPDF(buffer);

        } catch (Exception e) {
            logger.error("Failed to load PDF. {}", String.valueOf(e));
            throw new RuntimeException("Failed to load PDF", e);
        }
    }
}

