package io.recruitment.assessment.api;

import com.google.common.eventbus.EventBus;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@ComponentScan(basePackages = "io.recruitment.assessment")
@EnableSwagger2
@EnableAsync
@Slf4j
public class Application {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
        return modelMapper;
    }

    @Bean
    public EventBus getEventBus() { return new EventBus(); }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
