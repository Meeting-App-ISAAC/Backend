package CalendarResource;

import AdminConfiguration.ReadClientConfig;
import CalendarResource.ExchangeCommunication.BearerProvider;
import CalendarResource.ExchangeCommunication.ReservationModels.Attendee;
import CalendarResource.ExchangeCommunication.ReservationModels.ReservationModel;
import Communication.ReservationProvider;
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
import java.util.*;
import Reservation.Changed;
public class ExchangeCalendar implements Calender {


    private RoomConfig config;
    private BearerProvider bearerProvider = new BearerProvider(new ReadClientConfig());
    private Gson gson = new Gson();
    private ArrayList<Room> rooms = new ArrayList<Room>();

    private int userId = 1;
    private int reservationId = 1;

    public ExchangeCalendar(RoomConfig config) {
        this.config = config;
        for(RoomDataModel roomData : config.GetRoomData()){
            ArrayList<Reservation> reservations = new ArrayList<Reservation>();
            Room room = new Room(roomData.getId(), roomData.getName(), reservations);
            rooms.add(room);
        }
    }


    private Room GetRoomById(int id){
        for(Room room : rooms){
            if(room.getId() == id){
                return room;
            }
        }
        return null;
    }

    private Reservation GetReservationByUUID(ArrayList<Reservation> reservations, String UUID){
        for (Reservation reservation : reservations) {
            if(reservation.getUuid().equals(UUID)){
                return reservation;
            }
        }
        return null;
    }

    private void ModifyRoomReservations(Room room, ArrayList<Reservation> newReservations){

        boolean roomchanged = false;
        for (Reservation newReservation : newReservations) {
            Reservation old = GetReservationByUUID(room.getReservations(), newReservation.getUuid());
            //reservation already exists
            if(old != null){
                boolean changed = false;
                if(!old.getTitle().equals(newReservation.getTitle())){
                    old.setTitle(newReservation.getTitle());
                    changed = true;
                }
                if(!old.getStart().equals(newReservation.getStart())){
                    old.setStart(newReservation.getStart());
                    changed = true;
                }
                if(!old.getEnd().equals(newReservation.getEnd())){
                    old.setStart(newReservation.getEnd());
                    changed = true;
                }
                if(changed){
                    roomchanged = true;
                    old.Changed(Changed.CalendarUpdate);
                }
                continue;
            }
            roomchanged = true;
            room.addReservation(newReservation);

        }
        for (Reservation reservation : room.getReservations()) {
            if(GetReservationByUUID(newReservations, reservation.getUuid()) == null){
                room.removeReservation(reservation);
                roomchanged = true;
            }
        }
        room.roomChanged(Changed.CalendarUpdate);
    }

    @Override
    public void createNewEvent(Reservation reservation){
        ReservationModel reservationModel = new ReservationModel();
        int roomId = 0;
        ReservationProvider reservationProvider = ReservationProvider.getInstance();
        for (Room room : reservationProvider.getCollection().getAllRooms()){
            for (Reservation reservationTemp : room.getReservations()){
                if (reservationTemp.getId() == reservation.getId()){
                    roomId = room.getId();
                }
            }
        }
        String roomEmail = "";
        for (RoomDataModel roomDataModel : config.GetRoomData()) {
            if (roomDataModel.getId() == roomId) {
                roomEmail = roomDataModel.getEmail();
                reservationModel.setSubject(reservation.getTitle());
                reservationModel.getBody().setContentType("HTML");
                reservationModel.getBody().setContent("Automatic room reservation");
                reservationModel.getStart().setDateTime(reservation.getStart().toString());
                reservationModel.getStart().setTimeZone("Europe/Amsterdam");
                reservationModel.getEnd().setDateTime(reservation.getEnd().toString());
                reservationModel.getEnd().setTimeZone("Europe/Amsterdam");
                reservationModel.getLocation().setDisplayName(roomDataModel.getLocation());
                ArrayList<Attendee> attendees = new ArrayList<>();
                Attendee attendee = new Attendee();
                attendee.getEmailAddress().setAddress("Fontysgroup2@isaacfontys.onmicrosoft.com");
                attendee.getEmailAddress().setName("Alex");
                attendee.setType("required");
                attendees.add(attendee);
                reservationModel.setAttendees(attendees);
                //-TODO The model has possible room for attendees as well add if needed
            }
        }

        try {
            String result = Unirest.post("https://graph.microsoft.com/v1.0/users/" + roomEmail +"/events")
                    .header("accept", "application/json")
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + bearerProvider.getBearer())
                    .body(gson.toJson(reservationModel))
                    .asJson().getBody().toString();
            System.out.println("New reservation added: " + result);
        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void Reload() {
        getRooms();
    }

    public static void main(String[] args){
        new ExchangeCalendar(new ReadRoomConfig()).getRooms();
    }
    @Override
    public List<Room> getRooms() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String start = (LocalDateTime.now().format(formatter)) + "T00:00:00";
        String end = (LocalDateTime.now().plusDays(1).format(formatter)) + "T00:00:00";
        RequestContainer container = new RequestContainer();

        for (RoomDataModel roomDataModel : config.GetRoomData()) {
            if (roomDataModel.getEmail() != null && !roomDataModel.getEmail().isEmpty()) {
                RequestModel model = new RequestModel();
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

                JSONObject roomIterator = iterator.next();
                Room room = GetRoomById(Integer.parseInt(roomIterator.get("id").toString()));


                JSONObject body = (JSONObject) roomIterator.get("body");
                JSONArray value = (JSONArray) body.get("value");
                Iterator<JSONObject> valueIterator = value.iterator();

                ArrayList<Reservation> reservations = new ArrayList<Reservation>();

                while (valueIterator.hasNext()){
                    JSONObject next = valueIterator.next();
                    JSONObject organizer = (JSONObject) next.get("organizer");
                    JSONObject emailAddress = (JSONObject) organizer.get("emailAddress");
                    Reservation reservation = new Reservation(reservationId++,emailAddress.get("name").toString(),false,ParseTime((JSONObject) next.get("start")), ParseTime((JSONObject) next.get("end")));
                    reservation.setUuid(next.get("iCalUId").toString());
                    reservations.add(reservation);
                }

                ModifyRoomReservations(room, reservations);
            }
        } catch (UnirestException | ParseException e) {
            e.printStackTrace();
        }

        return rooms;
    }

    private LocalDateTime ParseTime(JSONObject time){
        System.out.println();
        System.out.println(time.get("timeZone"));

        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        return LocalDateTime.parse(time.get("dateTime").toString(), formatter);
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

    private List<User> cachedUsers = new ArrayList<User>();
    @Override
    public List<User> getUsers() {
        ArrayList<User> users = new ArrayList<User>();
        String result = null;
        try {
            result = Unirest.get("https://graph.microsoft.com/v1.0/users/")
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + bearerProvider.getBearer())
                    .asString().getBody();

            JSONObject jo = null;

            jo = (JSONObject) (new JSONParser()).parse(result);
            JSONArray responses = (JSONArray) jo.get("value");
            Iterator<JSONObject> iterator = responses.iterator();
            while (iterator.hasNext()) {
                JSONObject user = (JSONObject) iterator.next();
                if (!UserExists(user.get("id").toString())) {
                    User newUser = new User(userId++, user.get("displayName").toString());
                    newUser.setUid(user.get("id").toString());
                    newUser.setEmail((user.get("mail") == null? null : user.get("mail").toString()));
                    cachedUsers.add(newUser);
                }
            }


        }catch (UnirestException | ParseException e) {
            e.printStackTrace();
        }
        return cachedUsers;
    }

    private boolean UserExists(String uid){
        for (User cachedUser : this.cachedUsers) {
            if(cachedUser.getUid().equals(uid)){
                return true;
            }
        }
        return false;
    }

    private User GetUserByEmail(String email){
        for (User cachedUser : this.cachedUsers) {
            if(cachedUser.getEmail().equals(email)){
                return cachedUser;
            }
        }
        return null;
    }

    @Override
    public void updateEvent(Reservation reservation) {

    }
}
