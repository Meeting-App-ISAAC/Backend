package Reservation;

import java.util.List;

public interface UserCollection {

    List<User> getUsers();

    User getUserById(int id);
}
