package org.asname.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.asname.dao.UserAccountDAO;
import org.asname.dto.ClientDTO;
import org.asname.dto.GetClientsRequestDTO;
import org.asname.dto.GetClientsResponseDTO;
import org.asname.model.ClientATM;
import org.asname.model.ClientTypeType;
import org.asname.model.UserAccount;
import org.asname.service.ClientATMService;
import org.asname.service.ClientService;
import org.asname.service.ErrorDTOService;
import org.asname.service.PermissionService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static org.asname.model.Permissions.CLIENTS_VIEW;
import static org.asname.model.Permissions.REQUESTS_VIEW;

@WebServlet(urlPatterns = {"/getClients"})
public class GetClientsServlet extends HttpServlet {

    private static final long serialVersionUID = 6959500293773959824L;

    private Logger logger = Logger.getLogger(GetClientsServlet.class.getName());

    public GetClientsServlet() {
        super();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        logger.info("start");

        try {
            ObjectMapper mapper = new ObjectMapper();
            GetClientsRequestDTO getClientsRequestDTO = mapper.readValue(req.getReader(), GetClientsRequestDTO.class);

            UserAccount userAccount = new UserAccountDAO().getUserAccountBySessionId(req.getRequestedSessionId());
            if (!new PermissionService().isPermission(userAccount.getId(), CLIENTS_VIEW))
                throw new Exception(String.format("У пользователя %s отсутствует разрешение %s.",
                        userAccount.getAccount(),
                        CLIENTS_VIEW.name()));

            ArrayList<ClientDTO> clients = new ClientService().getClients(
                    !getClientsRequestDTO.getClientType().equals("ALL") ?
                            ClientTypeType.valueOf(getClientsRequestDTO.getClientType()) : null
            ).stream()
                    .map(x -> {
                                ClientDTO clientDTO = new ClientDTO();
                                clientDTO.setId(x.getId());
                                clientDTO.setClientCode(x.getClientCode());
                                clientDTO.setClientName(x.getClientName());
                                clientDTO.setClientType(x.getClientType().name());
                                clientDTO.setClientTypeDescription(x.getClientType().getDescription());
                                if (x.getClientType().equals(ClientTypeType.SELFSERVICE)) {
                                    ClientATM clientATM = null;
                                    try {
                                        clientATM = new ClientATMService().getClientByCode(x.getClientCode());
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }
                                    clientDTO.setAtmType(clientATM.getAtmType().name());
                                    clientDTO.setAtmTypeDescription(clientATM.getAtmType().getDescrition());
                                }
                                clientDTO.setAddress(x.getAddress());
                                clientDTO.setCloseDate(x.getCloseDate() != null ?
                                        new Date(x.getCloseDate().getTime()).toString() :
                                        null);
                                return clientDTO;
                            }
                    ).collect(Collectors.toCollection(() -> new ArrayList<ClientDTO>()));
            GetClientsResponseDTO getClientsResponseDTO = new GetClientsResponseDTO(
                    clients
            );
            resp.setContentType("application/json;charset=UTF-8");
            PrintWriter out = resp.getWriter();
            out.println(mapper.writeValueAsString(getClientsResponseDTO));
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            ErrorDTOService.writer(resp, e);
        }
        return;
    }

}
