package org.asname.servlet;

import org.asname.dao.ConfigureDAO;
import org.asname.dao.UserAccountDAO;
import org.asname.model.Configure;
import org.asname.model.UserAccount;
import org.asname.service.SecuritySevice;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Logger;

import static org.asname.model.Configures.MAX_INACTIVE_SESSION_INTERVAL;

@WebServlet(urlPatterns = {"/home"})
public class HomeServlet extends HttpServlet {

    private static final long serialVersionUID = -6999981773349590838L;
    private Logger logger = Logger.getLogger(HomeServlet.class.getName());

    public HomeServlet() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        logger.info("start");
        logger.info("getRequestURL:" + request.getRequestURL());
        logger.info("getRequestURI:" + request.getRequestURI());
        logger.info("getContextPath:" + request.getContextPath());
        logger.info("getServletPath:" + request.getServletPath());

        RequestDispatcher dispatcher = request
                .getRequestDispatcher("/index.html");

        dispatcher.forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        logger.info("start");
        logger.info("getRequestURL:" + request.getRequestURL());
        logger.info("getRequestURI:" + request.getRequestURI());
        logger.info("getContextPath:" + request.getContextPath());
        logger.info("getServletPath:" + request.getServletPath());

        doGet(request, response);
    }

}