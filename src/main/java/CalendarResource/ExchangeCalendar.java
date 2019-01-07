package CalendarResource;

import AdminConfiguration.ReadClientConfig;
import CalendarResource.ExchangeCommunication.BearerProvider;
import CalendarResource.ExchangeCommunication.ReservationModels.Attendee;
import CalendarResource.ExchangeCommunication.ReservationModels.Callids;
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
import java.time.ZoneId;
import java.time.ZonedDateTime;
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

    /**
     * Constructor, creates an local memory mapping of the rooms configured in "config".
     * When ExchangeCalendar::Reload is called, this mapping will be synchronized with the exchange server.
     * @param config
     */
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
                    old.setEnd(newReservation.getEnd());
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
        for (int i = room.getReservations().size() - 1; i >= 0; i--) {
            Reservation reservation = room.getReservations().get(i);
            if(GetReservationByUUID(newReservations, reservation.getUuid()) == null){
                room.removeReservation(reservation);
                roomchanged = true;
            }
        }
        if(roomchanged) {
            room.roomChanged(Changed.CalendarUpdate);
        }
    }

    /**
     * Adds a new reservation to the internal memory.
     * When ExchangeCalendar::Reload is called, this new reservation will be pushed to the exchange server.
     * @param reservation reservation object
     */
    @Override
    public void createNewEvent(Reservation reservation){
        System.out.println("CREATE");

        reservation.setId(reservationId++);

        ReservationModel reservationModel = new ReservationModel();
        String roomEmail = "";
        for (RoomDataModel roomDataModel : config.GetRoomData()) {
            if (roomDataModel.getId() == getRoomIdByReservation(reservation)) {
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
            Callids callids = gson.fromJson(result, Callids.class);
            reservation.setUuid(callids.getiCalUId());
            reservation.setCalId(callids.getId());
            System.out.println("UUID: " + reservation.getUuid());
            System.out.println("ID: " + reservation.getCalId());
            //get uuid here and add it to reservation
        } catch (UnirestException e) {
            e.printStackTrace();
        }

    }

    /**
     * Synchronise with the exchange server. All newly added, deleted or updated reservation both stored locally or
     * in the server will be used to update the two servers. A call to this method will make sure the local cache is up to date.
     * A call to this method on regular bases is recommended.
     */
    @Override
    public void Reload() {
        getRooms();
    }

    public static void main(String[] args){
        ExchangeCalendar temp = new ExchangeCalendar(new ReadRoomConfig());

        while(true){
            temp.Reload();
        }

    }

    /**
     * Gets a live copy to the local memory mapping of Exchange.
     * Any changes made outside the ExchangeCalendar class to this copy will make it invalid
     * @return
     */
    @Override
    public List<Room> getRooms() {

        String calID = "";
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        ZonedDateTime dateNow = ZonedDateTime.ofInstant(cal.toInstant(), ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("Z"));

        cal.add(Calendar.DAY_OF_YEAR,1 );
        ZonedDateTime dateEnd = ZonedDateTime.ofInstant(cal.toInstant(), ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("Z"));
        String start = (dateNow.format(formatter));
        String end = (dateEnd.format(formatter));
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
            if(responses == null){
                return rooms;
            }
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
                    if(next.get("isCancelled").toString().equals("true")){
                        continue;
                    }
                    calID = (String) next.get("id");
                    JSONArray attendees = (JSONArray) next.get("attendees");
                    if(attendees.size() > 0) {
                        JSONObject emailAddress = (JSONObject) ((JSONObject) attendees.get(0)).get("emailAddress");
                        Reservation reservation = new Reservation(reservationId++, emailAddress.get("name").toString(), false, ParseTime((JSONObject) next.get("start")), ParseTime((JSONObject) next.get("end")));
                        reservation.setUuid(next.get("iCalUId").toString());
                        reservation.setCalId(calID);
                        reservations.add(reservation);
                    }
                }

                ModifyRoomReservations(room, reservations);
            }
        } catch (UnirestException | ParseException e) {
            e.printStackTrace();
        }

        return rooms;
    }

    private LocalDateTime ParseTime(JSONObject time){
        //System.out.println();
        //System.out.println(time.get("timeZone"));

        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

        ZonedDateTime zoned = ZonedDateTime.parse(time.get("dateTime").toString().substring(0, 23)+"Z", formatter);
        return zoned.withZoneSameInstant(ZoneId.of("Europe/Paris")).toLocalDateTime();
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

    /**
     * Makes a call to the Exchange Server to get all users. Then return those.
     * @return
     */
    @Override
    public List<User> getUsers() {
        cachedUsers.clear();
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
        ReservationModel reservationModel = new ReservationModel();
        String roomEmail = "";


        for (RoomDataModel roomDataModel : config.GetRoomData()) {
            if (roomDataModel.getId() == getRoomIdByReservation(reservation)) {
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

        System.out.println("https://graph.microsoft.com/v1.0/users/" + roomEmail +"/events/" + reservation.getCalId());

        try {
            String result = Unirest.patch("https://graph.microsoft.com/v1.0/users/" + roomEmail +"/events/" + reservation.getCalId())
                    .header("accept", "application/json")
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + bearerProvider.getBearer())
                    .body(gson.toJson(reservationModel))
                    .asJson().getBody().toString();
            System.out.println("Reservation updated: " + result);
        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }

    public int getRoomIdByReservation(Reservation reservation){
        int roomId = 0;
        ReservationProvider reservationProvider = ReservationProvider.getInstance();
        for (Room room : reservationProvider.getCollection().getAllRooms()){
            for (Reservation reservationTemp : room.getReservations()){
                if (reservationTemp.getId() == reservation.getId()){
                    roomId = room.getId();
                }
            }
        }
        return roomId;
    }
}
