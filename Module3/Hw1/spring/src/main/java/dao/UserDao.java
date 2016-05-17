package dao;

import model.User;

import java.util.List;

public interface UserDao {
    User getUserById(long id);

    User getUserByEmail(String email);

    List getUsersByName(String name, int pageSize, int pageNum);

    User createUser(User user);

    User updateUser(User user);

    boolean deleteUser(long userId);
}
