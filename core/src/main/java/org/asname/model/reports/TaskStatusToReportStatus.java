package org.asname.model.reports;

import org.asname.model.tasks.TaskStatusType;

import java.util.EnumMap;

public class TaskStatusToReportStatus {

    private EnumMap<TaskStatusType, ReportStatusType> taskToReport;

    public TaskStatusToReportStatus() {
        taskToReport.put(TaskStatusType.CREATED, ReportStatusType.CREATED);
        taskToReport.put(TaskStatusType.STARTED, ReportStatusType.STARTED);
        taskToReport.put(TaskStatusType.FINISH, ReportStatusType.FINISH);
    }

    public ReportStatusType getStatus(TaskStatusType taskStatusType) {
        return taskToReport.get(taskStatusType);
    }
}
