package Weka;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class MarketingHelper {
	private static String path = System.getProperty("user.dir") + File.separator + "WWA" + File.separator + "Marketing";
	private static File directory = new File(path);
	private static String file = "marketingSetFile";
	private static File setFile = new File(path + File.separator + file);
	private static Set<String> marketingSet = new HashSet<String>();
	private static HttpServletRequest request;
	private static HttpServletResponse response;
	
	public MarketingHelper(HttpServletRequest req, HttpServletResponse res){
		request = req;
		response = res;
	}

 	public boolean addValue(String value) throws IOException, ClassNotFoundException{
		if(loadSet()){		
			if(marketingSet.add(value)){
				if(saveSet())
					return true;
				else{
					//Datei konnte nich gespeichert werden
					HttpSession session = request.getSession();
					session.setAttribute("errorLevel", "2");
					response.sendRedirect("MarketingHelper");
					return false;
				}
			}
			else
				return false;
			}
		else{
			HttpSession session = request.getSession();
			session.setAttribute("errorLevel", "3");
			response.sendRedirect("MarketingHelper");
			//Set konnte nicht geladen werden
			return false;
		}
	}
	
	public Set<String> getSet() throws IOException, ClassNotFoundException{
		if(loadSet()){
			return marketingSet;
		}
		else
		{
			//Set konnte nicht geladen werden. Set wird übergeben, dass möglicherweise leer ist
			return marketingSet;
		}
		
	}
	
	private boolean loadSet() throws IOException, ClassNotFoundException, FileNotFoundException{
		if(!directory.exists())
			if(directory.mkdirs()){
				//alles gut
			}
			else{
				HttpSession session = request.getSession();
				session.setAttribute("errorLevel", "2");
				session.setAttribute("errorFile", directory.getPath());
				response.sendRedirect("MarketingHelper");
				return false;
				//Verzeichnis kann nicht erstellt werden
			}

		if(directory.canRead()){
			try{
			FileInputStream finStream = new FileInputStream(setFile);
			ObjectInputStream oinStream = new ObjectInputStream(finStream);
			
				marketingSet = (Set<String>) oinStream.readObject();
				oinStream.close();
				return true;
			}catch(FileNotFoundException e){
				if(!saveSet()){
					HttpSession session = request.getSession();
					session.setAttribute("errorLevel", 1);
					session.setAttribute("errorFile", setFile.getPath());
					response.sendRedirect("MarketingHelper");
					//Datei ist nich vorhanden und kann nicht erzeugt werden
				}
			}catch(ClassNotFoundException e){
				HttpSession session = request.getSession();
				session.setAttribute("errorLevel", "0");
				response.sendRedirect("MarketingHelper");
			}catch(IOException e){
				HttpSession session = request.getSession();
				session.setAttribute("errorLevel", "0");
				response.sendRedirect("MarketingHelper");
			}
		}
		else{
			//error
			return false;
		}
		return false;
	}
	
	private boolean saveSet() throws IOException{
		if(!directory.exists())
			if(directory.mkdirs()){
				//alles gut
			}else{
				HttpSession session = request.getSession();
				session.setAttribute("errorLevel", "2");
				session.setAttribute("errorFile", directory.getPath());
				response.sendRedirect("MarketingHelper");
				return false;
				//Verzeichnis kann nicht erstellt werden
			}
		
		try{
			if(directory.canWrite()){
				FileOutputStream foutStream = new FileOutputStream(setFile);
				ObjectOutputStream ooutStream = new ObjectOutputStream(foutStream);
				ooutStream.writeObject(marketingSet);
				ooutStream.close();
				return true;
			}
			else{
				HttpSession session = request.getSession();
				session.setAttribute("errorLevel", "2");
				session.setAttribute("errorFile", directory.getPath());
				response.sendRedirect("MarketingHelper");
				return false;
				//Es kann nicht in das Verzeichnis geschrieben werden
			}
		}catch(IOException e){
			
		}
		return false;
	}
	
	public String genOptions() throws IOException, ClassNotFoundException{
		String options = "";
		if(loadSet()){
			for(String s: marketingSet){
				options += "<option>" + s + "</option>";
			}
			return options;
		}
		else{
			HttpSession session = request.getSession();
			session.setAttribute("errorLevel", "3");
			response.sendRedirect("MarketingHelper");
			//Set konnte nicht geholt werden
			return options;
		}
	}
}


