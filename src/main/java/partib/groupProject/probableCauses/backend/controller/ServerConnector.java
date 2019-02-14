package partib.groupProject.probableCauses.backend.controller;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class ServerConnector {
    private static String server_ip = ""; //TODO try not to push with the server_ip there

    public static String singleQueryCaller(String s) {
        try {
            URL url = new URL("http://"+ server_ip + ":8080");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");  // TODO Make it a POST
            con.setRequestProperty("", ""); // TODO put it in the same format we use

            //int responseCode = con.getResponseCode();

            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLIne;
            StringBuilder response = new StringBuilder();
            while((inputLIne = br.readLine()) != null) {
                response.append(inputLIne);
            }
            br.close();
            con.disconnect();

            return response.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<String> queryCaller(List<String> list) { //TODO do this method also
        for(int i = 0; i < list.size(); i++) list.set(i, singleQueryCaller(list.get(i)));
        return list;
    }
}
