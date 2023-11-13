package com.we_write.config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Configuration
@EnableJpaRepositories(basePackages = "com.we_write.repository")
@EnableTransactionManagement
public class JpaConfig {

    @Autowired
    private JpaProperties jpaProperties;  // Inject JpaProperties

//    @Bean
//    public JpaProperties jpaProperties() {
//        return new JpaProperties();  // Create and return an instance of JpaProperties
//    }

    
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder, DataSource dataSource) {
        Map<String, Object> properties = new HashMap<>(jpaProperties.getProperties());
        return builder
                .dataSource(dataSource)
                .packages("com.we_write.entity")
                .persistenceUnit("yourPersistenceUnitName")
                .properties(properties)
                .build();
    }
//    @Bean
//    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
//            EntityManagerFactoryBuilder builder, DataSource dataSource) {
//        return builder
//                .dataSource(dataSource)
//                .packages("com.we_write.entity")
//                .persistenceUnit("yourPersistenceUnitName")
//                .properties(jpaProperties.getProperties())
//                .build();
//    }
//    @Bean(name = "jpaSharedEM_entityManagerFactory")
//    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
//            EntityManagerFactoryBuilder builder,
//            DataSource dataSource,
//            HibernateProperties hibernateProperties) {
//        return builder
//                .dataSource(dataSource)
//                .packages("com.we_write.entity")
//                .persistenceUnit("yourPersistenceUnitName")
//                .properties(hibernateProperties.determineHibernateProperties(null, new HibernateSettings()))
//                .build();
//    }
}

//@Configuration
//@EnableJpaRepositories(basePackages = "com.we_write.repository", entityManagerFactoryRef = "jpaSharedEM_entityManagerFactory")
//
//@EnableTransactionManagement
//public class JpaConfig {
//    @Bean
//    public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder, DataSource dataSource) {
//        return builder
//                .dataSource(dataSource)
//                .packages("com.we_write.entity") // package containing your entities
//                .persistenceUnit("yourPersistenceUnitName")
//                .build();
//    }
//}
