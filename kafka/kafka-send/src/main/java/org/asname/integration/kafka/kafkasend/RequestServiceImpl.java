package org.asname.integration.kafka.kafkasend;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.asname.audit.dao.RequestAuditsDAO;
import org.asname.audit.model.AuditOperType;
import org.asname.audit.model.SystemType;
import org.asname.audit.service.AuditService;
import org.asname.integration.contract.requests.kafka.*;
import org.asname.integration.utils.model.MethodType;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.logging.Logger;

import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;

@Service
public class RequestServiceImpl implements RequestService {

    private Logger logger = Logger.getLogger(RequestServiceImpl.class.getName());

    @Override
    public void createRequestRs(CreateRequestRs createRequestRs, Exception exception) throws Exception {

        Header header = createRequestRs.getHeader();
        header.setRqUID(UUID.randomUUID().toString());
        header.setRqTm(new java.util.Date());
        createRequestRs.setHeader(header);

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

        Header header = cancelRequestRs.getHeader();
        header.setRqUID(UUID.randomUUID().toString());
        header.setRqTm(new java.util.Date());
        cancelRequestRs.setHeader(header);

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
    public void notifyRequestStatusRq(NotifyRequestStatusRq notifyRequestStatusRq, int requestId, Exception exception) {
        try {

            Header header = (notifyRequestStatusRq.getHeader()!=null) ? notifyRequestStatusRq.getHeader() : new Header();
            header.setRqUID(UUID.randomUUID().toString());
            header.setRqTm(new java.util.Date());
            notifyRequestStatusRq.setHeader(header);

            ApplicationContext ctx = new AnnotationConfigApplicationContext(KafkaConfig.class);
            KafkaSender kafkaSender = ctx.getBean(KafkaSender.class);
            kafkaSender.setDestination(DestinationsType.OUT.getDescription());
            ObjectMapper mapper = new ObjectMapper();
            mapper.disable(WRITE_DATES_AS_TIMESTAMPS);
            Requests req = new Requests();
            req.setNotifyRequestStatusRq(notifyRequestStatusRq);
            kafkaSender.send(notifyRequestStatusRq.getHeader().getRqUID(), notifyRequestStatusRq.getHeader().getCorrelationUID(),
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
                            notifyRequestStatusRq.getHeader().getRqUID(),
                            notifyRequestStatusRq.getNotifyRequestStatusRequest().getRequestUUID()),
                    notifyRequestStatusRq.getHeader().getRqUID()
            );

            if (requestId > 0) new RequestAuditsDAO().create(requestId, auditId);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}