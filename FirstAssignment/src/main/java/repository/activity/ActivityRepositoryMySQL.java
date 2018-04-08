package repository.activity;

import model.Account;
import model.Activity;
import model.Client;
import model.builder.ActivityBuilder;
import repository.EntityNotFoundException;
import repository.account.AccountRepository;
import repository.client.ClientRepository;
import repository.user.UserRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static database.Constants.Tables.ACTIVITY;

public class ActivityRepositoryMySQL implements ActivityRepository {

    private final Connection connection;
    private final UserRepository userRepository;

    public ActivityRepositoryMySQL(Connection connection,UserRepository userRepository) {
        this.connection = connection;
        this.userRepository=userRepository;

    }

    @Override
    public List<Activity> findAll()
    {
        List<Activity> activities=null;

        try {
            Statement statement = connection.createStatement();
            String fetchClientSql = "Select * from " + ACTIVITY;
            ResultSet activityResultSet = statement.executeQuery(fetchClientSql);

            activities=new ArrayList<>();
            while (activityResultSet.next()) {
                Activity activity = createActivity(activityResultSet);
                activities.add(activity);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return activities;
    }

    @Override
    public Activity findById(Long id) throws EntityNotFoundException {
        Statement statement;
        Activity activity;

        try {
            statement = connection.createStatement();
            String fetchActivitySql = "Select * from " + ACTIVITY + " where `id`=\'" + id + "\'";
            ResultSet activityResultSet = statement.executeQuery(fetchActivitySql);

            if(activityResultSet.next()) {
                activity = createActivity(activityResultSet);
            }
            else
            {
                throw new EntityNotFoundException(id, Activity.class.getSimpleName());
            }


        } catch (SQLException e) {
            e.printStackTrace();
            throw new EntityNotFoundException(id, Activity.class.getSimpleName());
        }

        return activity;
    }
    @Override
    public List<Activity> findByPerformerId(Long userId) {
        Statement statement;
        List<Activity> activities=null;

        try {
            statement = connection.createStatement();
            String fetchActivitySql = "Select * from " + ACTIVITY + " where `cl_id`=\'" + userId + "\'";
            ResultSet activityResultSet = statement.executeQuery(fetchActivitySql);

            activities=new ArrayList<>();
            while(activityResultSet.next()) {
                Activity activity = createActivity(activityResultSet);
                activities.add(activity);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return activities;
    }

    @Override
    public boolean save(Activity activity) {

        try {
            PreparedStatement insertActivityStatement = connection
                    .prepareStatement("INSERT INTO "+ACTIVITY+" values (null, ?, ?, ?, ?, ?)");
            insertActivityStatement.setString(1, activity.getType());
            insertActivityStatement.setLong(2, activity.getModifiedClientId());
            insertActivityStatement.setDate(3, activity.getDate());
            insertActivityStatement.setObject(4, activity.getModifiedClientId());
            insertActivityStatement.setObject(5, activity.getModifiedAccountId());
            insertActivityStatement.executeUpdate();

            ResultSet rs = insertActivityStatement.getGeneratedKeys();
            rs.next();
            long activityId = rs.getLong(1);
            activity.setId(activityId);

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public void removeAll() {
        try {
            Statement statement = connection.createStatement();
            String sql = "DELETE from " + ACTIVITY + " where id >= 0";
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private Activity createActivity(ResultSet activityResultSet) throws SQLException
    {
        Activity activity= new ActivityBuilder()
                .setId(activityResultSet.getLong("id"))
                .setDate(activityResultSet.getDate("date"))
                .setType(activityResultSet.getString("type"))
                .setPerformer(activityResultSet.getLong("us_id"))
                .setModifiedAccountId(activityResultSet.getLong("acc_id"))
                .setModifiedClientId(activityResultSet.getLong("cl_id"))
                .build();
        return activity;
    }

}
