package org.asname.mvc.controllers;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"org.asname.mvc.*","org.asname.entity.*","org.asname.service.*","org.asname.repository"})
public class MvcConfig extends WebMvcConfigurerAdapter {

}
