package com.company.imageproject.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Configuration
@EnableJpaRepositories("com.company.imageproject.repository")
@EnableJpaAuditing
@EnableTransactionManagement
@EnableElasticsearchRepositories("com.company.imageproject.repository.search")
public class DatabaseConfiguration {
}
