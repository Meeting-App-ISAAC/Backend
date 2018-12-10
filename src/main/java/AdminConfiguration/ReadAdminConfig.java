package AdminConfiguration;

import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;

import java.util.Scanner;

public class ReadAdminConfig {

        private Gson gson = new Gson();

        public String GetAdminData() {
            String data = null;
            File f = new File("target/classes/admin.txt");
            if (f.isFile()) {
                try {
                    data = ReadFile("target/classes/admin.txt");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String key = data;
                return key;

            } else {
                return "";
            }
        }


        private String ReadFile(String file) throws IOException {
            return new Scanner(new File(file)).useDelimiter("\\Z").next();
        }
}
