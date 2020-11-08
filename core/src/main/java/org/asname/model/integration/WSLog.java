package org.asname.model.integration;

import org.asname.integration.utils.model.MethodType;

import java.util.Date;

public class WSLog {
    private int Id;
    private DirectionType Direction;
    private String Request;
    private String Response;
    private MethodType Method;
    private StatusType Status;
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

    public DirectionType getDirection() {
        return Direction;
    }

    public void setDirection(DirectionType direction) {
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

    public MethodType getMethod() {
        return Method;
    }

    public void setMethod(MethodType method) {
        Method = method;
    }

    public StatusType getStatus() {
        return Status;
    }

    public void setStatus(StatusType status) {
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
