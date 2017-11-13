package ru.ifmo.pashaac.treii;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@SpringBootApplication
public class TreiiApplication {

	public static void main(String[] args) {
        new SpringApplicationBuilder(TreiiApplication.class)
                .headless(false)
                .run(args);
    }

	@Bean
	public Docket newsApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.useDefaultResponseMessages(false)
				.groupName("treii")
				.apiInfo(apiInfo())
				.select()
				.apis(RequestHandlerSelectors.basePackage("ru.ifmo.pashaac.treii"))
				.build();
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("TREII")
				.description("Travel and real estate industries intercommunication")
				.contact(new Contact("Pavel Asadchiy", "localhost", "localhost@gmail.com"))
				.version("1.0.0")
				.build();
	}

	/**
	 * Needs for dev server to work.
     * TODO: should be removed on production for security reasons.
	 */
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurerAdapter() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").allowedMethods(
						HttpMethod.GET.name(),
						HttpMethod.POST.name(),
						HttpMethod.PUT.name(),
						HttpMethod.DELETE.name()
				);
			}
		};
	}
}
