package me.training.reactivejava.config;

import io.r2dbc.spi.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;

/**
 * @author Mukut bhattacharjee
 */
@Slf4j
@TestConfiguration
public class TestAppConfig {

    @Bean
    public ConnectionFactoryInitializer initializer(ConnectionFactory connectionFactory) {
        var initializer = new ConnectionFactoryInitializer();
        initializer.setConnectionFactory(connectionFactory);
        log.info("Setting up test database");
        initializer.setDatabasePopulator(new ResourceDatabasePopulator(
                new ClassPathResource("sql/schema-test.sql")
        ));
        return initializer;
    }
}
