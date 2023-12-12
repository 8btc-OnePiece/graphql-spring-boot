package graphql.kickstart.playground.boot;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = PlaygroundTestConfig.class)
@AutoConfigureMockMvc
@ActiveProfiles("playground")
public class PlaygroundCSRFTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldLoadCSRFData() throws Exception {
        final MvcResult mvcResult = mockMvc.perform(get(PlaygroundTestHelper.DEFAULT_PLAYGROUND_ENDPOINT))
                .andExpect(status().isOk())
                .andReturn();
    }
}
