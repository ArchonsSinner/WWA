package Weka;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by floriannink on 30/05/17.
 */
public class old_helper extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        HttpSession session = request.getSession();
        if (session.getAttribute("uname")==null){
            response.sendRedirect("Login");
        }

        if (request.getParameter("path")!= null && request.getParameter("clust")!= null){
            session.setAttribute("file", request.getParameter("path"));
            session.setAttribute("Clusteranzahl", request.getParameter("clust"));
            out.print("<link rel='stylesheet' href='bulma.css'>" +
                    "<p>Lade " + request.getParameter("path") + " mit " + request.getParameter("clust") + " Clustern?</p>" +
                    "<form action='/StartServlet' methode='post'>" +
                    "<input type='submit' class='button is-info' value='Weiter'>" +
                    "</form>");
        } else {
            response.sendRedirect("Login");
        }
    }
}
