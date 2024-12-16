package pl.edu.pjatk.MPR_Project.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.pjatk.MPR_Project.model.Capybara;
import pl.edu.pjatk.MPR_Project.service.MyRestService;

import java.io.OutputStream;
import java.util.List;

@RestController
public class MyRestController {
    private final MyRestService myRestService;

    @Autowired
     public MyRestController(MyRestService myRestService) {
        this.myRestService = myRestService;
    }

    @GetMapping("capybara/get/all")
    public ResponseEntity<List<Capybara>> getAll() {
        return new ResponseEntity<>(this.myRestService.getAllCapybaraObjects(), HttpStatus.OK);
    }

    @GetMapping("capybara/find/name/{name}")
    public ResponseEntity<List<Capybara>> findByName(@PathVariable("name") String name) {
        return new ResponseEntity<>(this.myRestService.getByName(name), HttpStatus.OK);
    }

    @GetMapping("capybara/find/age/{age}")
    public ResponseEntity<List<Capybara>> findByAge(@PathVariable("age") int age) {
        return new ResponseEntity<>(this.myRestService.getByAge(age), HttpStatus.OK);
    }

    @GetMapping("capybara/find/id/{id}")
    public ResponseEntity<Capybara> findById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(this.myRestService.getById(id), HttpStatus.OK);
    }

    @PostMapping("capybara/add")
    public ResponseEntity<Void> addCapybara(@RequestBody Capybara capybara) {
        this.myRestService.addCapybara(capybara);
        return ResponseEntity.ok().build();
    }

    @PutMapping("capybara/patch/{id}")
    public ResponseEntity<Void> updateCapybara(@RequestBody Capybara capybara, @PathVariable("id") Long id) {
        myRestService.patchCapybaraById(capybara, id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("capybara/delete/{id}")
    public ResponseEntity<Void> deleteCapybara(@PathVariable("id") Long id) {
        this.myRestService.deleteCapybaraById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("capybara/get/information/{id}")
    public ResponseEntity<OutputStream> getInformation(@PathVariable("id") Long id, HttpServletResponse response) throws Exception {
        PDDocument document = myRestService.getInformationOfCapybaraById(id, response);

        response.setContentType(MediaType.APPLICATION_PDF_VALUE);
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=capybaraPDF.pdf");
        OutputStream out = response.getOutputStream();
        document.save(out);
        out.flush();
        document.close();

        return ResponseEntity.ok().build();
    }

}
