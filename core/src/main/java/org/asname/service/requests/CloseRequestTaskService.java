package org.asname.service.requests;

import org.asname.model.requests.Request;
import org.asname.model.requests.RequestStatusType;
import org.asname.model.tasks.Task;
import org.asname.model.tasks.TaskStatusType;
import org.asname.service.TaskService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static org.asname.model.tasks.TaskType.CLOSE_REQUEST;

public class CloseRequestTaskService implements Runnable {

    private Logger logger = Logger.getLogger(CloseRequestTaskService.class.getName());

    @Override
    public void run() {
        logger.info("start");
        try {
            Task task = new Task();
            task.setType(CLOSE_REQUEST);
            task.setCreateDateTime(new Date());
            task.setCreateDate(task.getCreateDateTime());
            task.setPlannedStartDateTime(task.getCreateDateTime());
            task.setStatus(TaskStatusType.CREATED);
            task.setUserAccountId(-1);

            Integer taskId = new TaskService().create(task, -1, 0);

            new TaskService().start(taskId);

            ArrayList<Request> requests = new RequestService().getRequests(
                    new java.sql.Date(new Date().getTime() - (1000 * 60 * 60 * 24)),
                    new java.sql.Date(new Date().getTime()),
                    null,
                    RequestStatusType.CANCELED
            );

            final int[] count_requests = {0};

            requests.stream()
                    .forEach(x -> {
                        try {
                            new RequestService().close(
                                    x.getRequestUUID(),
                                    -1,
                                    "Закрыто автоматически"
                            );
                            count_requests[0] = count_requests[0] + 1;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });


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
