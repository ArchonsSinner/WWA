package Weka;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;

/**
 * Servlet implementation class FileUpload
 */
@WebServlet("/FileUpload")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 10, maxFileSize = 1024 * 1024 * 50, maxRequestSize = 1024 * 1024
		* 250)

public class FileUpload extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String folderPath = System.getProperty("user.dir") + File.separator + "WWA" + File.separator
			+ "uploads";
	private static final int numberofFiles = 5;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public FileUpload() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.sendRedirect("FileUpload.jsp");
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//doGet(request, response);

		// Es soll geprüft werden, ob die verwendeten Ordner auf dem Server
		// vorhanden sind und wenn icht sollen diese angelegt werden. Dies wird
		// benötigt, damit später die Datei vernünftig hochgeladen werden kann.

		File folder = new File(folderPath);
		String date = new Date().toString();

		if (!folder.exists()) {
				folder.mkdirs();
		} else {
			File[] files = folder.listFiles();
			//Da nur numberofFiles Dateien aufbewart werden (zum jetzigen Zeitpunkt 5) muss überprüft werden wie viele Dateien vorhanden sind und ggf. die letzte gelöscht werden.
			while (files.length >= numberofFiles) {
				File oldestFile = files[0];

				for (int i = 1; i < files.length; i++) {
					if (files[i].lastModified() < oldestFile.lastModified())
						oldestFile = files[i];
				}

				if (folder.canWrite()) {
					oldestFile.delete();
				}
			}
		}

		if (folder.canWrite()) {
			for (Part part : request.getParts()) {
				String fileName = extractFileName(part);
				// Schreibt den Dateinamen neu für den Fall, dass der Dateiname
				// einen Pfad enthält.
				fileName = new File(fileName).getName();
				part.write(folderPath + File.separator + date + fileName);
			}
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
