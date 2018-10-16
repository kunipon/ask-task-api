package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

@SpringBootApplication
@PropertySource("classpath:properties/social.properties")
@EntityScan(basePackageClasses = {SpringSocialDemoApplication.class, Jsr310JpaConverters.class})
public class SpringSocialDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringSocialDemoApplication.class, args);
	}
}
