import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by florian on 23.05.17.
 */
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

        out.println("<html>\n" +
                "\t<head>\n" +
                "\t\t<link rel='stylesheet' href='css/bulma.css'>\n" +
                "\t\t<title>Weka Web App</title>\n" +
                "\t</head>\n" +
                "\t<body>\n" +
                "\t\t<div class='columns'>\n" +
                "\t\t\t<div class='column is-one-third'>\n" +
                "\t\t\t</div>\n" +
                "\t\t\t<div class='column'>\n" +
                "\t\t\t\t<form method='post' action='Login' align='center'>\n" +
                "\t\t\t\t\t<figure class='image'>\n" +
                "\t\t\t\t\t\t<img src='Images/logo.gif'>\n" +
                "\t\t\t\t\t</figure>\n" +
                "\t\t\t\t\t<div class=\"field\">\n" +
                "\t\t\t\t\t\t<p class=\"control\">\n" +
                "\t\t\t\t\t\t\t<input class=\"input\" type=\"text\" name='username' placeholder=\"Benutzername\">\n" +
                "\t\t\t\t\t\t</p>\n" +
                "\t\t\t\t\t</div>\n" +
                "\t\t\t\t\t<div class=\"field\">\n" +
                "\t\t\t\t\t\t<p class=\"control\">\n" +
                "\t\t\t\t\t\t\t<input class=\"input\" type=\"password\" name='password' placeholder=\"Passwort\">\n" +
                "\t\t\t\t\t\t</p>\n" +
                "\t\t\t\t\t</div>\n" +
                "\t\t\t\t\t<input type='submit' value='Anmelden' class='button is-info'>\n" +
                "\t\t\t\t</form>\n");

        if (request.getParameter("fail") != null) {
            String prefail = request.getParameter("fail");
            if (prefail.equals("1")) {
                out.println("<article class='message is-danger'>" + "  <div class='message-header'>"
                        + "    <p>Benutzername oder Passwort falsch</p>" + "  </div>" + "</article>");
            }
        }

        out.println("\t\t\t</div>\n" +
                "\t\t\t<div class='column'>\n" +
                "\t\t\t</div>\n" +
                "\t\t</div>\n" +
                "\t</body>\n" +
                "</html>");
    }
}