package com.dcc.osheaapp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class OsheaApplication {

  private static final Logger LOGGER = LogManager.getLogger(OsheaApplication.class);

  public static void main(String[] args) {
    SpringApplication.run(OsheaApplication.class, args);
    LOGGER.info("Oshea Application started......");
  }

  //    // http://localhost:9090/swagger-ui/index.html
  //    @Bean
  //    public OpenAPI customOpenAPI(@Value("${application-description}") String appDesciption,
  //                                 @Value("${application-version}") String appVersion) {
  //        return new OpenAPI()
  //                .info(new Info()
  //                        .title("USMk API Hub Security")
  //                        .version(appVersion)
  //                        .description(appDesciption)
  //                        .termsOfService("http://swagger.io/terms/")
  //                        .license(new License().name("Apache 2.0").url("http://springdoc.org")));
  //    }
}
