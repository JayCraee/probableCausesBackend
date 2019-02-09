package partib.groupProject.probableCauses.backend.controller;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class ServerConnector {

    public static String singleQueryCaller(String s) {
        try {
            URL url = new URL(""+ "/" + s);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("", ""); // come back

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

    public static List<String> queryCaller(List<String> list) {
        for(int i = 0; i < list.size(); i++) list.set(i, singleQueryCaller(list.get(i)));
        return list;
    }
}
