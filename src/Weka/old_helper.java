package Weka;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by floriannink on 30/05/17.
 */

@WebServlet("/old_helper")
public class old_helper extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		HttpSession session = request.getSession();
		if (session.getAttribute("uname") == null) {
			response.sendRedirect("Login");
		} else {
			session.removeAttribute("clusters");
			if (request.getParameter("path") != null && request.getParameter("clust") != null) {
				session.setAttribute("file", request.getParameter("path"));
				session.setAttribute("Clusteranzahl", request.getParameter("clust"));
				response.getWriter()
						.print("<link rel='stylesheet' href='bulma.css'>" + "<p>Lade " + request.getParameter("path")
								+ " mit " + request.getParameter("clust") + " Clustern?</p>"
								+ "<form action='StartAnalysis' method='POST'>"
								+ "<input type='submit' class='button is-info' value='Weiter'>" + "</form>");
			} else {
				response.sendRedirect("Login");
			}
		}
	}
}
