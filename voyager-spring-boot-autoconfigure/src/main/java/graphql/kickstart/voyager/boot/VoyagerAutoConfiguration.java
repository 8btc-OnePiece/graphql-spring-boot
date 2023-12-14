package graphql.kickstart.voyager.boot;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;

/**
 * @author Guilherme Blanco
 */
@AutoConfiguration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnProperty(value = "voyager.enabled", havingValue = "true", matchIfMissing = true)
public class VoyagerAutoConfiguration {
    @Bean
    VoyagerController voyagerController() {
        return new VoyagerController();
    }

    @Bean
    VoyagerIndexHtmlTemplate voyagerIndexHtmlTemplate() {
        return new VoyagerIndexHtmlTemplate();
    }
}
