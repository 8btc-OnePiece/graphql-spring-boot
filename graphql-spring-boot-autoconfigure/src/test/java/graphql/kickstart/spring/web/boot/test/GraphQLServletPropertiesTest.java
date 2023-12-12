package graphql.kickstart.spring.web.boot.test;

import graphql.kickstart.spring.web.boot.GraphQLServletProperties;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Andrew Potter
 */
public class GraphQLServletPropertiesTest {

    @Test
    public void getServletMappingReturnsAppropriateWildcardValue() {
        verifyServletMapping("/graphql");
        verifyServletMapping("/graphql/");
        verifyServletMapping("/graphql/*");
        verifyServletMapping("/graphql/**");
    }

    @Test
    public void getCorsMappingReturnsAppropriateWildcardValue() {
        verifyCorsMapping("/graphql");
        verifyCorsMapping("/graphql/");
        verifyCorsMapping("/graphql/*");
        verifyCorsMapping("/graphql/**");
    }

    private void verifyCorsMapping(String mapping) {
        GraphQLServletProperties servletProperties = new GraphQLServletProperties();
        servletProperties.setMapping(mapping);

        assertThat(servletProperties.getCorsMapping())
                .as(
                        String.format(
                                "Expected mapping '%s' to return cors mapping '%s'", mapping, "/graphql/**"))
                .isEqualTo("/graphql/**");
    }

    private void verifyServletMapping(String mapping) {
        GraphQLServletProperties servletProperties = new GraphQLServletProperties();
        servletProperties.setMapping(mapping);

        assertThat(servletProperties.getServletMapping())
                .as(
                        String.format(
                                "Expected mapping '%s' to return servlet mapping '%s'", mapping, "/graphql/*"))
                .isEqualTo("/graphql/*");
    }
}
