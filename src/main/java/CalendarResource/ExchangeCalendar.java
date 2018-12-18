package CalendarResource;

import AdminConfiguration.ReadClientConfig;
import CalendarResource.ExchangeCommunication.BearerProvider;
import Reservation.Reservation;
import Reservation.Room;
import Reservation.User;
import RoomConfiguration.ReadRoomConfig;
import RoomConfiguration.RoomConfig;
import RoomConfiguration.RoomDataModel;
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

public class ExchangeCalendar implements Calender {


    private RoomConfig config;
    private BearerProvider bearerProvider = new BearerProvider(new ReadClientConfig());
    private Gson gson = new Gson();

    public ExchangeCalendar(RoomConfig config) {
        this.config = config;
    }

    @Override
    public void createNewEvent(Reservation reservation) {

    }

    public static void main(String[] args){
        new ExchangeCalendar(new ReadRoomConfig()).getRooms();


    }
    @Override
    public List<Room> getRooms() {
        ArrayList<String> emails = new ArrayList<>();



        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String start = (LocalDateTime.now().format(formatter)) + "T00:00:00";
        String end = (LocalDateTime.now().plusDays(1).format(formatter)) + "T00:00:00";
        ExchangeCalendar.RequestContainer container = new ExchangeCalendar.RequestContainer();

        for (RoomDataModel roomDataModel : config.GetRoomData()) {
            if (roomDataModel.getEmail() != null && !roomDataModel.getEmail().isEmpty()) {
                ExchangeCalendar.RequestModel model = new ExchangeCalendar.RequestModel();
                model.id = roomDataModel.getId();
                model.url = "/users/" + roomDataModel.getEmail() + "/calendarview?startDatetime=" + start + "&endDatetime=" + end;
                container.requests.add(model);
            }
        }

        try {
            String result = Unirest.post("https://graph.microsoft.com/v1.0/$batch")
                    .header("accept", "application/json")
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + bearerProvider.getBearer())
                    .body(gson.toJson(container))
                    .asJson().getBody().toString();

            JSONObject jo = (JSONObject) (new JSONParser()).parse(result);
            JSONArray responses = (JSONArray) jo.get("responses");
            Iterator<JSONObject> iterator = responses.iterator();
            while (iterator.hasNext()){
                Room room = new Room();

                JSONObject roomIterator = iterator.next();
                room.setId(Integer.parseInt(roomIterator.get("id").toString()));


                JSONObject body = (JSONObject) roomIterator.get("body");
                JSONArray value = (JSONArray) body.get("value");
                Iterator<JSONObject> valueIterator = value.iterator();
                while (valueIterator.hasNext()){
                    JSONObject next = valueIterator.next();
                    JSONObject startTime =  (JSONObject) next.get("start");
                    System.out.println(startTime.get("dateTime"));
                    System.out.println(startTime.get("timeZone"));
                    JSONObject endTime =  (JSONObject) next.get("end");
                    System.out.println(endTime.get("dateTime"));
                    System.out.println(endTime.get("timeZone"));
                    JSONObject organizer = (JSONObject) next.get("organizer");
                    JSONObject orgEmail = (JSONObject) organizer.get("emailAddress");
                    System.out.println(orgEmail.get("name"));



                }
            }
        } catch (UnirestException | ParseException e) {
            e.printStackTrace();
        }

        return null;
    }


    private class RequestContainer{
        public ArrayList<RequestModel> requests = new ArrayList<>();

        public ArrayList<RequestModel> getRequests() {
            return requests;
        }

        public void setRequests(ArrayList<RequestModel> requests) {
            this.requests = requests;
        }
    }
    private class RequestModel{
        public int id;
        public String method = "GET";
        public String url = "";
    }

    @Override
    public List<User> getUsers() {
        return null;
    }

    @Override
    public void updateEvent(Reservation reservation) {

    }
}
