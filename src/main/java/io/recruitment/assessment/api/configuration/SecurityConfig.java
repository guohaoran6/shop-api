package io.recruitment.assessment.api.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.header.writers.StaticHeadersWriter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.requestMatchers().antMatchers(HttpMethod.GET, "/api/v1/**");

        http.headers()
                // Strict-Transport-Security: max-age=31536000; includeSubDomains
                .httpStrictTransportSecurity().and()
                // X-XSS-Protection: 1; mode=block
                .xssProtection().and()
                // X-Content-Type-Options: nosniff
                .contentTypeOptions().and()
                // X-Frame-Options: DENY
                .frameOptions().deny()
                // Content-Security-Policy: frame-ancestors 'none';
                .addHeaderWriter(new StaticHeadersWriter("X-Content-Security-Policy","frame-ancestors 'none'"));
    }
}