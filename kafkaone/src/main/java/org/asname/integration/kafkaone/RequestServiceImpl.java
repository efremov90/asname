package org.asname.integration.kafkaone;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.asname.audit.dao.RequestAuditsDAO;
import org.asname.audit.model.AuditOperType;
import org.asname.audit.model.SystemType;
import org.asname.audit.service.AuditService;
import org.asname.integration.contract.requests.kafkaone.*;
import org.asname.integration.utils.model.MethodType;
import org.asname.integration.utils.service.IntegrationService;
import org.asname.model.requests.Request;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Date;
import java.util.UUID;
import java.util.logging.Logger;

import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;

@Service
public class RequestServiceImpl implements RequestService {

    private Logger logger = Logger.getLogger(RequestServiceImpl.class.getName());

    //C:\Users\NEO\.IntelliJIdea2019.3\system\tomcat
    protected final static String pathSchema = "/Requests.json";
    @Override
    public void createRequestRq(CreateRequestRq req) throws IOException {
        Integer auditId = null;
        Integer requestId = null;
        try {
            try {

                auditId = new AuditService().create(
                        AuditOperType.MQ_IN_MESSAGE,
                        SystemType.ASNAME3.getId(),
                        new java.util.Date(),
                        String.format("Создание заявки\n" +
                                        "UID сообщения: %s \n" +
                                        "UID заявки: %s \n",
                                req.getHeader().getRqUID(),
                                req.getCreateRequest().getRequestUUID()),
                        req.getHeader().getRqUID()
                );

                CreateRequestRs res = new CreateRequestRs();
                Header header = new Header();
                header.setRqUID(UUID.randomUUID().toString());
                header.setCorrelationUID(req.getHeader().getRqUID());
                header.setRqTm(new java.util.Date());
                Status statusType = new Status();
                statusType.setCode(0);
                res.setHeader(header);
                res.setStatus(statusType);
                new RequestServiceImpl().createRequestRs(res,null);

            } catch (Exception e) {

                CreateRequestRs res = new CreateRequestRs();
                Header header = new Header();
                header.setRqUID(UUID.randomUUID().toString());
                header.setCorrelationUID(req.getHeader().getRqUID());
                header.setRqTm(new java.util.Date());
                Status statusType = new Status();
                statusType.setCode(-1);
                statusType.setDescription(new IntegrationService().getExceptionString(e));
                res.setHeader(header);
                res.setStatus(statusType);
                new RequestServiceImpl().createRequestRs(res,e);

                throw new Exception(e);
            }

            Request request = new Request();
            request.setRequestUUID(req.getCreateRequest().getRequestUUID());
            request.setCreateDate(Date.valueOf(req.getCreateRequest().getCreateDate().toString()));
            request.setCreateDateTime(new java.util.Date());
            request.setClientCode(req.getCreateRequest().getClientCode());
            request.setComment(req.getCreateRequest().getComment());
            request.setLastUserAccountIdChangeRequestStatus(SystemType.ASNAME3.getId());
            request.setCreateSystemId(SystemType.ASNAME3.getId());
            requestId = new org.asname.service.requests.RequestService().create(request,
                    SystemType.ASNAME3.getId());

            new RequestAuditsDAO().create(requestId, auditId);

        } catch (Exception e) {
            e.printStackTrace();
            NotifyRequestStatusRq notify = new NotifyRequestStatusRq();
            Header header = new Header();
            header.setRqUID(UUID.randomUUID().toString());
            header.setCorrelationUID(req.getHeader().getRqUID());
            header.setRqTm(new java.util.Date());
            NotifyRequestStatusRequest notifyRequest = new NotifyRequestStatusRequest();
            notifyRequest.setRequestUUID(req.getCreateRequest().getRequestUUID());
            notifyRequest.setStatus(NotifyRequestStatusRequest.Status.ERROR);
            notifyRequest.setComment(e.getMessage());
            notify.setHeader(header);
            notify.setNotifyRequestStatusRequest(notifyRequest);
            new RequestServiceImpl().notifyRequestStatusRq(notify,-1,e);
        }
    }

    @Override
    public void cancelRequestRq(CancelRequestRq req) throws IOException {
        Integer auditId = null;
        Integer requestId = null;
        try {
            try {

                auditId = new AuditService().create(
                        AuditOperType.MQ_IN_MESSAGE,
                        SystemType.ASNAME3.getId(),
                        new java.util.Date(),
                        String.format("Отмена заявки\n" +
                                        "UID сообщения: %s \n" +
                                        "UID заявки: %s \n",
                                req.getHeader().getRqUID(),
                                req.getCancelRequest().getRequestUUID()),
                        req.getHeader().getRqUID()
                );

                CancelRequestRs res = new CancelRequestRs();
                Header header = new Header();
                header.setRqUID(UUID.randomUUID().toString());
                header.setCorrelationUID(req.getHeader().getRqUID());
                header.setRqTm(new java.util.Date());
                Status statusType = new Status();
                statusType.setCode(0);
                res.setHeader(header);
                res.setStatus(statusType);
                new RequestServiceImpl().cancelRequestRs(res,null);

            } catch (Exception e) {

                CancelRequestRs res = new CancelRequestRs();
                Header header = new Header();
                header.setRqUID(UUID.randomUUID().toString());
                header.setCorrelationUID(req.getHeader().getRqUID());
                header.setRqTm(new java.util.Date());
                Status statusType = new Status();
                statusType.setCode(-1);
                statusType.setDescription(new IntegrationService().getExceptionString(e));
                res.setHeader(header);
                res.setStatus(statusType);
                new RequestServiceImpl().cancelRequestRs(res,e);

                throw new Exception(e);
            }

            requestId =
                    new org.asname.service.requests.RequestService().getRequestIdByUUID(req.getCancelRequest().getRequestUUID());
            if (requestId != null) new RequestAuditsDAO().create(requestId, auditId);

            new org.asname.service.requests.RequestService().cancel(req.getCancelRequest().getRequestUUID(),
                    SystemType.ASNAME3.getId(),
                    req.getCancelRequest().getComment());

        } catch (Exception e) {
            e.printStackTrace();
            NotifyRequestStatusRq notify = new NotifyRequestStatusRq();
            Header header = new Header();
            header.setRqUID(UUID.randomUUID().toString());
            header.setCorrelationUID(req.getHeader().getRqUID());
            header.setRqTm(new java.util.Date());
            NotifyRequestStatusRequest notifyRequest = new NotifyRequestStatusRequest();
            notifyRequest.setRequestUUID(req.getCancelRequest().getRequestUUID());
            notifyRequest.setStatus(NotifyRequestStatusRequest.Status.ERROR);
            notifyRequest.setComment(e.getMessage());
            notify.setHeader(header);
            notify.setNotifyRequestStatusRequest(notifyRequest);
            new RequestServiceImpl().notifyRequestStatusRq(notify,-1,e);
        }
    }

    @Override
    public void createRequestRs(CreateRequestRs createRequestRs, Exception exception) throws Exception {

        ApplicationContext ctx = new AnnotationConfigApplicationContext(KafkaConfig.class);
        KafkaSender kafkaSender = ctx.getBean(KafkaSender.class);
        kafkaSender.setDestination(DestinationsType.OUT.getDescription());
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(WRITE_DATES_AS_TIMESTAMPS);
        Requests res = new Requests();
        res.setCreateRequestRs(createRequestRs);
        kafkaSender.send(createRequestRs.getHeader().getRqUID(), createRequestRs.getHeader().getCorrelationUID(),
                mapper.writeValueAsString(res),
                MethodType.CreateRequestRs,
                exception);
    }

    @Override
    public void cancelRequestRs(CancelRequestRs cancelRequestRs, Exception exception) throws Exception {

        ApplicationContext ctx = new AnnotationConfigApplicationContext(KafkaConfig.class);
        KafkaSender kafkaSender = ctx.getBean(KafkaSender.class);
        kafkaSender.setDestination(DestinationsType.OUT.getDescription());
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(WRITE_DATES_AS_TIMESTAMPS);
        Requests res = new Requests();
        res.setCancelRequestRs(cancelRequestRs);
        kafkaSender.send(cancelRequestRs.getHeader().getRqUID(), cancelRequestRs.getHeader().getCorrelationUID(),
                mapper.writeValueAsString(res),
                MethodType.CancelRequestRs,
                exception);
//        } catch (JAXBException e) {
//            e.printStackTrace();
//        } catch (SAXException e) {
//            e.printStackTrace();
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void notifyRequestStatusRq(NotifyRequestStatusRq req, int requestId, Exception exception) {
        try {

            Header header = new Header();
            header.setRqUID(UUID.randomUUID().toString());
            header.setRqTm(new java.util.Date());

            ApplicationContext ctx = new AnnotationConfigApplicationContext(KafkaConfig.class);
            KafkaSender kafkaSender = ctx.getBean(KafkaSender.class);
            kafkaSender.setDestination(DestinationsType.OUT.getDescription());
            ObjectMapper mapper = new ObjectMapper();
            kafkaSender.send(req.getHeader().getRqUID(), req.getHeader().getCorrelationUID(),
                    mapper.writeValueAsString(req),
                    MethodType.NotifyRequestStatusRq,
                    exception);

            Integer auditId = new AuditService().create(
                    AuditOperType.MQ_REQUEST_MESSAGE,
                    SystemType.ASNAME.getId(),
                    new java.util.Date(),
                    String.format("Передача текущего статуса заявки\n" +
                                    "UID сообщения: %s \n" +
                                    "UID заявки: %s \n",
                            req.getHeader().getRqUID(),
                            req.getNotifyRequestStatusRequest().getRequestUUID()),
                    req.getHeader().getRqUID()
            );

            if (requestId > 0) new RequestAuditsDAO().create(requestId, auditId);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}