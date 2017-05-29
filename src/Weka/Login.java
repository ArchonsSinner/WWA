package Weka;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by florian on 23.05.17.
 */
@WebServlet("/Login")
public class Login extends HttpServlet {

	private static final long serialVersionUID = -4329419400612966819L;

	// Test Input data
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");

		String username = request.getParameter("username");
		String password = request.getParameter("password");

		if (username.equals("KD") && password.equals("kaufdort")) {
			// Login succeeded
			HttpSession session = request.getSession();
			session.setAttribute("uname", username);
			response.sendRedirect("DataControl");
		} else {
			// Login failed - redirect to login page
			response.sendRedirect("Login?fail=1");
		}
	}

	// Generate Login screen
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		HttpSession session = request.getSession();
		if(session.getAttribute("uname") != null)
			response.sendRedirect("DataControl");

		out.println("<head><link rel='stylesheet' type='text/css' href='" + request.getContextPath() + "/bulma.css'/>"
				+ "<title>Weka Web App</title></head>");

		out.println("<div class='block'>" + "<table align='center'>"
				+ "       <form method='post' action='Login' align='center'>"
				+ "           <tr><td><input type='text' name='username' placeholder='Benutzername'></td></tr>"
				+ "           <tr><td><input type='password' name='password' placeholder='Passwort'></td></tr>"
				+ "           <tr><td><input type='submit' value='Anmelden' align='center'></td></tr>"
				+ "       </form>" + "</table>" + "</div>");

		if (request.getParameter("fail") != null) {
			String prefail = request.getParameter("fail");
			if (prefail.equals("1")) {
				out.println("<article class='message is-danger'>" + "  <div class='message-header'>"
						+ "    <p>Benutzername oder Passwort falsch</p>" + "  </div>" + "</article>");
			}
		}

	}
}
