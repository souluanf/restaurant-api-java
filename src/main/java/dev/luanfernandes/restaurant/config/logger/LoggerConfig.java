package dev.luanfernandes.restaurant.config.logger;

import static java.util.Objects.requireNonNull;
import static org.slf4j.LoggerFactory.getLogger;

import org.slf4j.Logger;
import org.springframework.beans.factory.InjectionPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class LoggerConfig {

    @Bean
    @Scope("prototype")
    public Logger logger(InjectionPoint injectionPoint) {
        return getLogger(requireNonNull(injectionPoint.getMethodParameter()).getContainingClass());
    }
}
