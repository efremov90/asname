package org.asname.mvc.controllers.clients;

import org.asname.controllers.dto.ErrorDTO;
import org.asname.controllers.dto.ErrorResponseDTO;
import org.asname.controllers.dto.ResultDTO;
import org.asname.controllers.dto.ResultResponseDTO;
import org.asname.controllers.dto.clients.ClientATMDTO;
import org.asname.controllers.dto.clients.ClientDopofficeDTO;
import org.asname.controllers.dto.clients.CreateClientRequestDTO;
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
public class CreateClientController {

    private Logger logger = Logger.getLogger(CreateClientController.class.getName());


    @RequestMapping(value = "/createClient", method = RequestMethod.POST)
    public @ResponseBody
    ResultResponseDTO createClient(@RequestBody CreateClientRequestDTO createClientDTO,
                                   @CookieValue("JSESSIONID") String sessionId) throws Exception {

        logger.info("start");
//        try {
        if (createClientDTO.getDopoffice() != null) {
            ClientDopofficeDTO clientDTO = createClientDTO.getDopoffice();
            ClientDopoffice client = new ClientDopoffice();
            client.setId(clientDTO.getId());
            client.setClientCode(clientDTO.getClientCode());
            client.setClientName(clientDTO.getClientName());
            client.setAddress(clientDTO.getAddress());
            client.setCloseDate((clientDTO.getCloseDate() != null && clientDTO.getCloseDate() != "") ?
                    Date.valueOf(clientDTO.getCloseDate()) : null);
            new ClientDopofficeService().create(client,
                    new AccountSessionDAO().getAccountSessionBySessionId(sessionId).getUserAccountId());
            return new ResultResponseDTO(new ResultDTO("0", null));
        } else if (createClientDTO.getSelfservice() != null) {
            ClientATMDTO clientDTO = createClientDTO.getSelfservice();
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
//    ErrorResponseDTO handle(IOException e) throws IOException {
    ResultResponseDTO handle(IOException e) throws IOException {
        logger.info("start");
        e.printStackTrace();
//        return new ErrorResponseDTO(new ErrorDTO("1", e.getMessage(), new IntegrationService().getExceptionString(e)));
        return new ResultResponseDTO(new ResultDTO("0", null));
    }*/

/*    @ExceptionHandler
    public @ResponseBody
//    ErrorResponseDTO handle(IOException e) throws IOException {
    ResponseEntity<String> handle(IOException e) throws IOException {
        logger.info("start");
        e.printStackTrace();
//        return new ErrorResponseDTO(new ErrorDTO("1", e.getMessage(), new IntegrationService().getExceptionString(e)));
        return new ResponseEntity<String>(new ResultResponseDTO(new ResultDTO("0", null)).toString(),HttpStatus.BAD_REQUEST);
    }*/

/*    @ExceptionHandler({IOException.class})
    public @ResponseBody ResultResponseDTO handle() {
        logger.info("start");
//        e.printStackTrace();
//        return new ErrorResponseDTO(new ErrorDTO("1", e.getMessage(), new IntegrationService().getExceptionString(e)));
        return new ResultResponseDTO(new ResultDTO("0", null));
    }*/
/*    @ExceptionHandler({IOException.class})
    public ResponseEntity hello() {
        return new ResponseEntity("Hello World!", HttpStatus.OK);
    }*/
}
