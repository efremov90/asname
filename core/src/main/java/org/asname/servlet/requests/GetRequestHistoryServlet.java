package org.asname.servlet.requests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.asname.dao.users.UserAccountDAO;
import org.asname.dto.requests.GetRequestStatusHistoryRequestDTO;
import org.asname.dto.requests.GetRequestStatusHistoryResponseDTO;
import org.asname.dto.requests.RequestStatusHistoryDTO;
import org.asname.model.users.UserAccount;
import org.asname.service.servlet.ErrorDTOService;
import org.asname.service.security.PermissionService;
import org.asname.service.requests.RequestStatusHistoryService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static org.asname.model.security.Permissions.REQUESTS_VIEW;

@WebServlet(urlPatterns = {"/getRequestHistory"})
public class GetRequestHistoryServlet extends HttpServlet {

    private static final long serialVersionUID = 6693156538436066195L;

    private Logger logger = Logger.getLogger(GetRequestHistoryServlet.class.getName());

    public GetRequestHistoryServlet() {
        super();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        logger.info("start");

        try {
            ObjectMapper mapper = new ObjectMapper();
            GetRequestStatusHistoryRequestDTO getRequestStatusHistoryRequestDTO = mapper.readValue(req.getReader(), GetRequestStatusHistoryRequestDTO.class);

            UserAccount userAccount = new UserAccountDAO().getUserAccountBySessionId(req.getRequestedSessionId());
            if (!new PermissionService().isPermission(userAccount.getId(), REQUESTS_VIEW))
                throw new Exception(String.format("У пользователя %s отсутствует разрешение %s.",
                        userAccount.getAccount(),
                        REQUESTS_VIEW.name()));

            ArrayList<RequestStatusHistoryDTO> requestStatusHistory =
                    new RequestStatusHistoryService().getRequestStatusesHistory(getRequestStatusHistoryRequestDTO.getRequestId()).stream()
                            .map(x -> {
                                        RequestStatusHistoryDTO requestStatusHistoryDTO = new RequestStatusHistoryDTO();
                                        requestStatusHistoryDTO.setId(x.getId());
                                        requestStatusHistoryDTO.setStatus(x.getStatus().name());
                                        requestStatusHistoryDTO.setStatusDescription(x.getStatus().getDescription());
                                        requestStatusHistoryDTO.setUser(x.getUserName());
                                        requestStatusHistoryDTO.setEventDateTime(x.getEventDateTime().toString());
                                        requestStatusHistoryDTO.setComment(x.getComment());
                                        return requestStatusHistoryDTO;
                                    }
                            ).collect(Collectors.toCollection(() -> new ArrayList<RequestStatusHistoryDTO>()));
            GetRequestStatusHistoryResponseDTO getRequestStatusHistoryResponseDTO = new GetRequestStatusHistoryResponseDTO();
            getRequestStatusHistoryResponseDTO.setItems(requestStatusHistory);
            resp.setContentType("application/json;charset=UTF-8");
            PrintWriter out = resp.getWriter();
            out.println(mapper.writeValueAsString(getRequestStatusHistoryResponseDTO));
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            ErrorDTOService.writer(resp, e);
        }
        return;
    }

}
