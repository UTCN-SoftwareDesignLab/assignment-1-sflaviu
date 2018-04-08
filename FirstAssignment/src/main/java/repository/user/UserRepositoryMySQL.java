package repository.user;

import model.User;
import model.builder.UserBuilder;
import model.validation.Notification;
import repository.EntityNotFoundException;
import repository.security.RightsRolesRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import static database.Constants.Tables.USER;

/**
 * Created by Alex on 11/03/2017.
 */
public class UserRepositoryMySQL implements UserRepository {

    private final Connection connection;
    private final RightsRolesRepository rightsRolesRepository;


    public UserRepositoryMySQL(Connection connection, RightsRolesRepository rightsRolesRepository) {
        this.connection = connection;
        this.rightsRolesRepository = rightsRolesRepository;
    }

    public User findById(Long id) throws EntityNotFoundException {
        Statement statement;
        User user=null;

        try {
            statement = connection.createStatement();
            String fetchRoleSql = "Select * from " + USER +  " where `id`=\'"  + id + "\'";
            ResultSet userResultSet = statement.executeQuery(fetchRoleSql);

            if (userResultSet.next()) {
                user = createUser(userResultSet);
            }
            else
                throw new EntityNotFoundException(id,User.class.getSimpleName());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }
    @Override
    public List<User> findAll() {
        List<User> users=null;
        try {
            Statement statement = connection.createStatement();
            String fetchUserSql = "Select * from " + USER;
            ResultSet userResultSet = statement.executeQuery(fetchUserSql);

            users=new ArrayList<>();
            while (userResultSet.next()) {
                User user = createUser(userResultSet);
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public Notification<User> findByUsernameAndPassword(String username, String password) throws AuthenticationException {
        Notification<User> findByUsernameAndPasswordNotification = new Notification<>();
        try {
            Statement statement = connection.createStatement();
            String fetchUserSql = "Select * from `" + USER + "` where `username`=\'" + username + "\' and `password`=\'" + password + "\'";
            ResultSet userResultSet = statement.executeQuery(fetchUserSql);
            if (userResultSet.next()) {
                User user =  createUser(userResultSet);
                findByUsernameAndPasswordNotification.setResult(user);
                return findByUsernameAndPasswordNotification;
            } else {
                findByUsernameAndPasswordNotification.addError("Invalid email or password!");
                return findByUsernameAndPasswordNotification;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new AuthenticationException();
        }
    }

    @Override
    public boolean save(User user) {
        try {
            PreparedStatement insertUserStatement = connection
                    .prepareStatement("INSERT INTO " +USER+" values (null, ?, ?)");
            insertUserStatement.setString(1, user.getUserName());
            insertUserStatement.setString(2, user.getPassword());
            insertUserStatement.executeUpdate();

            ResultSet rs = insertUserStatement.getGeneratedKeys();
            rs.next();
            long userId = rs.getLong(1);
            user.setId(userId);

            rightsRolesRepository.addRolesToUser(user, user.getRoles());

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public boolean update(Long id, User user) {
        try {
            PreparedStatement updateClientStatement = connection
                    .prepareStatement("UPDATE "+ USER+" SET username= ?,password= ? WHERE id= ?");
            updateClientStatement.setString(1, user.getUserName());
            updateClientStatement.setString(2, user.getPassword());
            updateClientStatement.setLong(3, id);
            updateClientStatement.executeUpdate();

            user.setId(id);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean remove(Long id) {
        try {
            PreparedStatement removeClientStatement = connection
                    .prepareStatement("DELETE FROM " + USER + " WHERE id= ?");
            removeClientStatement.setLong(1, id);
            removeClientStatement.executeUpdate();
            return true;
        }catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void removeAll() {
        try {
            Statement statement = connection.createStatement();
            String sql = "DELETE from user where id >= 0";
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private User createUser(ResultSet userResultSet) throws SQLException
    {
        User user= new UserBuilder()
                .setId(userResultSet.getLong("id"))
                .setUserName(userResultSet.getString("username"))
                .setPassword(userResultSet.getString("password"))
                .setRoles(rightsRolesRepository.findRolesForUser(userResultSet.getLong("id")))
                .build();
        return user;
    }


}
