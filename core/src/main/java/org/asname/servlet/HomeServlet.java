package org.asname.servlet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

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