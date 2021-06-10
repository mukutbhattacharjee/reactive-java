package me.training.reactivejava.config;

import io.r2dbc.spi.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;

/**
 * @author Mukut bhattacharjee
 */
@Slf4j
@Configuration
public class AppConfig {

    @Bean
    @ConditionalOnMissingBean
    public ConnectionFactoryInitializer initializer(ConnectionFactory connectionFactory) {

        var initializer = new ConnectionFactoryInitializer();
        initializer.setConnectionFactory(connectionFactory);
        log.info("Setting up database");
        initializer.setDatabasePopulator(new ResourceDatabasePopulator(
                new ClassPathResource("sql/schema.sql"),
                new ClassPathResource("sql/data.sql"))
        );
        return initializer;
    }
}
