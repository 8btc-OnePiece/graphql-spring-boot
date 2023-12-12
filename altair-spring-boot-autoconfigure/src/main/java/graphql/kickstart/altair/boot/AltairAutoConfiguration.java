package graphql.kickstart.altair.boot;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * @author Moncef AOUDIA
 */
@AutoConfiguration
@ConditionalOnWebApplication
@EnableConfigurationProperties(AltairProperties.class)
@ConditionalOnClass(DispatcherServlet.class)
public class AltairAutoConfiguration {
    @Bean
    @ConditionalOnProperty(value = "altair.enabled", havingValue = "true", matchIfMissing = true)
    AltairController altairController() {
        return new AltairController();
    }
}
