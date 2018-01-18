package cn.cnic.trackrecord.web.config;

import cn.cnic.trackrecord.web.config.property.TokenProperties;
import cn.cnic.trackrecord.web.identity.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.ApiKeyVehicle;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static com.google.common.collect.Lists.newArrayList;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Autowired
    private TokenProperties properties;

    ApiInfo apiInfo() {
      return new ApiInfoBuilder()
        .title("API Reference")
        .version("1.0.0")
        .build();
    }

    @Bean
    public Docket customImplementation(){
		return new Docket(DocumentationType.SWAGGER_2)
      .apiInfo(apiInfo())
      .securitySchemes(newArrayList(apiKey()))
      .select().paths(PathSelectors.any())
                .apis(RequestHandlerSelectors.basePackage("cn.cnic.trackrecord.web.api.controller"))
                .build()
                .pathMapping("/")
      ;
    }

    private ApiKey apiKey() {
        //return new ApiKey("Authorization", "api_key", "header");
        return new ApiKey("Authorization", properties.getHeader(), "header");             // <<< === Create a Heaader (We are createing header named "Authorization" here)
    }

    @Bean
    SecurityConfiguration security() {
        //return new SecurityConfiguration("emailSecurity_client", "secret", "Spring", "emailSecurity", "apiKey", ApiKeyVehicle.HEADER, "api_key", ",");
        return new SecurityConfiguration("emailSecurity_client", "secret", "Spring", "emailSecurity", "", ApiKeyVehicle.HEADER, "", ",");
    }
}
