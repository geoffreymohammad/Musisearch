package com.musisearch.rest.configuration;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.builders.PathSelectors.regex;
@Configuration
@EnableSwagger2
public class SpringFoxConfig {

	@Bean
	public Docket apiDocket() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.any())
				.paths(regex("/api.*"))
				.build()
				.apiInfo(getApiInfo());
	}
	
	



	private ApiInfo getApiInfo() {
		return new ApiInfo("ANDROID MUSISEARCH REST", "DESCRIPTION", "v0.0.1-SNAPSHOT", "TERMS OF SERVICE URL",
				new Contact("NAME", "URL", "EMAIL"), "LICENSE", "LICENSE URL", Collections.emptyList());
	}
}
