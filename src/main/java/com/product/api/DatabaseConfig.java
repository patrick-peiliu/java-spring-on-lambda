package com.product.api;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {
    // @Bean
    // @ConditionalOnProperty(name = "spring.profiles.active", havingValue = "local", matchIfMissing = true)
    // public DataSource localDataSource() {
    //     // Configure an in-memory database or a local database
    //     return new EmbeddedDatabaseBuilder()
    //             .setType(EmbeddedDatabaseType.H2)
    //             .build();
    // }

    // @Bean
    // @ConditionalOnProperty(name = "spring.profiles.active", havingValue = "aws")
    // public DataSource awsDataSource() {
    //     // Configure AWS RDS or other cloud database
    //     return DataSourceBuilder.create()
    //             .url("jdbc:postgresql://aws-rds-endpoint:5432/productapi")
    //             .username("postgres")
    //             .password("password")
    //             .build();
    // }

    // @Bean
    // @ConditionalOnProperty(name = "spring.profiles.active", havingValue = "lambda")
    // public DataSource lambdaDataSource() {
    //     // No database configuration for Lambda
    //     return null;
    // }

    // Placeholder method to indicate that database configurations are disabled
//    public void databaseConfigurationsDisabled() {
//        // Database configurations are currently disabled
//    }
}
