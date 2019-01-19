package CalendarResource;

import AdminConfiguration.ReadClientConfig;
import CalendarResource.ExchangeCommunication.BearerProvider;
import CalendarResource.ExchangeCommunication.ReservationModels.Attendee;
import CalendarResource.ExchangeCommunication.ReservationModels.ReservationModel;
import Reservation.Changed;
import Reservation.Reservation;
import Reservation.Room;
import Reservation.User;
import RoomConfiguration.RoomConfig;
import RoomConfiguration.RoomDataModel;
import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
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
import java.util.concurrent.LinkedBlockingQueue;

public class ExchangeCalendar implements Calender {


    private RoomConfig config;
    private BearerProvider bearerProvider = new BearerProvider(new ReadClientConfig());
    private Gson gson = new Gson();
    private ArrayList<Room> rooms = new ArrayList<Room>();
    private int userId = 1;
    private int reservationId = 1;

    private Queue<String> urlsToCall = new LinkedBlockingQueue<>();

    private String cachedStart;
    private List<User> cachedUsers = new ArrayList<User>();

    /**
     * Constructor, creates an local memory mapping of the rooms configured in "config".
     * When ExchangeCalendar::Reload is called, this mapping will be synchronized with the exchange server.
     *
     * @param config
     */
    public ExchangeCalendar(RoomConfig config) {
        this.config = config;
        for (RoomDataModel roomData : config.GetRoomData()) {
            ArrayList<Reservation> reservations = new ArrayList<Reservation>();
            Room room = new Room(roomData.getId(), roomData.getName(), reservations);
            rooms.add(room);
        }
        Reset();
    }

    private void InitUrlsToCall() {
        urlsToCall.clear();

        String start = getMidnightDateStringUTC(0);
        String end = getMidnightDateStringUTC(1);

        cachedStart = start;
        for (RoomDataModel roomDataModel : config.GetRoomData()) {
            if (roomDataModel.getEmail() != null && !roomDataModel.getEmail().isEmpty()) {
                urlsToCall.add("/users/" + roomDataModel.getEmail() + "/calendar/calendarView/delta?endDateTime=" + end + "&startDateTime=" + start);
            }
        }
    }

    private String getMidnightDateStringUTC(int dayOffset) {
        Calendar cal = Calendar.getInstance();

        cal.setTime(new Date());
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        cal.add(Calendar.DAY_OF_YEAR, dayOffset);

        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        ZonedDateTime dateNow = ZonedDateTime.ofInstant(cal.toInstant(), ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("Z"));
        return dateNow.format(formatter);
    }

    /**
     * Adds a new reservation to the internal memory.
     * When ExchangeCalendar::Reload is called, this new reservation will be pushed to the exchange server.
     *
     * @param reservation reservation object
     */
    @Override
    public synchronized void createNewEvent(Reservation reservation) {
        System.out.println("CREATE A NEW RESERVATION!!!");
        ReservationModel reservationModel = new ReservationModel();
        UpdateReservationModel(reservation, reservationModel);

        try {
            Unirest.post("https://graph.microsoft.com/v1.0/users/" + reservation.getUserEmail() + "/events")
                    .header("accept", "application/json")
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + bearerProvider.getBearer())
                    .body(gson.toJson(reservationModel))
                    .asJson();
            waitAndReload();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }

    private String UpdateReservationModel(Reservation reservation, ReservationModel reservationModel) {
        String roomEmail;
        for (RoomDataModel roomDataModel : config.GetRoomData()) {
            if (roomDataModel.getId() == getRoomIdByReservation(reservation)) {
                roomEmail = roomDataModel.getEmail();
                reservationModel.setSubject(reservation.getTitle());
                if (reservation.getHasStarted()) {
                    reservationModel.setSubject("started - " + reservation.getTitle());
                }
                reservationModel.getBody().setContentType("Text");
                if (reservation.getHasStarted()) {
                    reservationModel.getBody().setContent("Meeting has started");
                }
                reservationModel.getStart().setDateTime(FormatTimeToUTC(reservation.getStart()));
                reservationModel.getStart().setTimeZone("UTC");
                reservationModel.getEnd().setDateTime(FormatTimeToUTC(reservation.getEnd()));
                reservationModel.getEnd().setTimeZone("UTC");
                reservationModel.getLocation().setDisplayName(roomDataModel.getLocation());
                ArrayList<Attendee> attendees = new ArrayList<>();
                Attendee attendee = new Attendee();
                attendee.getEmailAddress().setAddress(roomEmail);
                attendee.getEmailAddress().setName(roomDataModel.getName());
                attendee.setType("resource");
                attendees.add(attendee);
                reservationModel.setAttendees(attendees);
                return roomEmail;
            }
        }
        return "";
    }

    private String FormatTimeToUTC(LocalDateTime time) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        ZonedDateTime date = ZonedDateTime.of(time, ZoneId.systemDefault());
        ZonedDateTime dateEnd = date.withZoneSameInstant(ZoneId.of("Z"));

        return dateEnd.format(formatter);
    }

    /**
     * Synchronise with the exchange server. All newly added, deleted or updated reservation both stored locally or
     * in the server will be used to update the two servers. A call to this method will make sure the local cache is up to date.
     * A call to this method on regular bases is recommended.
     */
    @Override
    public void Reload() {
        Reload(false);
    }
    private void Reload(boolean untilChange){
        int triesLeft = 15;
        boolean result = false;
        do {
            if (!cachedStart.equals(getMidnightDateStringUTC(0))) {
                Reset();
            }
            result = UpdateMemoryWithExchange();
            triesLeft--;
        }while (!result && untilChange && triesLeft >= 0);
    }

    public void Reset() {
        for (Room room : this.rooms) {
            room.getReservations().clear();
        }
        InitUrlsToCall();
        UpdateUsersWithExchange();
    }

    /**
     * Gets a live copy to the local memory mapping of Exchange.
     * Any changes made outside the ExchangeCalendar class to this copy will make it invalid
     *
     * @return
     */
    @Override
    public List<Room> getRooms() {
        return rooms;
    }

    private synchronized boolean  UpdateMemoryWithExchange() {
        boolean didSomething = false;
        try {
            String greque = gson.toJson(CreateRequestContainer());
            HttpResponse httprResponse = Unirest.post("https://graph.microsoft.com/v1.0/$batch")
                    .header("accept", "application/json")
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + bearerProvider.getBearer())
                    .body(greque)
                    .asString();

            String result = httprResponse.getBody().toString();
            JSONObject jo = (JSONObject) (new JSONParser()).parse(result);
            JSONArray responses = (JSONArray) jo.get("responses");
            if (responses == null) {
                return false;
            }

            urlsToCall.clear();

            synchronized (this) {
                for (Object response : responses) {

                    JSONObject roomIterator = (JSONObject) response;
                    Room room = Room.SearchRoomById(Integer.parseInt(roomIterator.get("id").toString()), rooms);

                    if (room == null) {
                        continue;
                    }

                    JSONObject body = (JSONObject) roomIterator.get("body");
                    String nextLink = (String) body.get("@odata.nextLink");
                    String deltaLink = (String) body.get("@odata.deltaLink");

                    String nextLinkString = deltaLink != null ? deltaLink : nextLink;
                    urlsToCall.add(nextLinkString.substring(33));

                    JSONArray value = (JSONArray) body.get("value");
                    Iterator valueIterator = value.iterator();

                    boolean roomChanged = valueIterator.hasNext();
                    while (valueIterator.hasNext()) {
                        UpdateReservationFromJson(room, (JSONObject) valueIterator.next());
                    }

                    if (roomChanged) {
                        didSomething = true;
                        room.roomChanged(Changed.CalendarUpdate);
                    }
                }
            }

        } catch (UnirestException | ParseException e) {
            e.printStackTrace();
        }
        return didSomething;
    }

    private RequestContainer CreateRequestContainer() {
        RequestContainer container = new RequestContainer();

        int id = 0;
        while (urlsToCall.size() > 0) {
            String url = urlsToCall.poll();
            String email = url.substring(7);
            email = email.substring(0, email.indexOf("/"));
            RequestModel model = new RequestModel();
            model.id = RoomDataModel.getRoomIdByEmail(email, config.GetRoomData());
            model.url = url;
            System.out.println(model.url);
            container.requests.add(model);
        }
        return container;
    }

    private void UpdateReservationFromJson(Room room, JSONObject next) {
        String calID = (String) next.get("id");
        Reservation reservation = GetOrAddReservationByCalId(room, calID);


        if (next.get("@removed") != null) {
            room.removeReservation(reservation);
            return;
        }

        JSONArray attendees = (JSONArray) next.get("attendees");
        if (attendees != null && attendees.size() > 0) {
            JSONObject emailAddress = (JSONObject) ((JSONObject) attendees.get(0)).get("emailAddress");
            if (emailAddress != null) {
                reservation.setUserEmail(emailAddress.get("address").toString());
                reservation.setTitle(emailAddress.get("name").toString());
            }
        }

        String bodyPreview = (String) next.get("bodyPreview");
        boolean started = bodyPreview != null && bodyPreview.contains("Meeting has started");
        if (bodyPreview != null) {
            reservation.setHasStarted(started);
        }

        if (next.get("iCalUId") != null) {
            reservation.setUuid(next.get("iCalUId").toString());
        }

        if (next.get("start") != null) {
            reservation.setStart(ParseTime((JSONObject) next.get("start")));
        }
        if (next.get("end") != null) {
            reservation.setEnd(ParseTime((JSONObject) next.get("end")));
        }

        reservation.Changed(Changed.CalendarUpdate);
    }

    private Reservation GetOrAddReservationByCalId(Room room, String id) {
        for (Reservation reservation : room.getReservations()) {
            if (reservation.getCalId() != null && reservation.getCalId().equals(id)) {
                return reservation;
            }
        }
        Reservation newR = new Reservation(id);
        newR.setId(reservationId++);
        room.addReservation(newR);

        return newR;

    }

    private LocalDateTime ParseTime(JSONObject time) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

        ZonedDateTime zoned = ZonedDateTime.parse(time.get("dateTime").toString().substring(0, 23) + "Z", formatter);
        return zoned.withZoneSameInstant(ZoneId.of("Europe/Paris")).toLocalDateTime();
    }

    /**
     * Makes a call to the Exchange Server to get all users. Then return those.
     *
     * @return
     */
    @Override
    public List<User> getUsers() {
        return cachedUsers;
    }

    private void UpdateUsersWithExchange() {
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
                    newUser.setEmail((user.get("mail") == null ? null : user.get("mail").toString()));
                    cachedUsers.add(newUser);
                }
            }


        } catch (UnirestException | ParseException e) {
            e.printStackTrace();
        }
    }

    private boolean UserExists(String uid) {
        for (User cachedUser : this.cachedUsers) {
            if (cachedUser.getUid().equals(uid)) {
                return true;
            }
        }
        return false;
    }


    @Override
    public synchronized void updateEvent(Reservation reservation) {
        ReservationModel reservationModel = new ReservationModel();
        String email = UpdateReservationModel(reservation, reservationModel);

        System.out.println("https://graph.microsoft.com/v1.0/users/" + email + "/events/" + reservation.getCalId());

        try {
            String result = Unirest.patch("https://graph.microsoft.com/v1.0/users/" + email + "/events/" + reservation.getCalId())
                    .header("accept", "application/json")
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + bearerProvider.getBearer())
                    .body(gson.toJson(reservationModel))
                    .asJson().getBody().toString();
            System.out.println("Reservation updated: " + result);
            waitAndReload();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }

    private void waitAndReload() {
        try {
            Thread.sleep(4000);
            Reload(true);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private int getRoomIdByReservation(Reservation reservation) {
        return reservation.getRoom() == null ? -1 : reservation.getRoom().getId();
    }

    private class RequestContainer {
        public ArrayList<RequestModel> requests = new ArrayList<>();

        public ArrayList<RequestModel> getRequests() {
            return requests;
        }

        public void setRequests(ArrayList<RequestModel> requests) {
            this.requests = requests;
        }
    }

    private class RequestModel {
        public int id;
        public String method = "GET";
        public String url = "";
    }
}
