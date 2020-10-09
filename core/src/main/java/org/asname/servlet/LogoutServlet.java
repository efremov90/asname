package org.asname.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.asname.dao.AccountSessionDAO;
import org.asname.dao.PermissionDAO;
import org.asname.dao.UserAccountDAO;
import org.asname.dto.GetPermissionsResponseDTO;
import org.asname.model.UserAccount;
import org.asname.service.ErrorDTOService;
import org.asname.service.SecuritySevice;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@WebServlet(urlPatterns = {"/logout"})
public class LogoutServlet extends HttpServlet {

    private static final long serialVersionUID = 6982194230746788212L;

    private Logger logger = Logger.getLogger(LogoutServlet.class.getName());

    public LogoutServlet() {
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        logger.info("start");

        try {
            new SecuritySevice().logout(req.getRequestedSessionId());
        } catch (Exception e) {
            e.printStackTrace();
            ErrorDTOService.writer(resp, e);
        }

        return;
    }
}
