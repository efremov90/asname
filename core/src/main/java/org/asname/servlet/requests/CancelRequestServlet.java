package org.asname.servlet.requests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.asname.dao.users.AccountSessionDAO;
import org.asname.dto.requests.CancelRequestRequestDTO;
import org.asname.model.requests.Request;
import org.asname.service.requests.RequestService;
import org.asname.service.servlet.ErrorDTOService;
import org.asname.service.servlet.ResultDTOService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

@WebServlet(urlPatterns = {"/cancelRequest"})
public class CancelRequestServlet extends HttpServlet {

    private static final long serialVersionUID = 8123644727381918721L;

    private Logger logger = Logger.getLogger(CancelRequestServlet.class.getName());

    public CancelRequestServlet() {
        super();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        logger.info("start");

        try {
            ObjectMapper mapper = new ObjectMapper();
            CancelRequestRequestDTO cancelRequestRequestDTO = mapper.readValue(req.getReader(), CancelRequestRequestDTO.class);
            Request request = new RequestService().getRequestById(cancelRequestRequestDTO.getRequestId());
            new RequestService().cancel(
                    request.getRequestUUID(),
                    new AccountSessionDAO().getAccountSessionBySessionId(req.getRequestedSessionId()).getUserAccountId(),
                    cancelRequestRequestDTO.getComment()
            );
            ResultDTOService.writer(resp, "0", null);
        } catch (Exception e) {
            e.printStackTrace();
            ErrorDTOService.writer(resp, e);
        }
        return;
    }
}
