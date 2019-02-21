package partib.groupProject.probableCauses.backend;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Component
public class AutomatedAPITester implements CommandLineRunner {

    AutomatedAPITester(){};

    @Override
    public void run(String... strings) {
        if(!strings[0].equals("1")) {
            System.out.println("not testing");
            return;
        }

        System.out.println("testing");
        String s = null;
        try {
            List<String> allLines = Files.readAllLines(Paths.get("test.txt"), StandardCharsets.UTF_8);

            for(String query : allLines) {
                Process p = Runtime.getRuntime().exec("curl -v " + query);

                BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
                BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

                System.out.println("standard output of the command " + query);
                while((s = stdInput.readLine()) != null) {
                    System.out.println("h " + s + " h");
                    System.out.println("not finished");
                }

                System.out.println("standard error of the command " + query);
                while((s = stdError.readLine()) != null) {
                    System.out.println(s);
                }
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
