package org.asname.task;

import org.asname.service.CloseRequestTaskService;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.sql.SQLException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.asname.model.Configures.CLOSE_REQUEST_INTERVAL;

@WebListener
public class StartTask implements ServletContextListener {
    private ScheduledExecutorService scheduledExecutorService;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        this.scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        try {
            this.scheduledExecutorService.scheduleAtFixedRate(
                    new CloseRequestTaskService(),
                    0,
                    Integer.valueOf(CLOSE_REQUEST_INTERVAL.getValue()),
                    TimeUnit.SECONDS);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        scheduledExecutorService.shutdownNow();
    }
}
