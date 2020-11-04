package org.asname.servlet.requests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.asname.dao.users.UserAccountDAO;
import org.asname.dto.audit.AuditDTO;
import org.asname.dto.audit.AuditsResponseDTO;
import org.asname.dto.requests.GetRequestAuditsRequestDTO;
import org.asname.model.users.UserAccount;
import org.asname.service.servlet.ErrorDTOService;
import org.asname.service.security.PermissionService;
import org.asname.service.requests.RequestService;

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

@WebServlet(urlPatterns = {"/getRequestAudits"})
public class GetRequestAuditsServlet extends HttpServlet {

    private static final long serialVersionUID = 1298529002996149088L;

    private Logger logger = Logger.getLogger(GetRequestAuditsServlet.class.getName());

    public GetRequestAuditsServlet() {
        super();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        logger.info("start");

        try {
            ObjectMapper mapper = new ObjectMapper();
            GetRequestAuditsRequestDTO getRequestAuditsRequestDTO = mapper.readValue(req.getReader(),
                    GetRequestAuditsRequestDTO.class);

            UserAccount userAccount = new UserAccountDAO().getUserAccountBySessionId(req.getRequestedSessionId());
            if (!new PermissionService().isPermission(userAccount.getId(), REQUESTS_VIEW))
                throw new Exception(String.format("У пользователя %s отсутствует разрешение %s.",
                        userAccount.getAccount(),
                        REQUESTS_VIEW.name()));

            ArrayList<AuditDTO> audits = new RequestService().getAudits(getRequestAuditsRequestDTO.getRequestId()).stream()
                    .map(x -> {
                        AuditDTO auditDTO = new AuditDTO();
                        auditDTO.setId(x.getId());
                        auditDTO.setEvent(x.getAuditOperType().getName());
                        auditDTO.setUser(x.getUserName());
                        auditDTO.setEventDateTime(x.getEventDateTime().toString());
                        auditDTO.setDescription(x.getDescription());
                        auditDTO.setContent(x.getContent());
                                return auditDTO;
                            }
                    ).collect(Collectors.toCollection(() -> new ArrayList<AuditDTO>()));
            AuditsResponseDTO auditsResponseDTO = new AuditsResponseDTO();
            auditsResponseDTO.setItems(audits);
            resp.setContentType("application/json;charset=UTF-8");
            PrintWriter out = resp.getWriter();
            out.println(mapper.writeValueAsString(auditsResponseDTO));
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            ErrorDTOService.writer(resp, e);
        }
        return;
    }

}
