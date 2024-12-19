package pl.edu.pjatk.MPR_Project;

import jakarta.servlet.http.HttpServletResponse;
import org.apache.pdfbox.Loader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.web.client.MockServerRestClientCustomizer;
import org.springframework.http.HttpStatus;
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

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
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
        myRestService = new MyRestService(stringService, builder.build());
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
        assertThrows(InvalidInputCapybaraException.class, () -> myRestService.addCapybara(capybara));
    }
    @Test
    public void addCapybaraThrowsExceptionInvalidInputCapybaraDueToNameTest() {
        Capybara capybara = new Capybara("", 2);
        assertThrows(InvalidInputCapybaraException.class, () -> myRestService.addCapybara(capybara));
    }

    @Test
    public void addCapybaraThrowsExceptionCapybaraAlreadyExistsTest() {
        Capybara capybara = new Capybara("Test", 2);
        capybara.setIdentification();

        long existingIdentification = capybara.getIdentification();

        customizer.getServer().expect(MockRestRequestMatchers.requestTo("/capybara/find/identification/" + existingIdentification))
                .andRespond(MockRestResponseCreators.withSuccess("""
                    {"id":1,"name":"Test","age":2,"identification":102}
                    """, MediaType.APPLICATION_JSON));

        assertThrows(CapybaraAlreadyExists.class, () -> myRestService.addCapybara(capybara));
    }

    @Test //TODO: AssertionError instead of capy not found
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

        customizer.getServer().expect(MockRestRequestMatchers.requestTo("/capybara/find/identification/" + capybara.getIdentification()))
                .andRespond(MockRestResponseCreators.withSuccess("""
                        {"id":1,"name":"Test","age":2}
                        """, MediaType.APPLICATION_JSON));

        assertThrows(CapybaraAlreadyExists.class, () -> myRestService.patchCapybaraById(existingCapybara, 10L));
    }

    @Test //TODO: FIX HTTP CLIENT NOT_FOUND INSTEAD OF CAPYBARA NOT FOUND
    public void deleteCapybaraThrowsExceptionCapybaraNotFoundTest() {
        long id = 2L;

        customizer.getServer().expect(MockRestRequestMatchers.requestTo("/capybara/find/id/" + id))
                .andRespond(MockRestResponseCreators.withStatus(HttpStatus.NOT_FOUND));

        assertThrows(CapybaraNotFoundException.class, () -> myRestService.deleteCapybaraById(id));
    }

    @Test //TODO: FIX HTTP CLIENT NOT_FOUND INSTEAD OF CAPYBARA NOT FOUND
    public void getByNameThrowsExceptionCapybaraNotFoundTest() {
        String name = "TEST";

        customizer.getServer().expect(MockRestRequestMatchers.requestTo("/capybara/find/name/" + name))
                .andRespond(MockRestResponseCreators.withStatus(HttpStatus.NOT_FOUND));

        assertThrows(CapybaraNotFoundException.class, () -> myRestService.getByName(name));
    }

    @Test //TODO: FIX HTTP CLIENT NOT_FOUND INSTEAD OF CAPYBARA NOT FOUND
    public void getByAgeThrowsExceptionCapybaraNotFoundTest() {
        int age = 2;

        customizer.getServer().expect(MockRestRequestMatchers.requestTo("/capybara/find/age/" + age))
                .andRespond(MockRestResponseCreators.withStatus(HttpStatus.NOT_FOUND));

        assertThrows(CapybaraNotFoundException.class, () -> myRestService.getByAge(age));
    }

    @Test //TODO: FIX HTTP CLIENT NOT_FOUND INSTEAD OF CAPYBARA NOT FOUND
    public void getByIdThrowsExceptionCapybaraNotFoundTest() {
        long id = 2L;

        customizer.getServer().expect(MockRestRequestMatchers.requestTo("/capybara/find/id/" + id))
                .andRespond(MockRestResponseCreators.withStatus(HttpStatus.NOT_FOUND));

        assertThrows(CapybaraNotFoundException.class, () -> myRestService.getById(id));
    }

    @Test //TODO: FIX, Doesn't throw anything? why?
    public void getAllCapybaraObjectsThrowsExceptionCapybaraNotFoundTest() throws CapybaraNotFoundException {
        customizer.getServer().expect(MockRestRequestMatchers.requestTo("/"))
                .andRespond(MockRestResponseCreators.withSuccess("[]", MediaType.APPLICATION_JSON));

        assertThrows(CapybaraNotFoundException.class, () -> myRestService.getAllCapybaraObjects());
    }

    @Test //TODO: AssertionError instead of capy not found
    public void getInformationOfCapybaraByIdThrowsExceptionCapybaraNotFoundTest() throws CapybaraNotFoundException {
        long id = 1L;

        customizer.getServer().expect(MockRestRequestMatchers.requestTo("/capybara/find/id/" + id))
                .andRespond(MockRestResponseCreators.withStatus(HttpStatus.NOT_FOUND));

        assertThrows(CapybaraNotFoundException.class, () -> myRestService.getInformationOfCapybaraById(id, mock(HttpServletResponse.class)));
    }

    @Test //TODO: Assertion error :(
    public void getInformationOfCapybaraByIdThrowsRuntimeExceptionByIOExceptionTest() throws IOException {
        long id = 1L;
        Capybara capybara = new Capybara();

        customizer.getServer().expect(MockRestRequestMatchers.requestTo("/capybara/find/id/" + id))
                .andRespond(MockRestResponseCreators.withSuccess("", MediaType.APPLICATION_JSON));

        doThrow(new IOException("IOException Mock")).when(mock(Loader.class));

        assertThrows(IOException.class, () -> myRestService.getInformationOfCapybaraById(id, mock(HttpServletResponse.class)));
    }

    @Test //TODO: Assertion error :(
    public void getInformationOfCapybaraByIdThrowsRuntimeExceptionByIllegalAccessExceptionTest() throws IllegalAccessException {
        long id = 1L;

        customizer.getServer().expect(MockRestRequestMatchers.requestTo("/capybara/find/id/" + id))
                .andRespond(MockRestResponseCreators.withSuccess("", MediaType.APPLICATION_JSON));

        Field mockField = mock(Field.class);
        doThrow(new IllegalAccessException("IllegalAccessException Mock")).when(mockField).get(any());


        assertThrows(IllegalAccessException.class, () -> myRestService.getInformationOfCapybaraById(id, mock(HttpServletResponse.class)));
    }
}