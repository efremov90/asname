package org.asname.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.asname.dao.ReportDAO;
import org.asname.dao.ReportTasksDAO;
import org.asname.dao.RequestDAO;
import org.asname.dao.RequestTasksDAO;
import org.asname.dto.ReportRequestsConsolidatedDTO;
import org.asname.dto.ReportRequestsDetailedDTO;
import org.asname.model.FormatReportType;
import org.asname.model.ReportType;
import org.asname.reportbean.ReportRequestsDetailedBean;

import java.io.ByteArrayOutputStream;
import java.sql.Blob;
import java.sql.Date;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class RequestTaskService implements Runnable {

    private Logger logger = Logger.getLogger(RequestTaskService.class.getName());

    private int requestId;

    public RequestTaskService(int requestId) {
        this.requestId = requestId;
    }

    @Override
    public void run() {
        logger.info("start");
        try {
            int taskId = new RequestTasksDAO().getTaskByRequest(requestId);
            new TaskService().start(taskId);
            new RequestService().cancel(
                    new RequestService().getRequestById(requestId).getRequestUUID(),
                    -1,
                    "Отменено автоматически"
            );
            new TaskService().finish(taskId);
        } catch (Exception e) {
            e.printStackTrace();
            try {
                new TaskService().error(1,
                        e.getMessage() + "\n" +
                                e.toString() + "\n" +
                                Arrays.stream(e.getStackTrace()).map(x -> x.toString()).collect(Collectors.joining("\n"))
                );
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        logger.info("finish");
    }
}
