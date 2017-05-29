package Weka;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class StartAnalysis
 * 
 */
@WebServlet("/StartAnalysis")
public class StartAnalysisServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public StartAnalysisServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try{
		response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();
        if (session.getAttribute("uname")==null){
            response.sendRedirect("Login");
        }
        //CSV-Datei-Parameter wenn vorhanden an Session �bergeben
        if (request.getParameter("path")!= null){
        	session.setAttribute("file", request.getParameter("path"));
        }
        session.removeAttribute("clusters");
        out.println("<html>");
        out.println("<head>");
        
        //Script mit funktion einbinden
        out.println("<script src='js/loadingScreen.js'></script>");
        out.println("</head>");
        out.println("<body>");
        
        out.println("<table align='left'>" +
                "<tr>" +
                "   <form action = 'StartAnalysis' method = 'post'>" +
                "		<td><select name=\"Clusteranzahl\">" +
                "			<option value=\"1\" selected>1</option>"+
                "			<option value=\"2\">2</option>" +
                "			<option value=\"3\">3</option>"+
                "			<option value=\"4\">4</option>"+
                "			<option value=\"5\">5</option>"+
                "			<option value=\"6\">6</option>"+
                "			<option value=\"7\">7</option>"+
                "			<option value=\"8\">8</option>"+
                "			<option value=\"9\">9</option>"+
                "			<option value=\"10\">10</option>"+
                "			</select> </td>" +
                "       <td><input type = 'submit' value = 'Analyse Starten' onclick='showLoadingScreen();'/></td" +
                "   </form>" +
                "</tr>" +
                "</table>"
                + "<br>"
                + "<p id='loadingScreen'></p>");

        out.println("</body>");
        out.println("</html>");
		}catch(Exception exc){
			response.sendRedirect("Error.html");
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try{
		//Clusteranzahl
		int Clusteranzahl = 5;
		//Clusteranzahl  wird beim aufrufen als Parameter angegeben
		//wenn nicht  dann standardwert 5
		if (request.getParameter("Clusteranzahl")!=null)
			Clusteranzahl = Integer.parseInt(request.getParameter("Clusteranzahl"));
		
		HttpSession session = request.getSession();
		if(session.getAttribute("uname") == null)
			response.sendRedirect("Login");
		
		if(session.getAttribute("file") == null)
			response.sendRedirect("DataControl");
		
		Cluster[] clusters;
		String[] chartsFilenames;
		if(session.getAttribute("clusters")==null){
			//Pfad f�r die CSV Datei
			//Pfad muss beim Aufrufen des Servlets aus Parameter gesetzt sein an den path wird "kd.csv" rangeh�ngt 
			String path = session.getAttribute("file").toString();
			if(path==null){
				response.sendRedirect("DataControl");
				return;
			}
			//Weka
			if(new File(path).exists()){
				WekaClusterer.setNumClusters(Clusteranzahl);
				try {
					clusters = WekaClusterer.clustering(path);
				} catch (Exception e) {
					response.sendRedirect("DataControl?fileError=true");
					return;
				}
				chartsFilenames = DiagramCreator.masterCreate(clusters);
				session.setAttribute("clusters", clusters);
				session.setAttribute("charts",chartsFilenames );
			}
			else
			{
				response.sendRedirect("DataControl");
				return;
			}
		}else{
			clusters = (Cluster[]) session.getAttribute("clusters");
			chartsFilenames = (String[]) session.getAttribute("charts");
		}
		request.getRequestDispatcher("Analyse.jsp").forward(request, response);		
	
	}catch(Exception exc){
		response.sendRedirect("Error.html");
	}
	}
}
