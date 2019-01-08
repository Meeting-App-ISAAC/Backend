package AdminConfiguration;

import com.google.gson.Gson;

import java.io.*;

import java.net.URISyntaxException;
import java.util.Scanner;
import java.util.stream.Collectors;

public class ReadAdminConfig {

    private Gson gson = new Gson();
    private String cached;
    public String GetAdminData() {
        try {
            return ReadFile();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public boolean CheckAdminPass(String original){
        return GetAdminData().equals(original);
    }
    private String ReadFile() throws IOException, URISyntaxException {
        if(cached != null){
            return cached;
        }
        InputStream in = getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        cached = reader.lines().collect(Collectors.joining());
        return cached;
    }

    InputStream getInputStream(){
        String path = "./admin.txt";
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(path);
            return fis;
        } catch (FileNotFoundException e) {
            return ReadAdminConfig.class.getClassLoader().getResourceAsStream("admin.txt");
        }
    }

}
