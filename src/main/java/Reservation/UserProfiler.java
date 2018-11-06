package Reservation;

import java.util.ArrayList;
import java.util.List;

public class UserProfiler implements UserCollection {

    private List<User> users = new ArrayList<>();

    public UserProfiler(List<User> users){
        this.users = users;
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
