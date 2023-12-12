package graphql.kickstart.voyager.boot.test;

import graphql.kickstart.voyager.boot.ReactiveVoyagerAutoConfiguration;
import graphql.kickstart.voyager.boot.ReactiveVoyagerController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.boot.web.reactive.context.AnnotationConfigReactiveWebApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import java.lang.instrument.Instrumentation;

import static org.assertj.core.api.Assertions.*;

/**
 * @author Max Günther
 */
public class ReactiveVoyagerControllerTest extends AbstractAutoConfigurationTest {

    public ReactiveVoyagerControllerTest() {
        super(AnnotationConfigReactiveWebApplicationContext.class, ReactiveVoyagerAutoConfiguration.class);
    }

    @Configuration
    @PropertySource("classpath:enabled-config.properties")
    static class EnabledConfiguration {
        @Bean
        public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
            return new PropertySourcesPlaceholderConfigurer();
        }
    }

    @Configuration
    @PropertySource("classpath:disabled-config.properties")
    static class DisabledConfiguration {
        @Bean
        public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
            return new PropertySourcesPlaceholderConfigurer();
        }
    }

    @Test
    public void voyagerLoads() {
        load(EnabledConfiguration.class);
        assertThat(this.getContext().getBean(ReactiveVoyagerController.class)).isNull();

    }

    @Test
    public void voyagerDoesNotLoad() {
        load(DisabledConfiguration.class);

        AbstractApplicationContext context = getContext();

        assertThatExceptionOfType(NoSuchBeanDefinitionException.class).isThrownBy(() -> context.getBean(Instrumentation.class));
    }
}
