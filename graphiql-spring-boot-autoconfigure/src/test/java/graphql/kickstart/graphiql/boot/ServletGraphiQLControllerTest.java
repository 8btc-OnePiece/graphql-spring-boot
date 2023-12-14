package graphql.kickstart.graphiql.boot;

import graphql.kickstart.graphiql.boot.test.AbstractAutoConfigurationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class ServletGraphiQLControllerTest  extends AbstractAutoConfigurationTest {

    @Autowired
    private MockMvc mockMvc;

    public ServletGraphiQLControllerTest() {
        super(AnnotationConfigWebApplicationContext.class,GraphiQLAutoConfiguration.class);
    }

    @Test
    public void shouldBeAbleToAccessGraphiQL() throws Exception {
      mockMvc.perform(get("/graphiql"))
          .andExpect(status().is2xxSuccessful())
          .andExpect(content().contentType("text/html; charset=UTF-8"));
    }

    @SpringBootConfiguration
    @TestPropertySource(properties = "graphiql.enabled=true")
    @Import(GraphiQLAutoConfiguration.class)
    public static class ServletTestApplication {
    }
}
