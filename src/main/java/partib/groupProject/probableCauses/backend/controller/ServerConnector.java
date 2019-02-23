package partib.groupProject.probableCauses.backend.controller;

/**
*   ServerConnector.java
*   @author dks28 jhl69 (edited)
*   
*   Provides a Java API to connect to the data base server with queries.
*   The current state is bad Java practice; shouldn't be a collection of static methods, 
*   rather should provide constructors for ServerConnector objects that then take information 
*   on server address and port, such that they don't need to be hard coded.
*/

import java.io.*;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.ArrayList;

public class ServerConnector {

	private static String server_ip = "dev-1b-oscar.cl.cam.ac.uk"; //TODO try not to push with the server_ip there dev-1b-oscar.cl.cam.ac.uk

	public static boolean isStringNullOrWhiteSpace(String value) {
		if (value == null) {
			return true;
		}

		for (int i = 0; i < value.length(); i++) {
			if (!Character.isWhitespace(value.charAt(i))) {
				return false;
			}
		}
		return true;
	}	

	public static String singleQueryCaller(String db, String query) throws InvalidCallException{
		List<String> q = new ArrayList();
		q.add(query);
		return queryCaller(db, q);
	}
	
	public static String singleQueryCaller(String q) throws InvalidCallException{
		throw new InvalidCallException("This method is deprecated. " + 
			"Please provide a data base file to operate on and call singleQueryCaller(String db, String query)");
	}

	public static String queryCaller(String db, List<String> queries) throws InvalidCallException {
		String response = "";
		// Filter out empty queries
		List<String> tmp = new ArrayList(queries);
		for(String q : queries){
			if(isStringNullOrWhiteSpace(q)) tmp.remove(q);
		}
		queries = tmp;

		try {
			URL url = new URL("http://" + server_ip + ":8082");
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST"); 
			
			if(queries.isEmpty()) throw new InvalidCallException("No queries provided.");

			String msg = db;
			
			for(String q : queries){
				msg += "\n" + q;
			}

			con.setDoOutput(true);
			
			OutputStream os = con.getOutputStream();
			try{
				os.write(msg.getBytes());
			} catch (IOException io){
				System.out.println("Caught IOException.");
			} finally {
				os.flush();
				os.close();
			}

			int rc = con.getResponseCode();
			response = "";
			String tmpstr;
			switch(rc){
			
				case HttpURLConnection.HTTP_OK:
					BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
					while((tmpstr=in.readLine()) != null) response += tmpstr;
					break;
				default:
					System.out.println("Something went wrong!");
					break;

			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return response; 
	}

	public static String queryCaller(List<String> queries) throws InvalidCallException {
		throw new InvalidCallException("This method is deprecated. " + 
			"Please provide a data base file to operate on and call queryCaller(String db, List<String> queries)");
	}

	public static String fooQueryCaller(String query) throws InvalidCallException {
		return singleQueryCaller("foo.bdb", query);
	}

	public static String fooQueryCaller(List<String> queries) throws InvalidCallException {
		return queryCaller("foo.bdb", queries);
	}
}
