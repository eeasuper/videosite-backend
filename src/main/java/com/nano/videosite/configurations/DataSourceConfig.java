package com.nano.videosite.configurations;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class DataSourceConfig {


    @Bean
    @Profile("postgres")
    public DataSource postgresDataSource() {
        String databaseUrl = System.getenv("DATABASE_URL");
        System.out.println("Initializing PostgreSQL database: " + databaseUrl);

        URI dbUri;
        try {
            dbUri = new URI(databaseUrl);
        }
        catch (URISyntaxException e) {
        	System.out.println(String.format("Invalid DATABASE_URL: %s", databaseUrl));
        	System.out.println(e);
            return null;
        }

        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' 
                       + dbUri.getPort() + dbUri.getPath();

        org.apache.tomcat.jdbc.pool.DataSource dataSource 
            = new org.apache.tomcat.jdbc.pool.DataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl(dbUrl);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setTestOnBorrow(true);
        dataSource.setTestWhileIdle(true);
        dataSource.setTestOnReturn(true);
        dataSource.setValidationQuery("SELECT 1");
        return dataSource;
    }

}