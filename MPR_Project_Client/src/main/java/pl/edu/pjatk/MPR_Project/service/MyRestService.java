package pl.edu.pjatk.MPR_Project.service;

import jakarta.servlet.http.HttpServletResponse;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.io.RandomAccessReadBuffer;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import pl.edu.pjatk.MPR_Project.exception.InvalidInputCapybaraException;
import pl.edu.pjatk.MPR_Project.model.Capybara;

import java.io.IOException;
import java.util.List;

@Service
public class MyRestService {
    private final StringService stringService;
    RestClient restClient;

    @Autowired
    public MyRestService(StringService stringService, RestClient restClient) {
        this.stringService = stringService;
        this.restClient = restClient;
    }

    public void addCapybara(Capybara capybara) {
        restClient.post()
                .uri("/capybara/add")
                .contentType(MediaType.APPLICATION_JSON)
                .body(capybara)
                .retrieve()
                .toBodilessEntity();
    }

    public void patchCapybaraById(Capybara capybara, Long id) {
        if (capybara.getName().isBlank() || capybara.getAge() <= 0) {
            throw new InvalidInputCapybaraException();
        }

        restClient.patch()
                .uri("/capybara/patch/{id}", id)
                .retrieve()
                .toBodilessEntity();
    }

    public void deleteCapybaraById(Long id) {
        restClient.delete()
                .uri("/capybara/delete/{id}", id)
                .retrieve()
                .toBodilessEntity();
    }

    public List<Capybara> getByName(String name) {
        return restClient.get()
                .uri("/capybara/find/name/{name}", name)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }

    public List<Capybara> getByAge(int age) {
        return restClient.get()
                .uri("/capybara/find/age/{age}", age)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }

    public Capybara getById(Long id) {
        return restClient.get()
                .uri("/capybara/find/id/{id}", id)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }

    public List<Capybara> getAllCapybaraObjects() {
        return restClient.get()
                .uri("/")
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }

    public PDDocument getInformationOfCapybaraById(Long id, HttpServletResponse response) {
        byte[] pdfBytes = restClient.get()
                .uri("/capybara/get/information/{id}", id)
                .accept(MediaType.APPLICATION_PDF)
                .retrieve()
                .body(byte[].class);
        try {
            assert pdfBytes != null;
            RandomAccessReadBuffer buffer = new RandomAccessReadBuffer(pdfBytes);

            return Loader.loadPDF(buffer);

        } catch (IOException e) {
            throw new RuntimeException("Failed to load PDF", e);
        }
    }
}

