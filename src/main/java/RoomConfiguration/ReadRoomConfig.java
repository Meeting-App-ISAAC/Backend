package RoomConfiguration;


import AdminConfiguration.ReadAdminConfig;
import com.google.gson.Gson;


import java.io.*;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;

public class ReadRoomConfig implements RoomConfig {
    private Gson gson = new Gson();

    public ArrayList<RoomDataModel> GetRoomData(){

        try {
            String data = ReadFile();
            RoomDataModel[] roomData = gson.fromJson(data, RoomDataModel[].class);
            return new ArrayList<>(Arrays.asList(roomData));
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }

    }

    public RoomDataModel GetRoomData(String secret){
        ArrayList<RoomDataModel> all = GetRoomData();
        for(RoomDataModel item : all){
            if(item.getSecret().equals(secret)){
                return item;
            }
        }
        return null;
    }


    private String cached;
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
        String path = "./roomdata.txt";
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(path);
            return fis;
        } catch (FileNotFoundException e) {
            return ReadRoomConfig.class.getClassLoader().getResourceAsStream("/roomdata.txt");
        }

    }


    public void SaveRoomData(ArrayList<RoomDataModel> roomData) {

        /*
        File f = new File("target/classes/roomdata.txt");
        if(f.isFile()) {
            try (PrintWriter out = new PrintWriter(f)) {
                out.println(gson.toJson(roomData));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            try {
                try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream(f), StandardCharsets.UTF_8))) {
                    writer.write(gson.toJson(roomData));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        */
    }

}
