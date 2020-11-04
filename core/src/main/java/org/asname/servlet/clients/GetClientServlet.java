package org.asname.servlet.clients;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.asname.dto.clients.ClientATMDTO;
import org.asname.dto.clients.ClientDopofficeDTO;
import org.asname.dto.clients.GetClientRequestDTO;
import org.asname.dto.clients.GetClientResponseDTO;
import org.asname.model.clients.ClientATM;
import org.asname.model.clients.ClientDopoffice;
import org.asname.model.clients.ClientTypeType;
import org.asname.service.clients.ClientATMService;
import org.asname.service.clients.ClientDopofficeService;
import org.asname.service.clients.ClientService;
import org.asname.service.servlet.ErrorDTOService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.util.logging.Logger;

@WebServlet(urlPatterns = {"/getClient"})
public class GetClientServlet extends HttpServlet {

    private static final long serialVersionUID = 7980928199907184355L;

    private Logger logger = Logger.getLogger(GetClientServlet.class.getName());

    public GetClientServlet() {
        super();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        logger.info("start");

        try {
            ObjectMapper mapper = new ObjectMapper();
            GetClientRequestDTO getClientRequestDTO = mapper.readValue(req.getReader(), GetClientRequestDTO.class);

            ClientTypeType clientType = new ClientService().getClient(getClientRequestDTO.getClientCode()).getClientType();
            GetClientResponseDTO getClientResponseDTO = new GetClientResponseDTO();
            switch (clientType) {
                case DOPOFFICE:
                    ClientDopoffice clientDopoffice =
                            new ClientDopofficeService().getClientByCode(getClientRequestDTO.getClientCode());
                    ClientDopofficeDTO clientDopofficeDTO = new ClientDopofficeDTO();
                    clientDopofficeDTO.setId(clientDopoffice.getId());
                    clientDopofficeDTO.setClientCode(clientDopoffice.getClientCode());
                    clientDopofficeDTO.setClientName(clientDopoffice.getClientName());
                    clientDopofficeDTO.setAddress(clientDopoffice.getAddress());
                    clientDopofficeDTO.setCloseDate(clientDopoffice.getCloseDate() != null ?
                            new Date(clientDopoffice.getCloseDate().getTime()).toString() :
                            null);
                    getClientResponseDTO.setDopoffice(clientDopofficeDTO);
                    break;
                case SELFSERVICE:
                    ClientATM clientATM =
                            new ClientATMService().getClientByCode(getClientRequestDTO.getClientCode());
                    ClientATMDTO clientATMDTO = new ClientATMDTO();
                    clientATMDTO.setId(clientATM.getId());
                    clientATMDTO.setClientCode(clientATM.getClientCode());
                    clientATMDTO.setClientName(clientATM.getClientName());
                    clientATMDTO.setATMType(clientATM.getAtmType().name());
                    clientATMDTO.setAddress(clientATM.getAddress());
                    clientATMDTO.setCloseDate(clientATM.getCloseDate() != null ?
                            new Date(clientATM.getCloseDate().getTime()).toString() :
                            null);
                    getClientResponseDTO.setSelfservice(clientATMDTO);
                    break;
            }
            resp.setContentType("application/json;charset=UTF-8");
            PrintWriter out = resp.getWriter();
            out.println(mapper.writeValueAsString(getClientResponseDTO));
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            ErrorDTOService.writer(resp, e);
        }
        return;
    }
}
