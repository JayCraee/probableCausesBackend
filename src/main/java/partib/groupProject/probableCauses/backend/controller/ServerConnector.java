package partib.groupProject.probableCauses.backend.controller;

import java.io.*;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.ArrayList;

public class ServerConnector {

	private static String server_ip = ""; //TODO try not to push with the server_ip there
	
	public static String singleQueryCaller(String db, String query) {
		List<String> q = new ArrayList();
		q.add(query);
		return queryCaller(db, q);
	}
	
	public static String singleQueryCaller(String q){
		return singleQueryCaller("foo.bdb", q);
	}

	public static String queryCaller(String db, List<String> queries){
		String response = "";
		try {
			URL url = new URL("http://"+ server_ip + ":8080");
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST"); 
			
			if(queries.isEmpty()) throw new InvalidCallException("No queries provided.");

			String msg = db + "\n";
			
			for(String q : queries){
				msg += q + "\n";
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
			String tmp;
			switch(rc){
			
				case HttpURLConnection.HTTP_OK:
					BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
					while((tmp=in.readLine()) != null) response += tmp;
					break;
				default:
					System.out.println("Something went wrong!");
					break;

			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidCallException i) {
			System.out.println(i.getMessage());
		}

		return response; //TODO: See if I can just return the string rather than a list of strings -- non-trivial to parse due to CF property of JSON.
	}

	public static String queryCaller(List<String> queries) { 
		return queryCaller("foo.bdb", queries);
	}
}
