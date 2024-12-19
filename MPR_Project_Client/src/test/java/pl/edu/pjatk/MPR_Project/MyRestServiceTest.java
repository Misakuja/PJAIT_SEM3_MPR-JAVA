package pl.edu.pjatk.MPR_Project;

import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.web.client.MockServerRestClientCustomizer;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.web.client.RestClient;
import pl.edu.pjatk.MPR_Project.exception.CapybaraAlreadyExists;
import pl.edu.pjatk.MPR_Project.exception.CapybaraNotFoundException;
import pl.edu.pjatk.MPR_Project.exception.InvalidInputCapybaraException;
import pl.edu.pjatk.MPR_Project.model.Capybara;
import pl.edu.pjatk.MPR_Project.service.MyRestService;
import pl.edu.pjatk.MPR_Project.service.StringService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

@RestClientTest
public class MyRestServiceTest {

    MockServerRestClientCustomizer customizer = new MockServerRestClientCustomizer();
    RestClient.Builder builder = RestClient.builder();

    private MyRestService myRestService;
    @Mock
    private StringService stringService;

    @BeforeEach
    public void setUp() {
        customizer.customize(builder);
        myRestService = new MyRestService(stringService, builder.defaultStatusHandler(
                        HttpStatusCode::is4xxClientError,
                        (request, response) -> {
                            HttpStatus status = HttpStatus.valueOf(response.getStatusCode().value());
                            switch (status) {
                                case CONFLICT -> throw new CapybaraAlreadyExists();
                                case NOT_FOUND -> throw new CapybaraNotFoundException();
                                case BAD_REQUEST -> throw new InvalidInputCapybaraException();
                                default -> throw new RuntimeException("Unexpected status: " + status);
                            }
                        }
                )
                .build());
    }

    private RestClient buildCustomConfig() {
        return RestClient.builder()
                .defaultStatusHandler(
                        HttpStatusCode::is4xxClientError,
                        (request, response) -> {
                            HttpStatus status = HttpStatus.valueOf(response.getStatusCode().value());
                            switch (status) {
                                case CONFLICT -> throw new CapybaraAlreadyExists();
                                case NOT_FOUND -> throw new CapybaraNotFoundException();
                                case BAD_REQUEST -> throw new InvalidInputCapybaraException();
                                default -> throw new RuntimeException("Unexpected status: " + status);
                            }
                        }
                )
                .build();
    }


    @Test
    public void setIdentificationTest() {
        Capybara capybara = new Capybara("Test", 2);

        capybara.setIdentification();

        assertEquals(102, capybara.getIdentification());
    }

    @Test
    public void getCapybaraByIdTest() {
        customizer.getServer().expect(MockRestRequestMatchers.requestTo("/capybara/find/id/1"))
                .andRespond(MockRestResponseCreators.withSuccess("""
                        {"id":1,"name":"Test","age":2}
                        """, MediaType.APPLICATION_JSON));

        Capybara capybara = myRestService.getById(1L);

        assertEquals("Test", capybara.getName());
        assertEquals(2, capybara.getAge());
    }

    @Test
    public void getCapybaraByAgeTest() {
        customizer.getServer().expect(MockRestRequestMatchers.requestTo("/capybara/find/age/2"))
                .andRespond(MockRestResponseCreators.withSuccess("""
                        [{"id":1,"name":"Test","age":2}]
                        """, MediaType.APPLICATION_JSON));

        List<Capybara> capybaraList = myRestService.getByAge(2);

        assertEquals(1, capybaraList.size());
        assertEquals("Test", capybaraList.getFirst().getName());
    }

    @Test
    public void getCapybaraByNameTest() {
        customizer.getServer().expect(MockRestRequestMatchers.requestTo("/capybara/find/name/TEST"))
                .andRespond(MockRestResponseCreators.withSuccess("""
                        [{"id":1,"name":"TEST","age":2}]
                        """, MediaType.APPLICATION_JSON));

        List<Capybara> capybaraList = myRestService.getByName("TEST");

        assertEquals(1, capybaraList.size());
        assertEquals("TEST", capybaraList.getFirst().getName());
    }

    @Test
    public void deleteCapybaraByIdTest() {
        customizer.getServer().expect(MockRestRequestMatchers.requestTo("/capybara/delete/1"))
                .andRespond(MockRestResponseCreators.withSuccess("", MediaType.APPLICATION_JSON));

        myRestService.deleteCapybaraById(1L);
    }

    @Test
    public void patchCapybaraByIdTest() {
        Capybara existingCapybara = new Capybara("Test", 2);
        customizer.getServer().expect(MockRestRequestMatchers.requestTo("/capybara/patch/2"))
                .andRespond(MockRestResponseCreators.withSuccess("", MediaType.APPLICATION_JSON));

        myRestService.patchCapybaraById(existingCapybara, 2L);
    }

    @Test
    public void addCapybaraTest() {
        Capybara capybara = new Capybara("Test", 2);
        customizer.getServer().expect(MockRestRequestMatchers.requestTo("/capybara/add"))
                .andRespond(MockRestResponseCreators.withSuccess("", MediaType.APPLICATION_JSON));

        myRestService.addCapybara(capybara);
    }
    @Test
    public void getAllCapybaraObjectsTest() {
        customizer.getServer().expect(MockRestRequestMatchers.requestTo("/"))
                .andRespond(MockRestResponseCreators.withSuccess("""
                        [{"id":1,"name":"Test1","age":2},{"id":2,"name":"Test2","age":3}]
                        """, MediaType.APPLICATION_JSON));

        List<Capybara> capybaraList = myRestService.getAllCapybaraObjects();
        assertEquals(2, capybaraList.size());
        assertEquals("Test1", capybaraList.getFirst().getName());
        assertEquals("Test2", capybaraList.get(1).getName());
    }

    @Test
    public void addCapybaraThrowsExceptionInvalidInputCapybaraDueToAgeTest() {
        Capybara capybara = new Capybara("Test", 0);
        customizer.getServer().expect(MockRestRequestMatchers.requestTo("/capybara/add"))
                .andRespond(MockRestResponseCreators.withStatus(HttpStatus.BAD_REQUEST));

        assertThrows(InvalidInputCapybaraException.class, () -> myRestService.addCapybara(capybara));
    }
    @Test
    public void addCapybaraThrowsExceptionInvalidInputCapybaraDueToNameTest() {
        Capybara capybara = new Capybara("", 2);
        customizer.getServer().expect(MockRestRequestMatchers.requestTo("/capybara/add"))
                .andRespond(MockRestResponseCreators.withStatus(HttpStatus.BAD_REQUEST));

        assertThrows(InvalidInputCapybaraException.class, () -> myRestService.addCapybara(capybara));
    }

    @Test
    public void addCapybaraThrowsExceptionCapybaraAlreadyExistsTest() {
        Capybara capybara = new Capybara("Test", 2);
        customizer.getServer().expect(MockRestRequestMatchers.requestTo("/capybara/add"))
                .andRespond(MockRestResponseCreators.withStatus(HttpStatus.CONFLICT));

        assertThrows(CapybaraAlreadyExists.class, () -> myRestService.addCapybara(capybara));
    }

    @Test
    public void patchCapybaraThrowsExceptionCapybaraNotFoundTest() throws CapybaraNotFoundException {
        long id = 2L;
        customizer.getServer().expect(MockRestRequestMatchers.requestTo("/capybara/patch/" + id))
                .andRespond(MockRestResponseCreators.withStatus(HttpStatus.NOT_FOUND));

        assertThrows(CapybaraNotFoundException.class, () -> myRestService.patchCapybaraById(new Capybara("name", 2), id));
    }

    @Test
    public void patchCapybaraThrowsExceptionInvalidInputCapybaraDueToNameTest() {
        Capybara capybaraToSwitchTo = new Capybara("", 2);
        Capybara existingCapybara = new Capybara("Test2", 3);

        customizer.getServer().expect(MockRestRequestMatchers.requestTo("/capybara/patch/2"))
                .andRespond(MockRestResponseCreators.withStatus(HttpStatus.OK));

        assertThrows(InvalidInputCapybaraException.class, () -> myRestService.patchCapybaraById(capybaraToSwitchTo, 2L));
    }

    @Test
    public void patchCapybaraThrowsExceptionInvalidInputCapybaraDueToAgeTest() {
        Capybara capybaraToSwitchTo = new Capybara("Test", 0);
        Capybara existingCapybara = new Capybara("Test", 3);

        customizer.getServer().expect(MockRestRequestMatchers.requestTo("/capybara/patch/2"))
                .andRespond(MockRestResponseCreators.withStatus(HttpStatus.OK));

        assertThrows(InvalidInputCapybaraException.class, () -> myRestService.patchCapybaraById(capybaraToSwitchTo, 2L));
    }

    @Test
    public void patchCapybaraThrowsExceptionCapybaraAlreadyExistsTest() {
        Capybara capybara = new Capybara("Test", 2);
        Capybara existingCapybara = new Capybara("Test", 2);

        customizer.getServer().expect(MockRestRequestMatchers.requestTo("/capybara/patch/10"))
                .andRespond(MockRestResponseCreators.withStatus(HttpStatus.CONFLICT));

        assertThrows(CapybaraAlreadyExists.class, () -> myRestService.patchCapybaraById(existingCapybara, 10L));
    }

    @Test
    public void deleteCapybaraThrowsExceptionCapybaraNotFoundTest() {
        long id = 2L;

        customizer.getServer().expect(MockRestRequestMatchers.requestTo("/capybara/delete/" + id))
                .andRespond(MockRestResponseCreators.withStatus(HttpStatus.NOT_FOUND));

        assertThrows(CapybaraNotFoundException.class, () -> myRestService.deleteCapybaraById(id));
    }

    @Test
    public void getByNameThrowsExceptionCapybaraNotFoundTest() {
        String name = "TEST";

        customizer.getServer().expect(MockRestRequestMatchers.requestTo("/capybara/find/name/" + name))
                .andRespond(MockRestResponseCreators.withStatus(HttpStatus.NOT_FOUND));

        assertThrows(CapybaraNotFoundException.class, () -> myRestService.getByName(name));
    }

    @Test
    public void getByAgeThrowsExceptionCapybaraNotFoundTest() {
        int age = 2;

        customizer.getServer().expect(MockRestRequestMatchers.requestTo("/capybara/find/age/" + age))
                .andRespond(MockRestResponseCreators.withStatus(HttpStatus.NOT_FOUND));

        assertThrows(CapybaraNotFoundException.class, () -> myRestService.getByAge(age));
    }

    @Test
    public void getByIdThrowsExceptionCapybaraNotFoundTest() {
        long id = 2L;

        customizer.getServer().expect(MockRestRequestMatchers.requestTo("/capybara/find/id/" + id))
                .andRespond(MockRestResponseCreators.withStatus(HttpStatus.NOT_FOUND));

        assertThrows(CapybaraNotFoundException.class, () -> myRestService.getById(id));
    }

    @Test
    public void getAllCapybaraObjectsThrowsExceptionCapybaraNotFoundTest() throws CapybaraNotFoundException {
        customizer.getServer().expect(MockRestRequestMatchers.requestTo("/"))
                .andRespond(MockRestResponseCreators.withSuccess("[]", MediaType.APPLICATION_JSON));

        assertThrows(CapybaraNotFoundException.class, () -> myRestService.getAllCapybaraObjects());
    }

    @Test
    public void getInformationOfCapybaraByIdThrowsExceptionCapybaraNotFoundTest() throws CapybaraNotFoundException {
        long id = 1L;

        customizer.getServer().expect(MockRestRequestMatchers.requestTo("/capybara/get/information/" + id))
                .andRespond(MockRestResponseCreators.withStatus(HttpStatus.NOT_FOUND));

        assertThrows(CapybaraNotFoundException.class, () -> myRestService.getInformationOfCapybaraById(id, mock(HttpServletResponse.class)));
    }

}