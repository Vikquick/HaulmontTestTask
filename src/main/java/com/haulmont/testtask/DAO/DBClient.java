package com.haulmont.testtask.DAO;

import com.haulmont.testtask.Entities.Client;
import java.sql.*;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Виктор on 30.03.2017.
 */
public class DBClient  {

//Добавляем клиента
    static public void addClient (Client client)
{
    try
    {
    PreparedStatement statement = DBConnection.connection.prepareStatement("INSERT INTO CLIENT (SURNAME, NAME, MIDDLENAME, PHONE) VALUES (?,?,?,?)");
    statement.setString(1, client.getSurname());
    statement.setString(2, client.getName());
    statement.setString(3, client.getMiddleName());
    statement.setString(4, client.getPhone());
    statement.executeUpdate();
}
catch (SQLException e)
{
    e.printStackTrace();
}
}
//Удаляем клиента
static public int deleteClient(long ID){

    try {
        PreparedStatement statement = DBConnection.connection.prepareStatement("DELETE FROM CLIENT WHERE ID = ?");
        statement.setLong(1,ID);
        statement.executeUpdate();
        return 0;
    } catch (SQLException e){return 1;}
}

//Выводим список клиентов
static public List<Client> getClientList(){


    try {

        PreparedStatement statement = DBConnection.connection.prepareStatement("SELECT * FROM CLIENT");
        ResultSet resultSet = statement.executeQuery();
        List<Client> clients = new ArrayList<Client>();
        while (resultSet.next()){
            Client client = new Client();
            client.setId(resultSet.getLong(1));
            client.setSurname(resultSet.getString(2));
            client.setName(resultSet.getString(3));
            client.setMiddleName(resultSet.getString(4));
            client.setPhone(resultSet.getString(5));

            clients.add(client);
        }
        return clients;
    } catch (SQLException e) {
        e.printStackTrace();
        List<Client> clients = null;
        return clients;
    }


}

    static public void updateClient (Client client){
        try {
            PreparedStatement statement = DBConnection.connection.prepareStatement("UPDATE CLIENT SET NAME = ?, SURNAME = ?, MIDDLENAME = ?, PHONE = ? WHERE ID = ?");
            statement.setString(1, client.getName());
            statement.setString(2, client.getSurname());
            statement.setString(3, client.getMiddleName());
            statement.setString(4, client.getPhone());
            statement.setLong(5, client.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}