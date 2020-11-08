package org.asname.servlet.requests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.asname.dao.users.AccountSessionDAO;
import org.asname.dto.requests.CreateRequestRequestDTO;
import org.asname.dto.requests.RequestDTO;
import org.asname.model.requests.Request;
import org.asname.service.servlet.ErrorDTOService;
import org.asname.service.requests.RequestService;
import org.asname.service.servlet.ResultDTOService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import java.util.logging.Logger;

import static org.asname.audit.model.SystemType.ASNAME;

@WebServlet(urlPatterns = {"/createRequest"})
public class CreateRequestServlet extends HttpServlet {

    private static final long serialVersionUID = 5477656489716096957L;

    private Logger logger = Logger.getLogger(CreateRequestServlet.class.getName());

    public CreateRequestServlet() {
        super();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        logger.info("start");
        try {
            ObjectMapper mapper = new ObjectMapper();
            CreateRequestRequestDTO createRequestDTO = mapper.readValue(req.getReader(), CreateRequestRequestDTO.class);

            RequestDTO requestDTO = createRequestDTO.getRequest();
            Request request = new Request();
            request.setId(requestDTO.getRequestId());
            request.setRequestUUID(UUID.randomUUID().toString());
            request.setCreateDate(new java.util.Date());
            request.setCreateDateTime(new java.util.Date());
            request.setClientCode(requestDTO.getClientCode());
            request.setComment(requestDTO.getComment());
            request.setLastUserAccountIdChangeRequestStatus(new AccountSessionDAO().getAccountSessionBySessionId(req.getRequestedSessionId()).getUserAccountId());
            request.setCreateSystemId(ASNAME.getId());
            new RequestService().create(request,
                    new AccountSessionDAO().getAccountSessionBySessionId(req.getRequestedSessionId()).getUserAccountId());
            ResultDTOService.writer(resp, "0", null);
        } catch (Exception e) {
            e.printStackTrace();
            ErrorDTOService.writer(resp, e);
        }
        return;
    }

}
