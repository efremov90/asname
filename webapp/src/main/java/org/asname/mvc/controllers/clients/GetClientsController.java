package org.asname.mvc.controllers.clients;

import org.asname.controllers.dto.ErrorDTO;
import org.asname.controllers.dto.ErrorResponseDTO;
import org.asname.controllers.dto.clients.ClientDTO;
import org.asname.controllers.dto.clients.GetClientsRequestDTO;
import org.asname.controllers.dto.clients.GetClientsResponseDTO;
import org.asname.dao.users.UserAccountDAO;
import org.asname.integration.utils.service.IntegrationService;
import org.asname.model.clients.ClientATM;
import org.asname.model.clients.ClientTypeType;
import org.asname.model.users.UserAccount;
import org.asname.service.clients.ClientATMService;
import org.asname.service.clients.ClientService;
import org.asname.service.security.PermissionService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static org.asname.model.security.Permissions.CLIENTS_VIEW;

@Controller
public class GetClientsController {

    private Logger logger = Logger.getLogger(GetClientsController.class.getName());


    @RequestMapping(value = "/getClients", method = RequestMethod.POST)
    public @ResponseBody
    GetClientsResponseDTO createClient(@RequestBody GetClientsRequestDTO getClientsRequestDTO,
                                       @CookieValue("JSESSIONID") String sessionId) throws Exception {

        logger.info("start");
//        try {
        UserAccount userAccount = new UserAccountDAO().getUserAccountBySessionId(sessionId);
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
        return getClientsResponseDTO;
//        } catch (Exception e) {
//            e.printStackTrace();
//            ErrorDTOService.writer(resp, e);
//        }
//        return;
    }

    @ExceptionHandler
    public @ResponseBody
    ErrorResponseDTO handle(IOException e) throws IOException {
        logger.info("start");
        e.printStackTrace();
        return new ErrorResponseDTO(new ErrorDTO("1", e.getMessage(), new IntegrationService().getExceptionString(e)));
    }

}
