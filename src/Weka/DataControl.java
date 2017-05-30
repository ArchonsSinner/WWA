package Weka;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
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
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 3, maxFileSize = 1024 * 1024 * 40, maxRequestSize = 1024 * 1024 * 50)
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

            response.sendRedirect("DataControl");
        } catch (Exception e) {
            response.sendRedirect("Error.html");
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

            //HTML Head
            out.print(  "<html>"+
                    "<head>"+
                    "<link rel='stylesheet' href='bulma.css'>"+
                    "<meta charset='utf-8'/>"+
                    "<title>Weka Web App</title>"+
                    "<nav class='nav' style='background-color: #BDBDBD'>"+
                    "<div class='nav-left'>"+
                    "<a class='nav-item'>"+
                    "<img src='logo.png' alt='KD logo'>"+
                    "</a>"+
                    "</div>"+
                    "</nav>"+
                    "</head>");

            //HTML Upload select
            out.print(  "<body>"+
                    "<div class='columns' style='margin-top: 15px'>"+
                    "<div class='column is-1'>"+
                    "</div>"+
                    "<div class='column is-3'>"+
                    "<aside class='menu'>"+
                    "<p class='menu-label'>"+
                    "Datei Hochladen"+
                    "</p>"+
                    "<ul class='menu-list'>"+
                    "<form action='DataControl' method='post' enctype='multipart/form-data'>"+
                    "<input type='file' accept='.csv' name='file' size='50'>"+
                    "</br>"+
                    "</br>"+
                    "<input type='submit' class='button is-info' value='Upload File'>"+
                    "</form>"+
                    "</ul>");

            //HTML Upload Error
            if(request.getParameter("fileError")!=null){
                out.print   ("<div class='notification is-danger'>"+
                        "Datei ist Fehlerhaft! Upload abgebrochen."+
                        "</div>");
                request.removeAttribute("fileError");
            }

            //Set directory to search files


            File path = new File(uploadPath);


            //Abfrage ob Ordner vorhanden ist

            if(!path.exists())
                path.mkdirs();

           
            //HTML prepare output filenames
            out.print(  "</div>"+
                    "<div class='column is-3'>"+
                    "<aside class='menu'>"+
                    "<p class='menu-label'>"+
                    "Datei auswählen"+
                    "</p>"+
                    "<ul class='menu-list'>");

            File[] fileArray = path.listFiles();
            for (File f: fileArray
                    ) {
                String name = f.getName();
                if(name.endsWith(".csv")){
                    //HTML file name output
                    out.print(" <a href='/Weka/StartAnalysis?path=" + uploadPath + File.separator + name + "'>" + name + "</a>");
                }
            }

            //Load Data from saved analysis
            SaveAnalysisHelper helper = new SaveAnalysisHelper();
            HashMap<String, Integer> map = helper.getMap();

            Set<String> old_filenames_tmp = map.keySet();
            String old_filenames[] = old_filenames_tmp.toArray(new String[old_filenames_tmp.size()]);
            int old_num_cluster[] = new int[5];
            for(int i=0; i<5; i++){
                old_num_cluster[i] = map.get(old_filenames[i]);
            }

            //HTML output last files
            out.print(  "</ul>"+
                    "</aside>"+
                    "</div>"+
                    "<div class='column'>"+
                    "<aside class='menu'>"+
                    "<p class='menu-label'>"+
                    "Vorherige Analysen"+
                    "</p>");

            for(int i=0; i<map.size(); i++){
                out.print("<a href='/Weka/old_helper?clust=" + old_num_cluster[i] + "&path=" + uploadPath + File.separator + old_filenames[i] + "'>" + old_filenames[i] + " Cluster: " + old_num_cluster[i] + "</a><br>");
            }

            if (map.size()==0){
                out.print("Keine Verfügbar");
            }

            out.print("</ul></aside>"+
                    "</div>"+
                    "</div>"+
                    "</body>"+
                    "</html>");
            
        } catch (Exception e) {
            response.sendRedirect("Error.html");
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
