package org.asname.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.asname.dao.AccountSessionDAO;
import org.asname.dao.ClientATMDAO;
import org.asname.dao.ClientDopofficeDAO;
import org.asname.dto.*;
import org.asname.model.ATMTypeType;
import org.asname.model.Client;
import org.asname.model.ClientATM;
import org.asname.model.ClientDopoffice;
import org.asname.service.ClientATMService;
import org.asname.service.ClientDopofficeService;
import org.asname.service.ErrorDTOService;
import org.asname.service.ResultDTOService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.util.Arrays;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@WebServlet(urlPatterns = {"/editClient"})
public class EditClientServlet extends HttpServlet {

    private static final long serialVersionUID = 6011853190168045751L;

    private Logger logger = Logger.getLogger(EditClientServlet.class.getName());

    public EditClientServlet() {
        super();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        logger.info("start");
        try {
            ObjectMapper mapper = new ObjectMapper();
            EditClientRequestDTO editClientDTO = mapper.readValue(req.getReader(), EditClientRequestDTO.class);
            if (editClientDTO.getDopoffice() != null) {
                ClientDopofficeDTO clientDTO = editClientDTO.getDopoffice();
                ClientDopoffice client = new ClientDopoffice();
                client.setId(clientDTO.getId());
                client.setClientCode(clientDTO.getClientCode());
                client.setClientName(clientDTO.getClientName());
                client.setAddress(clientDTO.getAddress());
                client.setCloseDate((clientDTO.getCloseDate() != null && clientDTO.getCloseDate() != "") ?
                        Date.valueOf(clientDTO.getCloseDate()) : null);
                new ClientDopofficeService().edit(client,
                        new AccountSessionDAO().getAccountSessionBySessionId(req.getRequestedSessionId()).getUserAccountId());
                ResultDTOService.writer(resp, "0", null);
            } else if (editClientDTO.getSelfservice() != null) {
                ClientATMDTO clientDTO = editClientDTO.getSelfservice();
                ClientATM client = new ClientATM();
                client.setId(clientDTO.getId());
                client.setClientCode(clientDTO.getClientCode());
                client.setClientName(clientDTO.getClientName());
                client.setAtmType(ATMTypeType.valueOf(clientDTO.getATMType()));
                client.setAddress(clientDTO.getAddress());
                client.setCloseDate((clientDTO.getCloseDate() != null && clientDTO.getCloseDate() != "") ?
                        Date.valueOf(clientDTO.getCloseDate()) : null);
                new ClientATMService().edit(client,
                        new AccountSessionDAO().getAccountSessionBySessionId(req.getRequestedSessionId()).getUserAccountId());
                ResultDTOService.writer(resp, "0", null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            ErrorDTOService.writer(resp, e);
        }
        return;
    }

}
