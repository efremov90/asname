package org.asname.integration.kafkaone;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import java.util.logging.Logger;

public class ApplicationInitializer implements WebApplicationInitializer {

    private Logger logger = Logger.getLogger(ApplicationInitializer.class.getName());

    public void onStartup(ServletContext container) throws ServletException {

        AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
        ctx.register(KafkaConfig.class);
        ctx.setServletContext(container);
        ServletRegistration.Dynamic servlet = container.addServlet("dispatcher2", new DispatcherServlet(ctx));
        servlet.setLoadOnStartup(1);
        servlet.addMapping("/");
    }
}