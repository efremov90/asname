package org.asname.model.reports;

public enum ReportType {

    REPORT_REQUESTS_DETAILED("Отчет по заявкам (детальный)"),
    REPORT_REQUESTS_CONSOLIDATED("Отчет по заявкам (сводный)");

    private String Description;

    ReportType(String description) {
        Description = description;
    }

    public String getDescription() {
        return Description;
    }
}
