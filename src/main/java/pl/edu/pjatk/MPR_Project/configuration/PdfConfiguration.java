package pl.edu.pjatk.MPR_Project.configuration;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class PdfConfiguration {

    @Bean
    public PDDocument pdDocument() {
        return new PDDocument();
    }

    @Bean
    public PDPageContentStream pdPageContentStream(PDDocument document) throws IOException {
        PDPage page = new PDPage();
        document.addPage(page);
        return new PDPageContentStream(document, page);
    }

}

