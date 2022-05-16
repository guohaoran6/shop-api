package io.recruitment.assessment.api.configuration;

import io.github.resilience4j.common.circuitbreaker.configuration.CircuitBreakerConfigCustomizer;
import org.springframework.context.annotation.Bean;

import java.time.Duration;

import static io.github.resilience4j.circuitbreaker.CircuitBreakerConfig.SlidingWindowType.COUNT_BASED;

public class CircuitBreakerConfig {
    @Bean
    public CircuitBreakerConfigCustomizer testCustomizer() {
        return CircuitBreakerConfigCustomizer.of("generateOrderService", builder -> builder
                .slidingWindowSize(100)
                .slidingWindowType(COUNT_BASED)
                .failureRateThreshold(50)
                .minimumNumberOfCalls(30)
                .waitDurationInOpenState(Duration.ofMillis(1800000)));
    }
}
