package org.asname.servlet.requests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.asname.dao.users.UserAccountDAO;
import org.asname.dto.requests.GetRequestsRequestDTO;
import org.asname.dto.requests.GetRequestsResponseDTO;
import org.asname.dto.requests.RequestDTO;
import org.asname.model.clients.Client;
import org.asname.model.requests.Request;
import org.asname.model.users.UserAccount;
import org.asname.service.clients.ClientService;
import org.asname.service.servlet.ErrorDTOService;
import org.asname.service.security.PermissionService;
import org.asname.service.requests.RequestService;

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

import static org.asname.model.security.Permissions.REQUESTS_VIEW;

@WebServlet(urlPatterns = {"/getRequests"})
public class GetRequestsServlet extends HttpServlet {

    private static final long serialVersionUID = 4012603729403601696L;

    private Logger logger = Logger.getLogger(GetRequestsServlet.class.getName());

    public GetRequestsServlet() {
        super();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        logger.info("start");

        try {
            ObjectMapper mapper = new ObjectMapper();
            GetRequestsRequestDTO getRequestsRequestDTO = mapper.readValue(req.getReader(), GetRequestsRequestDTO.class);

            UserAccount userAccount = new UserAccountDAO().getUserAccountBySessionId(req.getRequestedSessionId());
            if (!new PermissionService().isPermission(userAccount.getId(), REQUESTS_VIEW))
                throw new Exception(String.format("У пользователя %s отсутствует разрешение %s.",
                        userAccount.getAccount(),
                        REQUESTS_VIEW.name()));

            ArrayList<RequestDTO> requests = new RequestService().getRequests(
                    (getRequestsRequestDTO.getFromCreateDate() != null && getRequestsRequestDTO.getFromCreateDate() != "") ?
                            Date.valueOf(getRequestsRequestDTO.getFromCreateDate()) : null,
                    (getRequestsRequestDTO.getToCreateDate() != null && getRequestsRequestDTO.getToCreateDate() != "") ?
                            Date.valueOf(getRequestsRequestDTO.getToCreateDate()) : null,
                    null,
                    null
            ).stream()
                    .map(x -> {
                                RequestDTO requestDTO = new RequestDTO();
                                Request request = null;
                                Client client = null;
                                UserAccount lastUserAccount = null;
                                try {
                                    client = new ClientService().getClient(x.getClientCode());
                                    lastUserAccount =
                                            new UserAccountDAO().getUserAccountById(x.getLastUserAccountIdChangeRequestStatus());
                                } catch (SQLException | ClassNotFoundException e) {
                                    e.printStackTrace();
                                }
                                requestDTO.setRequestId(x.getId());
                                requestDTO.setRequestUUID(x.getRequestUUID());
                                requestDTO.setCreateDate(new Date(x.getCreateDate().getTime()).toString());
                        requestDTO.setCreateDatetime(x.getCreateDateTime().toString());
                        requestDTO.setClientCode(x.getClientCode());
                                requestDTO.setClientName(client.getClientName());
                                requestDTO.setClientType(client.getClientType().name());
                                requestDTO.setClientTypeDescription(client.getClientType().getDescription());
                                requestDTO.setComment(x.getComment());
                                requestDTO.setRequestStatus(x.getRequestStatus().name());
                                requestDTO.setCommentRequestStatus(x.getCommentRequestStatus());
                                requestDTO.setRequestStatusDescription(x.getRequestStatus().getDescription());
                                requestDTO.setLastDateTimeChangeRequestStatus(x.getLastDateTimeChangeRequestStatus().toString());
                                requestDTO.setLastUserAccountIdChangeRequestStatus(x.getLastUserAccountIdChangeRequestStatus());
                                requestDTO.setLastUserNameChangeRequestStatus(lastUserAccount.getFullName());
                                return requestDTO;
                            }
                    ).collect(Collectors.toCollection(() -> new ArrayList<RequestDTO>()));
            GetRequestsResponseDTO getRequestsResponseDTO = new GetRequestsResponseDTO(
                    requests
            );
            resp.setContentType("application/json;charset=UTF-8");
            PrintWriter out = resp.getWriter();
            out.println(mapper.writeValueAsString(getRequestsResponseDTO));
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            ErrorDTOService.writer(resp, e);
        }
        return;
    }

}
