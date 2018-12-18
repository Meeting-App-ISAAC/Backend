package CalendarResource.ExchangeCommunication;

import AdminConfiguration.ReadClientConfig;
import Reservation.Room;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
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
    BearerProvider bearerProvider = new BearerProvider(new ReadClientConfig());
    Gson gson = new Gson();

    public static void main(String[] args){
        RestGraphApi restGraphApi = new RestGraphApi();
        List<String> emails = new ArrayList<>();
        emails.add("fontysroom2@isaacfontys.onmicrosoft.com");
        emails.add("fontysroom1@isaacfontys.onmicrosoft.com");
        restGraphApi.getIds(emails);


    }

    public List<RoomEventCollection> getIds(List<String> emails){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String start = (LocalDateTime.now().format(formatter)) + "T00:00:00";
        String end = (LocalDateTime.now().plusDays(1).format(formatter)) + "T00:00:00";
        RequestContainer container = new RequestContainer();
        int id = 1;
        for (String email : emails) {
            RequestModel model = new RequestModel();
            model.id = id++;
            model.url = "/users/" + email + "/calendarview?startDatetime="+start+"&endDatetime="+end;
            container.requests.add(model);
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
                JSONObject body = (JSONObject) iterator.next().get("body");
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
    private class RoomEventCollection{
        public String Email;
        public ArrayList<String> calIds  = new ArrayList<>();
    }
}
