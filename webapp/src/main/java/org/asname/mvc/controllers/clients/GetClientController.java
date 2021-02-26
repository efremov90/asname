package org.asname.mvc.controllers.clients;

import org.asname.controllers.dto.ErrorDTO;
import org.asname.controllers.dto.ErrorResponseDTO;
import org.asname.controllers.dto.ResultDTO;
import org.asname.controllers.dto.ResultResponseDTO;
import org.asname.controllers.dto.clients.EditClientRequestDTO;
import org.asname.controllers.dto.clients.GetClientRequestDTO;
import org.asname.dao.users.AccountSessionDAO;
import org.asname.dto.clients.ClientATMDTO;
import org.asname.dto.clients.ClientDopofficeDTO;
import org.asname.dto.clients.GetClientResponseDTO;
import org.asname.integration.utils.service.IntegrationService;
import org.asname.model.clients.ATMTypeType;
import org.asname.model.clients.ClientATM;
import org.asname.model.clients.ClientDopoffice;
import org.asname.model.clients.ClientTypeType;
import org.asname.service.clients.ClientATMService;
import org.asname.service.clients.ClientDopofficeService;
import org.asname.service.clients.ClientService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.Date;
import java.util.logging.Logger;

@Controller
public class GetClientController {

    private Logger logger = Logger.getLogger(GetClientController.class.getName());


    @RequestMapping(value = "/getClient", method = RequestMethod.POST)
    public @ResponseBody
    GetClientResponseDTO createClient(@RequestBody GetClientRequestDTO getClientRequestDTO,
                                   @CookieValue("JSESSIONID") String sessionId) throws Exception {

        logger.info("start");
//        try {
        ClientTypeType clientType = new ClientService().getClient(getClientRequestDTO.getClientCode()).getClientType();
        GetClientResponseDTO getClientResponseDTO = new GetClientResponseDTO();
        switch (clientType) {
            case DOPOFFICE:
                ClientDopoffice clientDopoffice =
                        new ClientDopofficeService().getClientByCode(getClientRequestDTO.getClientCode());
                org.asname.dto.clients.ClientDopofficeDTO clientDopofficeDTO = new ClientDopofficeDTO();
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
                org.asname.dto.clients.ClientATMDTO clientATMDTO = new ClientATMDTO();
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
        return getClientResponseDTO;
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
