package graphql.kickstart.graphiql.boot;

import graphql.kickstart.graphiql.boot.test.AbstractAutoConfigurationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

@WebFluxTest
public class ReactiveGraphiQLControllerTest  extends AbstractAutoConfigurationTest {

    @Autowired
    private WebTestClient webTestClient;
    public ReactiveGraphiQLControllerTest() {
        super(AnnotationConfigWebApplicationContext.class, GraphiQLAutoConfiguration.class);
    }

    @Test
    public void shouldBeAbleToAccessGraphiQL() {
      webTestClient.get()
          .uri("/graphiql")
          .exchange()
          .expectStatus().is2xxSuccessful()
          .expectHeader().contentType(MediaType.TEXT_HTML);
    }

    @SpringBootConfiguration
    @TestPropertySource(properties = "graphiql.enabled=true")
    @Import(GraphiQLAutoConfiguration.class)
    public static class ReactiveTestApplication {
    }
}
