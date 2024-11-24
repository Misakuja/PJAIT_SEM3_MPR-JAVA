package pl.edu.pjatk.MPR_Project.configuration;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
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

    @Bean
    public PDType1Font pdFont() {
        return new PDType1Font(Standard14Fonts.FontName.TIMES_ROMAN);
    }
}

