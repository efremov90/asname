package org.asname.model;

import java.util.Date;

public class WSLog {
    private int Id;
    private WSLogDirectionType Direction;
    private String Request;
    private String Response;
    private String Method;
    private WSLogStatusType Status;
    private Date StartDatetime;
    private Date EndDatetime;

    public WSLog() {
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public WSLogDirectionType getDirection() {
        return Direction;
    }

    public void setDirection(WSLogDirectionType direction) {
        Direction = direction;
    }

    public String getRequest() {
        return Request;
    }

    public void setRequest(String request) {
        Request = request;
    }

    public String getResponse() {
        return Response;
    }

    public void setResponse(String response) {
        Response = response;
    }

    public String getMethod() {
        return Method;
    }

    public void setMethod(String method) {
        Method = method;
    }

    public WSLogStatusType getStatus() {
        return Status;
    }

    public void setStatus(WSLogStatusType status) {
        Status = status;
    }

    public Date getStartDatetime() {
        return StartDatetime;
    }

    public void setStartDatetime(Date startDatetime) {
        StartDatetime = startDatetime;
    }

    public Date getEndDatetime() {
        return EndDatetime;
    }

    public void setEndDatetime(Date endDatetime) {
        EndDatetime = endDatetime;
    }
}
