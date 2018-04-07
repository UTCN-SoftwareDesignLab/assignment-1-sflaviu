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
    private final ClientRepository clientRepository;
    private final AccountRepository accountRepository;



    public ActivityRepositoryMySQL(Connection connection,UserRepository userRepository,ClientRepository clientRepository,AccountRepository accountRepository) {
        this.connection = connection;
        this.userRepository=userRepository;
        this.clientRepository=clientRepository;
        this.accountRepository=accountRepository;

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

                Account modifiedAccount=null;
                if(activityResultSet.getLong("acc_id")!=0)
                    modifiedAccount=accountRepository.findById(activityResultSet.getLong("acc_id"));

                Client modifiedClient=null;
                if(activityResultSet.getLong("cl_id")!=0)
                    modifiedClient =clientRepository.findById(activityResultSet.getLong("cl_id"));

                Activity activity = new ActivityBuilder()
                        .setId(activityResultSet.getLong("id"))
                        .setPerformer(userRepository.findById(activityResultSet.getLong("us_id")))
                        .setDate(activityResultSet.getDate("date"))
                        .setType(activityResultSet.getString("type"))
                        .setModifiedAccount(modifiedAccount)
                        .setModifiedClient(modifiedClient)
                        .build();
                activities.add(activity);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        catch (EntityNotFoundException e)
        {
            e.printStackTrace();
            System.out.println("Database integrity may be at fault !");
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
                activity = new ActivityBuilder()
                        .setId(activityResultSet.getLong("id"))
                        .setPerformer(userRepository.findById(activityResultSet.getLong("us_id")))
                        .setDate(activityResultSet.getDate("date"))
                        .setType(activityResultSet.getString("type"))
                        .setModifiedAccount(accountRepository.findById(activityResultSet.getLong("acc_id")))
                        .setModifiedClient(clientRepository.findById(activityResultSet.getLong("cl_id")))
                        .build();
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
                Activity activity = new ActivityBuilder()
                        .setId(activityResultSet.getLong("id"))
                        .setPerformer(userRepository.findById(activityResultSet.getLong("us_id")))
                        .setDate(activityResultSet.getDate("date"))
                        .setType(activityResultSet.getString("type"))
                        .setModifiedAccount(accountRepository.findById(activityResultSet.getLong("acc_id")))
                        .setModifiedClient(clientRepository.findById(activityResultSet.getLong("cl_id")))
                        .build();
                activities.add(activity);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        catch (EntityNotFoundException e)
        {
            e.printStackTrace();
            System.out.println("Database integrity may be at fault !");
        }

        return activities;
    }

    @Override
    public boolean save(Activity activity) {

        Long modifiedClientId=null;
        if(activity.getModifiedClient()!=null)
            modifiedClientId=activity.getModifiedClient().getId();

        Long modifiedAccountId=null;
        if(activity.getModifiedAccount()!=null)
            modifiedAccountId=activity.getModifiedAccount().getId();

        try {
            PreparedStatement insertActivityStatement = connection
                    .prepareStatement("INSERT INTO "+ACTIVITY+" values (null, ?, ?, ?, ?, ?)");
            insertActivityStatement.setString(1, activity.getType());
            insertActivityStatement.setLong(2, activity.getPerformer().getId());
            insertActivityStatement.setDate(3, activity.getDate());
            insertActivityStatement.setObject(4, modifiedClientId);
            insertActivityStatement.setObject(5, modifiedAccountId);
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
