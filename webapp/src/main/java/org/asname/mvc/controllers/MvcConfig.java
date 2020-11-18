package org.asname.mvc.controllers;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.logging.Logger;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "org.asname.mvc.*")
public class MvcConfig extends WebMvcConfigurerAdapter {

//    private Logger logger = Logger.getLogger(MvcConfig.class.getName());

}
