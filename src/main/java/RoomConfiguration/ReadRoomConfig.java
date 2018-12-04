package RoomConfiguration;

import com.google.gson.Gson;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class ReadRoomConfig {
    Gson gson = new Gson();

    public ArrayList<RoomDataModel> GetRoomData(){
        String data = null;
        File f = new File("target/classes/roomdata.txt");
        if(f.isFile()) {
        try {
            data = ReadFile("target/classes/roomdata.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        RoomDataModel[] roomData = gson.fromJson(data, RoomDataModel[].class);
        return new ArrayList<>(Arrays.asList(roomData));

        } else {
            return new ArrayList<>();
        }
    }



    private String ReadFile(String file) throws IOException {
        return new Scanner(new File(file)).useDelimiter("\\Z").next();
    }

    public void SaveRoomData(ArrayList<RoomDataModel> roomData){
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
    }

}
