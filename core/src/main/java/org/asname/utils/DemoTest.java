package org.asname.utils;

//import org.asname.integration.requests.CancelRequestRqType;
import org.asname.integration.mq.MessageConverter;
import org.asname.integration.requests.ws.CancelRequestRqType;
import org.springframework.jms.support.converter.MarshallingMessageConverter;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
        import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
        import java.io.*;
        import java.util.UUID;

public class DemoTest {
    public static void main(String[] args) throws Exception {

        //Заявка
        /*Request request = new Request();
        String uuid = UUID.randomUUID().toString();
        request.setRequestUUID(uuid);
        request.setCreateDate(Date.valueOf(LocalDate.now()));
        request.setCreateDateTime(new java.util.Date());
        request.setClientCode("1001");
        request.setComment("Комментарий");
        new RequestService().create(request, 2);
        new RequestService().cancel(uuid, 2, "Причина отмены");*/

        //Клиент
        /*ClientATM client = new ClientATM();
        client.setClientCode("2005");
        client.setClientName("N 2005");
        client.setAtmType(ATMTypeType.valueOf("ATM"));
        client.setAddress("A 2005");
        client.setCloseDate(null);
        new ClientATMService().edit(client, 2);*/

//        Request r = ;

        /*Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("fromCreateDate", Date.valueOf("2020-08-01").toString());
        parameters.put("toCreateDate", Date.valueOf("2020-09-01").toString());

        new ReportService().generate(
                ReportType.REPORT_REQUESTS_DETAILED,
                parameters,
                new JRBeanCollectionDataSource(
                        new ReportRequestsDetailedService().getData(
                                Date.valueOf("2020-08-01"),
                                Date.valueOf("2020-09-01"),
                                "1001"
                        )
                ));*/

        CancelRequestRqType CancelRequestRq = new CancelRequestRqType();
        CancelRequestRq.setRequestUUID(UUID.randomUUID().toString());
        CancelRequestRq.setComment("Comment");

        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = sf.newSchema(new File("core/src/main/java/org/asname/integration/requests/ws/Requests.xsd"));


        JAXBContext context = JAXBContext.newInstance(CancelRequestRqType.class);
//        JAXBContext context = JAXBContext.newInstance();
        Marshaller m = context.createMarshaller();
        m.setSchema(schema);
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        m.marshal(CancelRequestRq, baos);

        System.out.println(baos.toString());

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());

        Unmarshaller um = context.createUnmarshaller();
        CancelRequestRqType cancelRequestRqRead = (CancelRequestRqType) um.unmarshal(bais);
        String str = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                "<sch:CancelRequestRq xmlns:sch=\"http://org.asname.requests/schemas\">\n" +
                "  <RequestUUID>stringstringstringstringstringstring</RequestUUID>\n" +
                "  <Comment>string</Comment>\n" +
                "</sch:CancelRequestRq>";

//        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
//        Schema schema = sf.newSchema(new File("core/src/main/java/org/asname/integration/requests/Requests.xsd"));

/*        JAXBContext jc = JAXBContext.newInstance(CancelRequestRqType.class);
//        JAXBContext jc = JAXBContext.newInstance();
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        unmarshaller.setSchema(schema);
        cancelRequestRqRead = (CancelRequestRqType) unmarshaller.unmarshal(new ByteArrayInputStream(str.getBytes()));
        Object object = unmarshaller.unmarshal(new ByteArrayInputStream(str.getBytes()));
        System.out.println("");*/
        MessageConverter messageConverter = new MessageConverter(new CancelRequestRqType(),
                "core/src/main/java/org/asname/integration/requests/ws/Requests.xsd");
        messageConverter.unmarshal(str);
    }

}
