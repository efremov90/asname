package org.asname.servlet.reports;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.asname.dao.users.UserAccountDAO;
import org.asname.dto.reports.GetReportsRequestDTO;
import org.asname.dto.reports.GetReportsResponseDTO;
import org.asname.dto.reports.ReportDTO;
import org.asname.model.requests.Request;
import org.asname.model.users.UserAccount;
import org.asname.service.reports.ReportService;
import org.asname.service.security.PermissionService;
import org.asname.service.servlet.ErrorDTOService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static org.asname.model.security.Permissions.REPORTS_VIEW;

@WebServlet(urlPatterns = {"/getReports"})
public class GetReportsServlet extends HttpServlet {

    private static final long serialVersionUID = 5809258732063270868L;

    private Logger logger = Logger.getLogger(GetReportsServlet.class.getName());

    public GetReportsServlet() {
        super();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        logger.info("start");

        try {
            ObjectMapper mapper = new ObjectMapper();
            GetReportsRequestDTO getReportsRequestDTO = mapper.readValue(req.getReader(),
                    GetReportsRequestDTO.class);

            UserAccount userAccount = new UserAccountDAO().getUserAccountBySessionId(req.getRequestedSessionId());
            if (!new PermissionService().isPermission(userAccount.getId(), REPORTS_VIEW))
                throw new Exception(String.format("У пользователя %s отсутствует разрешение %s.",
                        userAccount.getAccount(),
                        REPORTS_VIEW.name()));

            ArrayList<ReportDTO> reports = new ReportService().getReports(
                    (getReportsRequestDTO.getFromRunDate() != null && getReportsRequestDTO.getFromRunDate() != "") ?
                            Date.valueOf(getReportsRequestDTO.getFromRunDate()) : null,
                    (getReportsRequestDTO.getToRunDate() != null && getReportsRequestDTO.getToRunDate() != "") ?
                            Date.valueOf(getReportsRequestDTO.getToRunDate()) : null,
                    null
            ).stream()
                    .map(x -> {
                                ReportDTO reportDTO = new ReportDTO();
                                Request request = null;
                                reportDTO.setReportId(x.getId());
                                reportDTO.setRunDate(new Date(x.getCreateDate().getTime()).toString());
                                reportDTO.setRunDatetime(x.getCreateDatetime().toString());
                                reportDTO.setReportName(x.getType().getDescription());
                                reportDTO.setFromPeriodDate(x.getFromPeriodDate() != null ?
                                        new Date(x.getFromPeriodDate().getTime()).toString() :
                                        null);
                                reportDTO.setToPeriodDate(x.getToPeriodDate() != null ?
                                        new Date(x.getToPeriodDate().getTime()).toString() :
                                        null);
                                reportDTO.setParameters(x.getParameters());
                                reportDTO.setStatus(x.getStatus().getDescription());
                        reportDTO.setFormat(x.getFormat().getDescription());
                                return reportDTO;
                            }
                    ).collect(Collectors.toCollection(() -> new ArrayList<ReportDTO>()));
            GetReportsResponseDTO getReportsResponseDTO = new GetReportsResponseDTO(
                    reports
            );
            resp.setContentType("application/json;charset=UTF-8");
            PrintWriter out = resp.getWriter();
            out.println(mapper.writeValueAsString(getReportsResponseDTO));
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            ErrorDTOService.writer(resp, e);
        }
        return;
    }

}
