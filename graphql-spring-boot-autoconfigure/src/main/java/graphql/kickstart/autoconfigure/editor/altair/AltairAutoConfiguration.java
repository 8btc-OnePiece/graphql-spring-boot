package graphql.kickstart.autoconfigure.editor.altair;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.DispatcherServlet;

/** @author Moncef AOUDIA */
@AutoConfiguration
@ConditionalOnWebApplication
@EnableConfigurationProperties({AltairProperties.class, AltairOptions.class, AltairResources.class})
@ConditionalOnClass(DispatcherServlet.class)
public class AltairAutoConfiguration {

  @Bean
  @ConditionalOnProperty(value = "graphql.altair.enabled", havingValue = "true")
  AltairController altairController(
      AltairProperties altairProperties,
      AltairOptions altairOptions,
      AltairResources altairResources) {
    return new AltairController(altairProperties, altairOptions, altairResources);
  }
}
