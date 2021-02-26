package org.asname.integration.mq.send.mqone;

import org.asname.audit.dao.RequestAuditsDAO;
import org.asname.audit.model.AuditOperType;
import org.asname.audit.service.AuditService;
import org.asname.integration.contract.requests.mq.*;
import org.asname.integration.mq.MessageConverter;
import org.asname.integration.mq.send.JmsConfigASNAME1;
import org.asname.integration.mq.send.JmsSender;
import org.asname.integration.utils.model.MethodType;
import org.asname.integration.utils.service.IntegrationService;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static org.asname.audit.model.SystemType.ASNAME;
import static org.asname.integration.mq.DestinationsType.AS1_OUT;
import static org.asname.integration.mq.DestinationsType.AS1_REQUEST;

@Service
public class RequestServiceImpl implements RequestService {

    protected final static String pathSchema = "/Requests.xsd";

    @Override
    public void createRequestRs(CreateRequestRsType res, Exception exception) throws Exception {
        JmsSender jmsSender = new JmsSender();
        jmsSender.setDestination(AS1_OUT.getDescription());
        jmsSender.setJmsTemplate(JmsConfigASNAME1.getJmsTemplate());
//        try {
        jmsSender.sendMessage(res.getRqUID(), res.getCorrelationUID(),
                    new MessageConverter(new CreateRequestRsType(), pathSchema).marshal(res),
                    MethodType.CreateRequestRs,
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
    public void cancelRequestRs(CancelRequestRsType res, Exception exception) throws Exception {
        JmsSender jmsSender = new JmsSender();
        jmsSender.setDestination(AS1_OUT.getDescription());
        jmsSender.setJmsTemplate(JmsConfigASNAME1.getJmsTemplate());
//        try {
        jmsSender.sendMessage(res.getRqUID(), res.getCorrelationUID(),
                    new MessageConverter(new CancelRequestRsType(), pathSchema).marshal(res),
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
    public void notifyRequestStatusRq(NotifyRequestStatusRqType req, int requestId, Exception exception) {
        try {

            req.setRqUID(UUID.randomUUID().toString());
            req.setRqTm(new IntegrationService().getXMLGregorianCalendar(new java.util.Date()));

            JmsSender jmsSender = new JmsSender();
            jmsSender.setDestination(AS1_REQUEST.getDescription());
            jmsSender.setJmsTemplate(JmsConfigASNAME1.getJmsTemplate());
            jmsSender.sendMessage(req.getRqUID(), req.getCorrelationUID(),
                    new MessageConverter(new NotifyRequestStatusRqType(), pathSchema).marshal(req),
                    MethodType.NotifyRequestStatusRq,
                    exception);

            Integer auditId = new AuditService().create(
                    AuditOperType.MQ_REQUEST_MESSAGE,
                    ASNAME.getId(),
                    new java.util.Date(),
                    String.format("Передача текущего статуса заявки\n" +
                                    "UID сообщения: %s \n" +
                                    "UID заявки: %s \n",
                            req.getRqUID(),
                            req.getNotifyRequestStatusRequest().getRequestUUID()),
                    req.getRqUID()
            );

            if (requestId > 0) new RequestAuditsDAO().create(requestId, auditId);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}