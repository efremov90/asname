package org.asname.servlet;

import org.asname.service.servlet.ErrorDTOService;
import org.asname.service.security.SecuritySevice;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

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
