package pl.edu.pjatk.MPR_Project.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestClient;
import pl.edu.pjatk.MPR_Project.exception.CapybaraAlreadyExists;
import pl.edu.pjatk.MPR_Project.exception.CapybaraNotFoundException;
import pl.edu.pjatk.MPR_Project.exception.InvalidInputCapybaraException;

@ComponentScan
@Configuration
public class RestClientConfig {
    @Bean
    public RestClient getRestClient() {
        return RestClient.builder()
                .baseUrl("http://localhost:8082/server")
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
}
