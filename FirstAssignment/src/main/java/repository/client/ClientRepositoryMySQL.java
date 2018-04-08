package repository.client;

import model.Client;
import model.builder.ClientBuilder;
import model.validation.Notification;
import repository.EntityNotFoundException;
import repository.account.AccountRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static database.Constants.Tables.CLIENT;

public class ClientRepositoryMySQL implements ClientRepository {

    private final Connection connection;


    public ClientRepositoryMySQL(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Client findById(Long id) throws EntityNotFoundException {
        Statement statement;

        Client client;
        try {
            statement = connection.createStatement();
            String fetchClientSql = "Select * from " + CLIENT + " where `id`=\'" + id + "\'";
            ResultSet clientResultSet = statement.executeQuery(fetchClientSql);

            if(clientResultSet.next()) {
                client =createClient(clientResultSet);
            }
            else
            {
                throw new EntityNotFoundException(id,Client.class.getSimpleName());
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new EntityNotFoundException(id,Client.class.getSimpleName());
        }
        return client;
    }



    @Override
    public List<Client> findAll() {
        List<Client> clients=new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            String fetchClientSql = "Select * from " + CLIENT;
            ResultSet clientResultSet = statement.executeQuery(fetchClientSql);

            clients=new ArrayList<>();
            while (clientResultSet.next()) {
                Client client = createClient(clientResultSet);
                clients.add(client);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clients;
    }



    @Override
    public Client findByCnp(String cnp) throws EntityNotFoundException{
        Statement statement;

        Client client;
        try {
            statement = connection.createStatement();
            String fetchRoleSql = "Select * from " + CLIENT + " where `cnp`=\'" + cnp + "\'";
            ResultSet clientResultSet = statement.executeQuery(fetchRoleSql);

            if(clientResultSet.next()) {
                client=createClient(clientResultSet);
                return client;
            }
            else {
                throw new EntityNotFoundException(Long.parseLong(cnp),Client.class.getSimpleName());
            }

        } catch (SQLException e) {
            throw new EntityNotFoundException(Long.parseLong(cnp),Client.class.getSimpleName());

        }
    }


    @Override
    public Notification<Client> save(Client client) {
        Notification<Client> notificationSaveClient=new Notification<>();
        try {
            PreparedStatement insertClientStatement = connection
                    .prepareStatement("INSERT INTO client values (null, ?, ?, ?, ?)");
            insertClientStatement.setString(1, client.getName());
            insertClientStatement.setString(2, client.getAddress());
            insertClientStatement.setString(3,  client.getCardNr());
            insertClientStatement.setString(4, client.getCnp());
            insertClientStatement.executeUpdate();

            ResultSet rs = insertClientStatement.getGeneratedKeys();
            rs.next();
            long clientId = rs.getLong(1);
            client.setId(clientId);

            notificationSaveClient.setResult(client);
            return notificationSaveClient;
        } catch (SQLException e) {
            e.printStackTrace();
            notificationSaveClient.addError("Saving account not succesfull");
            return notificationSaveClient;
        }

    }

    @Override
    public boolean update(Long id,Client client)
    {
        try {
            PreparedStatement updateClientStatement = connection
                    .prepareStatement("UPDATE "+ CLIENT+" SET name= ?,address= ?, card_nr= ?, cnp= ? WHERE id= ?");
            updateClientStatement.setString(1, client.getName());
            updateClientStatement.setString(2, client.getAddress());
            updateClientStatement.setString(3,  client.getCardNr());
            updateClientStatement.setString(4, client.getCnp());
            updateClientStatement.setLong(5, id);
            updateClientStatement.executeUpdate();

            client.setId(id);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public boolean remove(Long id)
    {
        try {
            PreparedStatement removeClientStatement = connection
                    .prepareStatement("DELETE FROM " + CLIENT + " WHERE id= ?");
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
            String sql = "DELETE from " + CLIENT;
            statement.executeUpdate(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Client createClient(ResultSet clientResultSet) throws SQLException
    {
        Client client = new ClientBuilder()
                .setId(clientResultSet.getLong("id"))
                .setName(clientResultSet.getString("name"))
                .setAddress(clientResultSet.getString("address"))
                .setCardNr(clientResultSet.getString("card_nr"))
                .setCnp(clientResultSet.getString("cnp"))
                .build();
        return client;
    }

}
