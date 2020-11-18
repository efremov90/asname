package org.asname.mvc.controllers.clients;

import org.asname.controllers.dto.ResultDTO;
import org.asname.controllers.dto.ResultResponseDTO;
import org.asname.controllers.dto.clients.ClientATMDTO;
import org.asname.controllers.dto.clients.ClientDopofficeDTO;
import org.asname.controllers.dto.clients.EditClientRequestDTO;
import org.asname.dao.users.AccountSessionDAO;
import org.asname.model.clients.ATMTypeType;
import org.asname.model.clients.ClientATM;
import org.asname.model.clients.ClientDopoffice;
import org.asname.service.clients.ClientATMService;
import org.asname.service.clients.ClientDopofficeService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.logging.Logger;

@Controller
public class EditClientController {

    private Logger logger = Logger.getLogger(EditClientController.class.getName());


    @RequestMapping(value = "/editClient", method = RequestMethod.POST)
    public @ResponseBody
    ResultResponseDTO createClient(@RequestBody EditClientRequestDTO editClientDTO,
                                   @CookieValue("JSESSIONID") String sessionId) throws Exception {

        logger.info("start");
//        try {
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
                    new AccountSessionDAO().getAccountSessionBySessionId(sessionId).getUserAccountId());
            return new ResultResponseDTO(new ResultDTO("0", null));
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
            new ClientATMService().create(client,
                    new AccountSessionDAO().getAccountSessionBySessionId(sessionId).getUserAccountId());
            return new ResultResponseDTO(new ResultDTO("0", null));
        } else return null;
//        } catch (Exception e) {
//            e.printStackTrace();
//            ErrorDTOService.writer(resp, e);
//        }
//        return;
    }

/*    @ExceptionHandler
    public @ResponseBody
    ErrorResponseDTO handle(IOException e) throws IOException {
        logger.info("start");
        e.printStackTrace();
        return new ErrorResponseDTO(new ErrorDTO("1", e.getMessage(), new IntegrationService().getExceptionString(e)));
    }*/

}
