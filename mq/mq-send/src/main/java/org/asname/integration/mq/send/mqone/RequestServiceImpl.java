package org.asname.integration.mq.send.mqone;

import org.asname.audit.dao.RequestAuditsDAO;
import org.asname.audit.model.AuditOperType;
import org.asname.audit.service.AuditService;
import org.asname.integration.contract.requests.mq.*;
import org.asname.integration.mq.MessageConverter;
import org.asname.integration.mq.send.JmsSender;
import org.asname.integration.utils.model.MethodType;
import org.asname.integration.utils.service.IntegrationService;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;

import java.util.UUID;

import static org.asname.audit.model.SystemType.ASNAME2;

@Service
public class RequestServiceImpl implements RequestService {

    protected final static String pathSchema = "mq/mq-contract/src/main/java/org/asname/integration/contract/requests/mq/Requests.xsd";

    @Override
    public void createRequestRs(CreateRequestRsType res) {
        try {
            new JmsSender().sendMessage(res.getRqUID(), res.getCorrelationUID(),
                    new MessageConverter(new CreateRequestRsType(), pathSchema).marshal(res),
                    MethodType.CreateRequestRs);
        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void cancelRequestRs(CancelRequestRsType res) {
        try {
            new JmsSender().sendMessage(res.getRqUID(), res.getCorrelationUID(),
                    new MessageConverter(new CancelRequestRsType(), pathSchema).marshal(res),
                    MethodType.CreateRequestRs);
        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void notifyRequestStatusRq(NotifyRequestStatusRqType req, int requestId) {
        try {

            req.setRqUID(UUID.randomUUID().toString());
            req.setRqTm(new IntegrationService().getXMLGregorianCalendar(new java.util.Date()));

            new JmsSender().sendMessage(req.getRqUID(), req.getCorrelationUID(),
                    new MessageConverter(new NotifyRequestStatusRqType(), pathSchema).marshal(req),
                    MethodType.CreateRequestRs);

            Integer auditId = new AuditService().create(
                    AuditOperType.MQ_IN_MESSAGE,
                    ASNAME2.getId(),
                    new java.util.Date(),
                    String.format("Передача текущего статуса заявки\n" +
                                    "UID сообщения: %s \n" +
                                    "UID заявки: %s \n",
                            req.getRqUID(),
                            req.getNotifyRequestStatusRequest().getRequestUUID()),
                    req.getRqUID()
            );

            new RequestAuditsDAO().create(requestId, auditId);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}