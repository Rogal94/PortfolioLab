package pl.coderslab.charity.user;

public interface UserService {
    User findByEmail(String name);
    void saveUser(User user);
    void editUser(User user);
}
