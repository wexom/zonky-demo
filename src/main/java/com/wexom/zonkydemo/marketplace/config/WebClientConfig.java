package com.wexom.zonkydemo.marketplace.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Configuration class for web client.
 */
@Configuration
public class WebClientConfig {

    @Value("${web.client.config.zonky-base-url}")
    private String zonkyBaseUrl;

    @Value("${web.client.application-name}")
    private String applicationName;

    @Value("${web.client.application-build-version}")
    private String applicationBuildVersion;

    @Value("${web.client.application-organization-url}")
    private String applicationOrganizationUrl;

    /**
     * Initialization of {@link org.springframework.web.reactive.function.client.WebClient} bean.
     *
     * @return configured bean of {@link org.springframework.web.reactive.function.client.WebClient} with User-Agent default header and base URL to Zonky API.
     */
    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl(zonkyBaseUrl)
                .defaultHeader("User-Agent", String.format(
                        "%s/%s (%s)",
                        applicationName,
                        applicationBuildVersion,
                        applicationOrganizationUrl
                        )
                )
                .build();
    }

    /**
     * Initialization of {@link com.fasterxml.jackson.databind.ObjectMapper} bean.
     *
     * @param builder jackson to object mapper builder
     * @return {@link com.fasterxml.jackson.databind.ObjectMapper} bean configured to be able to use Java 8 Time objects during the parsing process
     */
    @Bean
    @Primary
    public ObjectMapper objectMapper(final Jackson2ObjectMapperBuilder builder) {
        return builder.build()
                .registerModule(new ParameterNamesModule())
                .registerModule(new Jdk8Module())
                .registerModule(new JavaTimeModule());
    }
}
