package Reservation;

import CalendarResource.Calender;

import java.util.ArrayList;
import java.util.List;

public class UserProfiler implements UserCollection {

    private Calender calender;

    public UserProfiler(Calender calender){
        this.calender = calender;
    }

    @Override
    public List<User> getUsers() {
        return calender.getUsers();
    }

    @Override
    public User getUserById(int id) {
        for (User user : calender.getUsers()){
            if (user.getId() == id){
                return user;
            }
        }
        return null;
    }
}
