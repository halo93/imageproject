package com.company.imageproject.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Properties specific to Imageproject.
 * <p>
 * Properties are configured in the {@code application.yml} file.
 * See {@link io.github.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
@Getter
@Setter
public class ApplicationProperties {

    @NotNull
    private Elasticsearch elasticsearch;

    @Getter
    @Setter
    static class Elasticsearch {

        @NotBlank
        private String host;
    }
}
