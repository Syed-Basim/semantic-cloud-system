package com.example.rest_api_build.semantic.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.example.rest_api_build.repository",
        entityManagerFactoryRef = "semanticEntityManagerFactory",
        transactionManagerRef = "semanticTransactionManager"
)
@EntityScan(
        basePackages = "com.example.rest_api_build.entity"
)
public class SemanticDataSourceConfig {

    @Bean(name = "semanticDataSource")
    public DataSource semanticDataSource() {

        return DataSourceBuilder.create()
                .driverClassName("org.postgresql.Driver")
                .url("jdbc:postgresql://localhost:5432/semantic_cloud_db")
                .username("postgres")
                .password("root")
                .build();
    }

    @Bean(name = "semanticEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean semanticEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("semanticDataSource") DataSource dataSource
    ) {

        return builder
                .dataSource(dataSource)
                .packages("com.example.rest_api_build.entity")
                .persistenceUnit("semantic")
                .build();
    }

    @Bean(name = "semanticTransactionManager")
    public PlatformTransactionManager semanticTransactionManager(
            @Qualifier("semanticEntityManagerFactory")
            EntityManagerFactory entityManagerFactory
    ) {

        return new JpaTransactionManager(entityManagerFactory);
    }
}