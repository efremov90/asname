package org.asname.model.integration;

import java.util.Date;

public class MQLog {
    private int Id;
    private String RqUID;
    private String CorrelationUID;
    private Date CreateDatetime;
    private DirectionType Direction;
    private String Content;
    private MethodType Method;
    private StatusType Status;
    private String Destination;
    private String Error;

    public MQLog() {
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getRqUID() {
        return RqUID;
    }

    public void setRqUID(String rqUID) {
        RqUID = rqUID;
    }

    public String getCorrelationUID() {
        return CorrelationUID;
    }

    public void setCorrelationUID(String correlationUID) {
        CorrelationUID = correlationUID;
    }

    public Date getCreateDatetime() {
        return CreateDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        CreateDatetime = createDatetime;
    }

    public DirectionType getDirection() {
        return Direction;
    }

    public void setDirection(DirectionType direction) {
        Direction = direction;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
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

    public String getDestination() {
        return Destination;
    }

    public void setDestination(String destination) {
        Destination = destination;
    }

    public String getError() {
        return Error;
    }

    public void setError(String error) {
        Error = error;
    }
}
