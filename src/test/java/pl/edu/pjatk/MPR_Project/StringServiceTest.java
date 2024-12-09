package pl.edu.pjatk.MPR_Project;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import pl.edu.pjatk.MPR_Project.service.StringService;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class StringServiceTest {

    @Test
    public void uppercaseTest() {
        StringService stringService = new StringService();
        String s = "testString";

        assertEquals("TESTSTRING", stringService.uppercase(s));
    }

    @Test
    public void lowercaseTest() {
        StringService stringService = new StringService();
        String s = "TESTSTRING";

        assertEquals("Teststring", stringService.lowercase(s));
    }
}
