package repository.activity;

import model.Activity;
import model.builder.ActivityBuilder;
import repository.client.ClientAccountRepository;
import repository.activity.ActivityRepository;
import repository.user.UserRepository;

import java.sql.*;
import java.util.List;

import static database.Constants.Tables.ACTIVITY;

public class ActivityRepositoryMySQL implements ActivityRepository {

    private final Connection connection;
    private final UserRepository userRepository;
    private final ClientAccountRepository clientAccountRepository;



    public ActivityRepositoryMySQL(Connection connection,UserRepository userRepository,ClientAccountRepository clientAccountRepository) {
        this.connection = connection;
        this.userRepository=userRepository;
        this.clientAccountRepository=clientAccountRepository;

    }

    @Override
    public Activity findById(Long id) {
        Statement statement;
        try {
            statement = connection.createStatement();
            String fetchActivitySql = "Select * from " + ACTIVITY + " where `id`=\'" + id + "\'";
            ResultSet activityResultSet = statement.executeQuery(fetchActivitySql);

            Activity activity=null;
            if(activityResultSet.next()) {
                activity = new ActivityBuilder()
                        .setId(activityResultSet.getLong("id"))
                        .setPerformer(userRepository.findById(activityResultSet.getLong("us_id")))
                        .setDate(activityResultSet.getDate("date"))
                        .setType(activityResultSet.getString("type"))
                        .setModifiedAccount(clientAccountRepository.findAccountById(activityResultSet.getLong("acc_id")))
                        .setModifiedClient(clientAccountRepository.findClientById(activityResultSet.getLong("cl_id")))
                        .build();
            }
            return activity;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
    @Override
    public List<Activity> findByPerformerId(int userId) {
        Statement statement;
        try {
            statement = connection.createStatement();
            String fetchActivitySql = "Select * from " + ACTIVITY + " where `cl_id`=\'" + userId + "\'";
            ResultSet activityResultSet = statement.executeQuery(fetchActivitySql);

            List<Activity> activities=null;
            while(activityResultSet.next()) {
                Activity activity = new ActivityBuilder()
                        .setId(activityResultSet.getLong("id"))
                        .setPerformer(userRepository.findById(activityResultSet.getLong("us_id")))
                        .setDate(activityResultSet.getDate("date"))
                        .setType(activityResultSet.getString("type"))
                        .setModifiedAccount(clientAccountRepository.findAccountById(activityResultSet.getLong("acc_id")))
                        .setModifiedClient(clientAccountRepository.findClientById(activityResultSet.getLong("cl_id")))
                        .build();
                activities.add(activity);
            }
            return activities;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean save(Activity activity) {
        try {
            PreparedStatement insertActivityStatement = connection
                    .prepareStatement("INSERT INTO "+ACTIVITY+" values (null, ?, ?, ?, ?, ?)");
            insertActivityStatement.setString(1, activity.getType());
            insertActivityStatement.setLong(2, activity.getPerformer().getId());
            insertActivityStatement.setDate(3, activity.getDate());
            insertActivityStatement.setLong(4, activity.getModifiedClient().getId());
            insertActivityStatement.setLong(5, activity.getModifiedAccount().getId());
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

}
