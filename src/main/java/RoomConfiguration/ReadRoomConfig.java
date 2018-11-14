package RoomConfiguration;

import com.google.gson.Gson;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class ReadRoomConfig {
    Gson gson = new Gson();

    public ArrayList<RoomDataModel> GetRoomData() throws IOException {
        String data = ReadFile("target/classes/roomdata.txt");
        RoomDataModel[] roomData = gson.fromJson(data, RoomDataModel[].class);
        return new ArrayList<RoomDataModel>(Arrays.asList(roomData));
    }



    private String ReadFile(String file) throws IOException {
        return new Scanner(new File(file)).useDelimiter("\\Z").next();
    }

    public void SaveRoomData(ArrayList<RoomDataModel> roomData) throws FileNotFoundException{
        File f = new File("target/classes/roomdata.txt");
        if(f.isFile()) {
            try (PrintWriter out = new PrintWriter(f)) {
                out.println(gson.toJson(roomData));
            }
        } else {
            try {
                try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream(f), "utf-8"))) {
                    writer.write(gson.toJson(roomData));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
