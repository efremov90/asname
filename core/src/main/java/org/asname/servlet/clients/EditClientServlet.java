package org.asname.servlet.clients;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.asname.dao.users.AccountSessionDAO;
import org.asname.dto.clients.ClientATMDTO;
import org.asname.dto.clients.ClientDopofficeDTO;
import org.asname.dto.clients.EditClientRequestDTO;
import org.asname.model.clients.ATMTypeType;
import org.asname.model.clients.ClientATM;
import org.asname.model.clients.ClientDopoffice;
import org.asname.service.clients.ClientATMService;
import org.asname.service.clients.ClientDopofficeService;
import org.asname.service.servlet.ErrorDTOService;
import org.asname.service.servlet.ResultDTOService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.util.logging.Logger;

//@WebServlet(urlPatterns = {"/editClient"})
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
