package CalendarResource.ExchangeCommunication;

import AdminConfiguration.ReadClientConfig;
import Reservation.Room;
import RoomConfiguration.ReadRoomConfig;
import com.google.gson.Gson;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RestGraphApi {

    public Room GetRoom;

    ReadRoomConfig readRoomConfig = new ReadRoomConfig();




    public List<Room> getIds(){


        return null;
    }

    private class RoomEventCollection{
        public String Email;
        public ArrayList<String> calIds  = new ArrayList<>();
    }
}
