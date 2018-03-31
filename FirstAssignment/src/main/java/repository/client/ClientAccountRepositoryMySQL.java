package repository.client;

import model.Account;
import model.Client;
import model.builder.AccountBuilder;
import model.builder.ClientBuilder;
import model.validation.Notification;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static database.Constants.Tables.ACCOUNT;
import static database.Constants.Tables.CLIENT;

public class ClientAccountRepositoryMySQL implements ClientAccountRepository {

    private final Connection connection;


    public ClientAccountRepositoryMySQL(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Client findClientById(Long id) {
        Statement statement;
        try {
            statement = connection.createStatement();
            String fetchClientSql = "Select * from " + CLIENT + " where `id`=\'" + id + "\'";
            ResultSet clientResultSet = statement.executeQuery(fetchClientSql);

            Client client=null;
            if(clientResultSet.next()) {
                client = new ClientBuilder()
                        .setId(clientResultSet.getLong("id"))
                        .setName(clientResultSet.getString("name"))
                        .setAddress(clientResultSet.getString("address"))
                        .setCardNr(clientResultSet.getString("card_nr"))
                        .setCnp(clientResultSet.getString("cnp"))
                        .build();
            }
            return client;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Account findAccountById(Long id) {
        Statement statement;
        try {
            statement = connection.createStatement();
            String fetchRoleSql = "Select * from " + ACCOUNT + " where `id`=\'" + id + "\'";
            ResultSet accountResultSet = statement.executeQuery(fetchRoleSql);

            Account account=null;
            if(accountResultSet.next()) {
                account = new AccountBuilder()
                        .setId(accountResultSet.getLong("id"))
                        .setOwner(findClientById(accountResultSet.getLong("client_id")))
                        .setCreation(accountResultSet.getDate("creation"))
                        .setBalance(accountResultSet.getInt("balance"))
                        .setType(accountResultSet.getString("type"))
                        .build();
            }
            return account;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    @Override
    public List<Client> findAllClients() {
        try {
            Statement statement = connection.createStatement();
            String fetchClientSql = "Select * from " + CLIENT;
            ResultSet clientResultSet = statement.executeQuery(fetchClientSql);

            List<Client> clients=new ArrayList<>();
            while (clientResultSet.next()) {
                Client client = new ClientBuilder()
                        .setId(clientResultSet.getLong("id"))
                        .setName(clientResultSet.getString("name"))
                        .setAddress(clientResultSet.getString("address"))
                        .setCardNr(clientResultSet.getString("card_nr"))
                        .setCnp(clientResultSet.getString("cnp"))
                        .setAccounts(findAccountsByOwnerId(clientResultSet.getLong("id")))
                        .build();
                clients.add(client);
            }
            return clients;


        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Account> findAllAccounts() {
        Statement statement;
        try {
            statement = connection.createStatement();
            String fetchRoleSql = "Select * from " + ACCOUNT ;
            ResultSet accountResultSet = statement.executeQuery(fetchRoleSql);

            List<Account> accounts=new ArrayList<Account>();
            while(accountResultSet.next()) {
                Account account = new AccountBuilder()
                        .setId(accountResultSet.getLong("id"))
                        .setOwner(findClientById(accountResultSet.getLong("client_id")))
                        .setCreation(accountResultSet.getDate("creation"))
                        .setBalance(accountResultSet.getInt("balance"))
                        .setType(accountResultSet.getString("type"))
                        .build();
                accounts.add(account);
            }
            return accounts;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Notification<Client> findClientByCnp(String cnp) {
        Statement statement;
        try {
            statement = connection.createStatement();
            String fetchRoleSql = "Select * from " + CLIENT + " where `cnp`=\'" + cnp + "\'";
            ResultSet clientResultSet = statement.executeQuery(fetchRoleSql);

            Notification<Client> findByCnpNotification=new Notification<>();
            if(clientResultSet.next()) {
                findByCnpNotification.setResult(new ClientBuilder()
                        .setId(clientResultSet.getLong("id"))
                        .setName(clientResultSet.getString("name"))
                        .setAddress(clientResultSet.getString("address"))
                        .setCardNr(clientResultSet.getString("card_nr"))
                        .setCnp(clientResultSet.getString("cnp"))
                        .setAccounts(findAccountsByOwnerId(clientResultSet.getLong("id")))
                        .build());
                return findByCnpNotification;
            }
            else {
                findByCnpNotification.addError("No client found with this cnp!");
                return findByCnpNotification;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    @Override
    public boolean saveClient(Client client) {
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

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public boolean saveAccount(Account account) {
        try {
            PreparedStatement insertAccountStatement = connection
                    .prepareStatement("INSERT INTO "+ACCOUNT+" values (null, ?, ?, ?, ?)");
            insertAccountStatement.setString(1, account.getType());
            insertAccountStatement.setInt(2, account.getBalance());
            insertAccountStatement.setLong(3, account.getOwner().getId());
            insertAccountStatement.setDate(4, account.getCreation());
            insertAccountStatement.executeUpdate();

            ResultSet rs = insertAccountStatement.getGeneratedKeys();
            rs.next();
            long accountId = rs.getLong(1);
            account.setId(accountId);

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public List<Account> findAccountsByOwnerId(Long ownerId)
    {
        Statement statement;
        try {
            statement = connection.createStatement();
            String fetchAccountSql = "Select * from " + ACCOUNT + " where `client_id`=\'" + ownerId + "\'";
            ResultSet accountResultSet = statement.executeQuery(fetchAccountSql);

            List<Account> accounts=new ArrayList<Account>();
            while(accountResultSet.next()) {
                Account account = new AccountBuilder()
                        .setId(accountResultSet.getLong("id"))
                        .setOwner(findClientById(accountResultSet.getLong("client_id")))
                        .setCreation(accountResultSet.getDate("creation"))
                        .setBalance(accountResultSet.getInt("balance"))
                        .setType(accountResultSet.getString("type"))
                        .build();
                accounts.add(account);
            }
            return accounts;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void removeAll() {
        try {
            Statement statement = connection.createStatement();
            String sql = "DELETE from " + CLIENT +"where id >= 0";
            statement.executeUpdate(sql);

            sql = "DELETE from "+ACCOUNT+" where id >= 0";
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
