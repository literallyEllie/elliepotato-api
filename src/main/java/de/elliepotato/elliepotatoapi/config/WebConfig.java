package de.elliepotato.elliepotatoapi.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
/*
 Detects the existence of Jackson and JAXB 2 on the classpath,
 and automatically creates and registers default JSON and XML converters
 */
@EnableWebMvc
@ComponentScan(basePackages = "de.elliepotato.elliepotatoapi")
public class WebConfig {
}
