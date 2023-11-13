package com.we_write;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = "com.we_write")
@EntityScan(basePackages = "com.we_write.entity")
@EnableJpaRepositories(basePackages = "com.we_write.repository")
public class CollaborativeBlogVersionControlSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(CollaborativeBlogVersionControlSystemApplication.class, args);
	}

}
