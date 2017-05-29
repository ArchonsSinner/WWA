package Weka;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
/**
 * Created by florian on 23.05.17.
 */
@WebServlet("/DataControl")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 3, maxFileSize = 1024 * 1024 * 40, maxRequestSize = 1024 * 1024
* 50)
public class DataControl extends HttpServlet{
    private static final long serialVersionUID = 1L;

    private static final String uploadPath = System.getProperty("user.dir") + File.separator + "WWA" + File.separator + "uploads";

    // upload settings
    private static final int MEMORY_THRESHOLD   = 1024 * 1024 * 3;  // 3MB
    private static final int MAX_FILE_SIZE      = 1024 * 1024 * 40; // 40MB
    private static final int MAX_REQUEST_SIZE   = 1024 * 1024 * 50; // 50MB

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
	
	try {
	    
        // checks if the request actually contains upload file
        if (!ServletFileUpload.isMultipartContent(request)) {
            //TBD Exception handling
            return;
        }
        
        
        HttpSession session = request.getSession();
        if(session.getAttribute("uname") == null)
        	response.sendRedirect("Login");

        // configures upload settings
        DiskFileItemFactory factory = new DiskFileItemFactory();
        // sets memory threshold - beyond which files are stored in disk
        factory.setSizeThreshold(MEMORY_THRESHOLD);
        // sets temporary location to store files

        ServletFileUpload upload = new ServletFileUpload(factory);

        // sets maximum size of upload file
        upload.setFileSizeMax(MAX_FILE_SIZE);

        // sets maximum size of request (include file + form data)
        upload.setSizeMax(MAX_REQUEST_SIZE);

        // creates the directory if it does not exist
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        
        if (uploadDir.canWrite()) {
			for (Part part : request.getParts()) {
				String fileName = extractFileName(part);
				// Schreibt den Dateinamen neu für den Fall, dass der Dateiname
				// einen Pfad enthält.
				fileName = new File(fileName).getName();
				if(fileName.endsWith(".csv")){
                            		String fullName = uploadDir + File.separator + new Date().toString().replaceAll(":", "_") + fileName;
					part.write(fullName);
					session.setAttribute("file", fullName);
                        	}
				
			}
		}

        /*  Check num of files in directory and delete all csv older than latest 5
        *
        * */
        File path = new File(uploadPath);
        File[] fileArray = path.listFiles();

        while (fileArray.length > 5) {
            File oldestFile = fileArray[0];

            for (int i = 1; i < fileArray.length; i++) {
                if (fileArray[i].lastModified() < oldestFile.lastModified())
                    oldestFile = fileArray[i];
            }
            oldestFile.delete();
            fileArray = path.listFiles();
        }

        response.sendRedirect("StartAnalysis");
	} catch (Exception e) {
		out.println("Es ist ein Fehler aufgetreten. Wenn der Fehler erneut auftritt, kontaktieren Sie bitte Ihren Administrator!");
	}
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
	try {
	    
	HttpSession session = request.getSession();
        if (session.getAttribute("uname")==null){
            response.sendRedirect("Login");
        }

        out.println("<table align='left'>" +
                "<tr>") ;
        
        if(request.getParameter("fileError")!=null){
        	out.println("<td><font color = RED>Datei ist fehlerhaft</font></td>"
        			+ "</tr>"
        			+ "<tr>");
        	request.removeAttribute("fileError");
        }
        out.println(
                "   <form action = 'DataControl' method = 'post' enctype = 'multipart/form-data'>" +
                "       <td><input type = 'file' accept= '.csv' name = 'file' size = '50' /></td>" +
                "           </tr><tr>" +
                "       <td><input type = 'submit' value = 'Upload File' /></td" +
                "   </form>" +
                "</tr>" +
                "</table>");

       
        
        
        
        //Set directory to search files
           
        
        File path = new File(uploadPath);
        
        
        //Abfrage ob Ordner vorhanden ist
    
        if(!path.exists())
        	path.mkdirs();
        
        
        
        
        File[] fileArray = path.listFiles();

        out.println("<table>");
        for (File f: fileArray
             ) {
            String name = f.getName();
            if(name.endsWith(".csv")){
                out.println("<tr><td><a href='/Weka/StartAnalysis?path=" + uploadPath + File.separator + name + "'>" + name + "</a></td></tr>");
            }
        }
        out.println("</table>");
	} catch (Exception e) {
		out.println("Es ist ein Fehler aufgetreten. Wenn der Fehler erneut auftritt, kontaktieren Sie bitte Ihren Administrator!");
	}
    }
    
    private String extractFileName(Part part) {
		//Dies soll den Dateinamen korrekt heraussuchen und bereinigen, sodass wir den leichter verwenden können.
		String contentDisp = part.getHeader("content-disposition");
		String[] items = contentDisp.split(";");
		for (String s : items) {
			if (s.trim().startsWith("filename")) {
				return s.substring(s.indexOf("=") + 2, s.length() - 1);
			}
		}
		return "";
	}
}
