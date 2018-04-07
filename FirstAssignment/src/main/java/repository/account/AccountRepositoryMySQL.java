package repository.account;

import model.Account;
import model.builder.AccountBuilder;
import repository.EntityNotFoundException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static database.Constants.Tables.ACCOUNT;
import static database.Constants.Tables.CLIENT;

public class AccountRepositoryMySQL implements AccountRepository {


    private final Connection connection;


    public AccountRepositoryMySQL(Connection connection) {
        this.connection = connection;
    }

    public List<Account> findAll() {
        Statement statement;

        List<Account> accounts=null;
        try {
            statement = connection.createStatement();
            String fetchRoleSql = "Select * from " + ACCOUNT ;
            ResultSet accountResultSet = statement.executeQuery(fetchRoleSql);

            accounts=new ArrayList<Account>();
            while(accountResultSet.next()) {
                Account account = new AccountBuilder()
                        .setId(accountResultSet.getLong("id"))
                        .setCreation(accountResultSet.getDate("creation"))
                        .setBalance(accountResultSet.getInt("balance"))
                        .setType(accountResultSet.getString("type"))
                        .build();
                accounts.add(account);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return accounts;
    }

    @Override
    public Account findById(Long id) throws EntityNotFoundException {
        Statement statement;
        Account account;
        try {
            statement = connection.createStatement();
            String fetchRoleSql = "Select * from " + ACCOUNT + " where `id`=\'" + id + "\'";
            ResultSet accountResultSet = statement.executeQuery(fetchRoleSql);

            if(accountResultSet.next()) {
                account = new AccountBuilder()
                        .setId(accountResultSet.getLong("id"))
                        .setCreation(accountResultSet.getDate("creation"))
                        .setBalance(accountResultSet.getInt("balance"))
                        .setType(accountResultSet.getString("type"))
                        .build();
            }
            else
            {
                throw new EntityNotFoundException(id,Account.class.getSimpleName());
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new EntityNotFoundException(id,Account.class.getSimpleName());
        }
        return account;
    }

    @Override
    public boolean save(Account account,Long clientId) {
        try {
            PreparedStatement insertAccountStatement = connection
                    .prepareStatement("INSERT INTO "+ACCOUNT+" values (null, ?, ?, ?, ?)");
            insertAccountStatement.setString(1, account.getType());
            insertAccountStatement.setInt(2, account.getBalance());
            insertAccountStatement.setLong(3, clientId);
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

        List<Account> accounts=null;
        try {
            statement = connection.createStatement();
            String fetchAccountSql = "Select * from " + ACCOUNT + " where `client_id`=\'" + ownerId + "\'";
            ResultSet accountResultSet = statement.executeQuery(fetchAccountSql);

            accounts=new ArrayList<Account>();
            while(accountResultSet.next()) {
                Account account = new AccountBuilder()
                        .setId(accountResultSet.getLong("id"))
                        .setCreation(accountResultSet.getDate("creation"))
                        .setBalance(accountResultSet.getInt("balance"))
                        .setType(accountResultSet.getString("type"))
                        .build();
                accounts.add(account);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return accounts;
    }

    @Override
    public boolean update(Long id, Account account) {
        try {
            PreparedStatement updateAccountStatement = connection
                    .prepareStatement("UPDATE "+ ACCOUNT+" SET type= ?,balance= ?, creation= ? WHERE id= ?");
            updateAccountStatement.setString(1, account.getType());
            updateAccountStatement.setInt(2, account.getBalance());
            updateAccountStatement.setDate(3,  account.getCreation());
            updateAccountStatement.setLong(4, id);
            updateAccountStatement.executeUpdate();

            account.setId(id);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean remove(Long id) {
        try {
            PreparedStatement removeAccountStatement = connection
                    .prepareStatement("DELETE FROM " + ACCOUNT + " WHERE id= ?");
            removeAccountStatement.setLong(1, id);
            removeAccountStatement.executeUpdate();
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
            String sql = "DELETE from "+ACCOUNT+" where id >= 0";
            statement.executeUpdate(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
