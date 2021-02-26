package org.asname.servlet;

import org.asname.service.security.AuthHeader;
import org.asname.service.servlet.ErrorDTOService;
import org.asname.service.security.SecurityContext;
import org.asname.service.security.SecuritySevice;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.logging.Logger;

import static org.asname.filter.SecurityFilter.AUTHENTICATION_HEADER;
import static org.asname.model.security.SecurityContextStatusCodeType.*;

@WebServlet(urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 8044646618257579732L;

    private Logger logger = Logger.getLogger(LoginServlet.class.getName());

    public LoginServlet() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        logger.info("start");
        /*logger.info("getRequestURL:"+request.getRequestURL());
        logger.info("getRequestURI:"+request.getRequestURI());
        logger.info("getContextPath:"+request.getContextPath());
        logger.info("getServletPath:"+request.getServletPath());*/

        RequestDispatcher dispatcher = request
                .getRequestDispatcher("/view/login.html");

        dispatcher.forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        logger.info("start");
        /*logger.info("getRequestURL:"+request.getRequestURL());
        logger.info("getRequestURI:"+request.getRequestURI());
        logger.info("getContextPath:"+request.getContextPath());
        logger.info("getServletPath:"+request.getServletPath());*/
        String sessionId = req.getRequestedSessionId();

        try {
            Cookie cookieAuthStatusCode = null;
            if (sessionId != null && req.getHeader(AUTHENTICATION_HEADER) == null) {
                if (new SecuritySevice().isActiveSession(sessionId)) {
                    logger.info("new SecuritySevice().isActiveSession(sessionId)");
//                    resp.addHeader("AUTH_STATUS_CODE", S00000.toString());
                    cookieAuthStatusCode = new Cookie("AUTH_STATUS_CODE", S00000.toString());
                }
                if (!(new SecuritySevice().isActiveSession(sessionId))) {
                    logger.info("!(new SecuritySevice().isActiveSession(sessionId))");
                    resp.setStatus(401);
//                    resp.addHeader("AUTH_STATUS_CODE", S00002.toString());
                    cookieAuthStatusCode = new Cookie("AUTH_STATUS_CODE", S00002.toString());
                }
            }
            else if (req.getHeader(AUTHENTICATION_HEADER) != null) {
                logger.info("req.getHeader(AUTHENTICATION_HEADER) != null");
                AuthHeader authHeader = new AuthHeader(req.getHeader(AUTHENTICATION_HEADER));
                SecurityContext securityContext = new SecuritySevice().login(authHeader.getAccount(),
                        authHeader.getPassword(), req);
//                resp.addHeader("AUTH_STATUS_CODE", securityContext.getStatusCode().toString());
                cookieAuthStatusCode = new Cookie("AUTH_STATUS_CODE", securityContext.getStatusCode().toString());
                Cookie cookieSessionId = null;
                if (securityContext.getStatusCode().equals(S00000)) {
                    logger.info("securityContext.getStatusCode().equals(S00000)");
                    Cookie[] cookies = req.getCookies();
                    if (cookies != null)
                        for (Cookie cookie : cookies) {
                            if (cookie.getName() == "JSESSIONID") {
                                cookieSessionId = cookie;
                                break;
                            }
                        }
                    if (cookieSessionId == null) cookieSessionId = new Cookie("JSESSIONID", null);
                    cookieSessionId.setValue(securityContext.getSessionId());
                    cookieSessionId.setMaxAge(-1);
                    cookieSessionId.setHttpOnly(false);
                    cookieSessionId.setDomain(req.getServerName());
                    cookieSessionId.setPath("/");
                    resp.addCookie(cookieSessionId);
                    resp.addHeader(AUTHENTICATION_HEADER, req.getHeader(AUTHENTICATION_HEADER));
                    resp.setContentType("text/plain;charset=UTF-8");
                    return;
                } else {
                    logger.info("else securityContext.getStatusCode().equals(S00000)");
                    resp.setStatus(401);
                }
            } else {
                logger.info("else sessionId == null && req.getHeader(AUTHENTICATION_HEADER) != null");
                resp.setStatus(401);
//                resp.addHeader("AUTH_STATUS_CODE", S00003.toString());
                cookieAuthStatusCode = new Cookie("AUTH_STATUS_CODE", S00002.toString());
            }
            cookieAuthStatusCode.setMaxAge(-1);
            cookieAuthStatusCode.setHttpOnly(false);
            cookieAuthStatusCode.setDomain(req.getServerName());
            cookieAuthStatusCode.setPath("/");
            resp.addCookie(cookieAuthStatusCode);
            doGet(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            ErrorDTOService.writer(resp, e);
        }
        return;
    }

}