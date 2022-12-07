package com.binarair.binarairrestapi.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class SwaggerConfiguration {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Binar Air RESTful API")
                        .description("OpenAPI for Binar Air RESTful API")
                        .version("1.0.0")
                        .contact(
                                new Contact()
                                        .name("BE Kelompok 3")
                                        .email("qq.khoiri@gmail.com")
                                        .url("https://github.com/qyu4x")
                        )
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")))
                .externalDocs(new ExternalDocumentation()
                        .description("github Binar Air RESTful API")
                        .url("github.com/qyu4x/binar-air-rest-api.git"))
                .servers(servers())

                ;
    }

    private List<Server> servers() {
        List<Server> servers = new ArrayList<>();

        Server serverDevelopment = new Server();
        serverDevelopment.setUrl("http://localhost:8080/");
        serverDevelopment.setDescription("Main server for Development");

        Server serverProduction = new Server();
        serverProduction.setUrl("https://binar-air-rest-api-production.up.railway.app/");
        serverProduction.setDescription("Main server for Production");

        servers.add(serverDevelopment);
        servers.add(serverProduction);
        return servers;
    }

}
