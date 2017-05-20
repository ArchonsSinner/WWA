package Weka;

import java.io.IOException;
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
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//Clusteranzahl
		int Clusteranzahl = 5;
		if (request.getParameter("Clusteranzahl")!=null)
			Clusteranzahl = Integer.parseInt(request.getParameter("Clusteranzahl"));
		HttpSession session = request.getSession();
		
		Cluster[] clusters;
		String[] chartsFilenames;
		if(session.getAttribute("clusters")==null){
			//Pfad
			String path=request.getParameter("path");
			if(path==null){
				System.out.println("Error:No filelocation given!");
				//TODO  Fehlerseite?????
			}
			//Weka
			WekaClusterer.setNumClusters(Clusteranzahl);
			clusters = WekaClusterer.clustering(path);
			chartsFilenames = DiagramCreator.masterCreate(clusters);
			session.setAttribute("clusters", clusters);
			session.setAttribute("charts",chartsFilenames );
		}else{
			clusters = (Cluster[]) session.getAttribute("clusters");
			chartsFilenames = (String[]) session.getAttribute("charts");
		}
		
		
		
		
		request.getRequestDispatcher("Analyse.jsp").forward(request, response);
		
	}

}
