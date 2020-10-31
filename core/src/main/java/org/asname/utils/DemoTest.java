package org.asname.utils;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.asname.dao.AccountSessionDAO;
//import org.asname.integration.requests.CancelRequestRqType;
import org.asname.model.ATMTypeType;
import org.asname.model.ClientATM;
import org.asname.model.ReportType;
import org.asname.model.Request;
import org.asname.service.ClientATMService;
import org.asname.service.ReportRequestsDetailedService;
import org.asname.service.ReportService;
import org.asname.service.RequestService;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

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

/*        CancelRequestRqType cancelRequestRq = new CancelRequestRqType();
        cancelRequestRq.setRequestUUID(UUID.randomUUID().toString());
        cancelRequestRq.setComment("Comment");

//        JAXBContext context = JAXBContext.newInstance(CancelRequestRqType.class);
        JAXBContext context = JAXBContext.newInstance();
        Marshaller m = context.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        m.marshal(cancelRequestRq, baos);

        System.out.println(baos.toString());*/

        /*ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());

        Unmarshaller um = context.createUnmarshaller();
        CancelRequestRqType cancelRequestRqRead = (CancelRequestRqType) um.unmarshal(bais);
        String str = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                "<ns2:CancelRequestRq xmlns:ns2=\"http://ru.crud.requests\">\n" +
                "    <RequestUUID>d6faad1f-bdb1-4738-a548-90fc18d745201</RequestUUID>\n" +
                "    <Comment>Comment</Comment>\n" +
                "</ns2:CancelRequestRq>";

        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = sf.newSchema(new File("src\\org\\crudservlet\\integration\\requests\\schema\\CRUDRequests" +
                ".xsd"));

        JAXBContext jc = JAXBContext.newInstance(CancelRequestRqType.class);
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        unmarshaller.setSchema(schema);
        cancelRequestRqRead = (CancelRequestRqType) unmarshaller.unmarshal(new ByteArrayInputStream(str.getBytes()));
        System.out.println("");*/





    }

}
