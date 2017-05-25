import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

/**
 * Created by florian on 23.05.17.
 */
public class Login extends HttpServlet {
    //Test Input data
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        Enumeration paramNames = request.getParameterNames();
        String[] paramArray = request.getParameterValues((String) paramNames.nextElement());
        String username = paramArray[0];
        paramArray = request.getParameterValues((String) paramNames.nextElement());
        String password = paramArray[0];

        if(username.equals("KD") && password.equals("kaufdort")){
            //Login succeeded
            HttpSession session = request.getSession();
            session.setAttribute("Login",(Integer)1);
            response.sendRedirect("DataControl");
        } else {
            //Login failed - redirect to login page
            response.sendRedirect("Login?fail=1");
        }
    }

    //Generate Login screen
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String prefail = request.getParameter("fail");

        out.println("<head><link rel='stylesheet' type='text/css' href='" + request.getContextPath() + "/bulma.css'/>" +
                "<title>Weka Web App</title></head>");

        out.println(
                "<div class='block'>" +
                "<table align='center'>" +
                "       <form method='post' action='Login' align='center'>" +
                "           <tr><td><input type='text' name='username' placeholder='Benutzername'></td></tr>" +
                "           <tr><td><input type='password' name='password' placeholder='Passwort'></td></tr>" +
                "           <tr><td><input type='submit' value='Anmelden' align='center'></td></tr>" +
                "       </form>" +
                "</table>" +
                "</div>");

        if(prefail.equals("1")){
            out.println("<article class='message is-danger'>" +
                    "  <div class='message-header'>" +
                    "    <p>Benutzername oder Passwort falsch</p>" +
                    "  </div>" +
                    "</article>");
        }

    }
}
