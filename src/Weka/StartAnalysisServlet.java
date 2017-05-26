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
 */
@WebServlet("/StartAnalysis")
public class StartAnalysisServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public StartAnalysisServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();
        if (session.getAttribute("uname")==null){
            response.sendRedirect("Login");
        }

        out.println("<table align='left'>" +
                "<tr>" +
                "   <form action = 'StartAnalysis' method = 'post'>" +
                "       <td><input type = 'submit' value = 'Upload File' /></td" +
                "   </form>" +
                "</tr>" +
                "</table>");
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
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
				System.out.println("Error:No filelocation given!");
				//TODO  Fehlerseite?????
			}
			//Weka
			if(new File(path).exists()){
			WekaClusterer.setNumClusters(Clusteranzahl);
			clusters = WekaClusterer.clustering(path);
			chartsFilenames = DiagramCreator.masterCreate(clusters);
			session.setAttribute("clusters", clusters);
			session.setAttribute("charts",chartsFilenames );
			}
			else
			{
				//Datei existiert nicht und es muss ein Fehler ausgegeben werden
			}
		}else{
			clusters = (Cluster[]) session.getAttribute("clusters");
			chartsFilenames = (String[]) session.getAttribute("charts");
		}
		
		
		
		
		request.getRequestDispatcher("Analyse.jsp").forward(request, response);
		
	}

}
