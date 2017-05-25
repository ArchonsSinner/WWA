package Weka;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class Login
 */
@WebServlet(description = "Servlet zu Überpüfung der Logindaten", urlPatterns = { "/Login" })
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Login() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// To Do Input prüfen und hashen
		try {
			String uname = request.getParameter("uname");
			String password = request.getParameter("password");

			// To Do username und password nicht hartcoden sondern vernünftig
			// abspeichern mit hash und so
			if (uname.equals("admin") && password.equals("password")) {
				HttpSession session = request.getSession();
				// Info Benutzername wird in session gespeichert. So kann auf
				// anderen Seiten geprüft werden ob der User eine passende
				// Session hat
				session.setAttribute("username", uname);
				response.sendRedirect("FileUpload.jsp");
			} else
				response.sendRedirect("Login.jsp");
		} catch (NullPointerException e) {
			response.sendRedirect("Login.jsp");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
