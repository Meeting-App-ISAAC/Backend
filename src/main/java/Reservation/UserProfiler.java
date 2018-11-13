package Reservation;

import CalendarResource.Calender;

import java.util.ArrayList;
import java.util.List;

public class UserProfiler implements UserCollection {

    private Calender calender;
    private List<User> users = new ArrayList<>();

    public UserProfiler(Calender calender){
        this.calender = calender;
        users = calender.getUsers();
    }

    @Override
    public List<User> getUsers() {
        return users;
    }

    @Override
    public User getUserById(int id) {
        for (User user : users){
            if (user.getId() == id){
                return user;
            }
        }
        return null;
    }
}
