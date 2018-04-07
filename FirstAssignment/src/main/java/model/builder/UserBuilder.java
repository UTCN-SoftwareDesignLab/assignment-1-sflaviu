package model.builder;

import model.Role;
import model.User;

import java.util.List;

public class UserBuilder implements Builder<User>{

    private User user;

    public UserBuilder() {
        user=new User();
    }

    public UserBuilder setId(Long id) {
        user.setId(id);
        return this;
    }

    public UserBuilder setRoles(List<Role> roles){
        user.setRoles(roles);
        return this;
    }

    public UserBuilder setUserName(String userName) {
        user.setUserName(userName);
        return this;
    }

    public UserBuilder setPassword(String password) {
        user.setPassword(password);
        return this;
    }

    public User build() { return user; }
}
