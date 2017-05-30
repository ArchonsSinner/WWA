package Weka;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class MarketingServlet
 */
@WebServlet("/MarketingServlet")
public class MarketingServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MarketingServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		/*Error Types:
		 * 0: Exception unsolved
		 * 1: FileNotFoundException
		 * 2: Can't write or read file
		 * 3: Fehler beim holen des Sets
		 */
		HttpSession session = request.getSession();
		if(session.getAttribute("errorLevel") != null){
		String fehlerNr = session.getAttribute("errorLevel").toString();
		
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		if(fehlerNr.equals("0")){
			out.println("<html>");
	        out.println("<head>");
			out.println("</head>");
	        out.println("<body>");
	        out.println("Während der Verarbeitung ist ein Fehler im Bezug auf die Marketingstrategien aufgetreten.");
	        out.println("</body>");
	        out.println("</html>");
		}else if(fehlerNr.equals("1")){
			out.println("<html>");
	        out.println("<head>");
			out.println("</head>");
	        out.println("<body>");
	        out.println("Während der Verarbeitung ist ein Fehler im Bezug auf die Marketingstrategien aufgetreten."
	        		+ "Es scheint als sei das Verzeichnis / die Datei nicht vorhanden oder lässt sich nicht öffnen bzw. schreiben.");
	        if(session.getAttribute("errorFile") != null){
	        	out.println(session.getAttribute("errorFile").toString());
	        	session.removeAttribute("errorFile");
	        }
	        out.println("</body>");
	        out.println("</html>");
		}else if(fehlerNr.equals("2")){
			out.println("<html>");
	        out.println("<head>");
			out.println("</head>");
	        out.println("<body>");
	        out.println("Während der Verarbeitung ist ein Fehler im Bezug auf die Marketingstrategien aufgetreten."
	        		+ "Folgende Datei lässt sich nicht öffnen bzw. schreiben:");
	        if(session.getAttribute("errorFile") != null){
	        	out.println(session.getAttribute("errorFile").toString());
	        	session.removeAttribute("errorFile");
	        }
	        out.println("</body>");
	        out.println("</html>");
		}
		else if(fehlerNr.equals("3")){
			out.println("<html>");
	        out.println("<head>");
			out.println("</head>");
	        out.println("<body>");
	        out.println("Während der Verarbeitung ist ein Fehler im Bezug auf die Marketingstrategien aufgetreten."
	        		+ "Es konnten keine Marketingmaßnahmen gelesen bzw. erstellt werden.");
	        out.println("</body>");
	        out.println("</html>");
		}
		
		session.removeAttribute("errorLevel");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		try {
			MarketingHelper helper = new MarketingHelper(request,response);
			if(!helper.addValue(request.getParameter("marketingAction").toString())){
				//Eintrag bereits vorhanden ggf. Fehlermeldung
			}
		} catch (ClassNotFoundException e) {
			//Mal kucken
		}
		
		response.sendRedirect("Analyse.jsp");
	}

}
