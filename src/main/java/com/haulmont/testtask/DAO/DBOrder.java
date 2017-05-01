package com.haulmont.testtask.DAO;

import com.haulmont.testtask.Entities.Client;
import com.haulmont.testtask.Entities.Order;
import com.vaadin.data.util.filter.Or;

import java.sql.*;
import java.sql.Date;
import java.util.*;

/**
 * Created by Виктор on 30.03.2017.
 */
public class DBOrder {



    //Добавляем заказ
    static public void addOrder (Order order)
    {
        try
        {
            PreparedStatement statement = DBConnection.connection.prepareStatement("INSERT INTO SERVICEORDER (DESCRIPTION, CLIENT, DATE_START, DATE_END, COST, STATUS) VALUES (?, ?, ?, ?, ?, ?)");
            statement.setString(1, order.getDescription());
            statement.setString(2, order.getClient().toString());
            statement.setDate(3,  new java.sql.Date(order.getDateOfCreation().getTime()));
            statement.setDate(4,  new java.sql.Date(order.getDateOfCompletionOfWork().getTime()));
            statement.setDouble(5, order.getCost());
            statement.setString(6, order.getStatus());
            statement.executeUpdate();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
    //Удаляем заказ
    static public void deleteOrder(long id){

        try {
            PreparedStatement statement = DBConnection.connection.prepareStatement("DELETE FROM SERVICEORDER WHERE id = ?");
            statement.setLong(1,id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Выводим список заказов
  static public ArrayList<Order> getOrderList(){
        ArrayList orders = new ArrayList();

        try {
            PreparedStatement statement = DBConnection.connection.prepareStatement("SELECT * FROM SERVICEORDER");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()){
                Order order = new Order();
                order.setId(resultSet.getLong(1));
                order.setDescription(resultSet.getString(2));
                order.setClient(resultSet.getLong(3));
                order.setDateOfCreation(resultSet.getDate(4));
                order.setDateOfCompletionOfWork(resultSet.getDate(5));
                order.setCost(resultSet.getDouble(6));
                order.setStatus(resultSet.getString(7));
                orders.add(order);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orders;
    }
//Изменяем заказ
    static public void updateOrder (Order order){
        try {
            PreparedStatement statement = DBConnection.connection.prepareStatement("UPDATE SERVICEORDER SET DESCRIPTION = ?, CLIENT = ?, DATE_START = ?, DATE_END = ?, COST = ?, STATUS = ? WHERE ID = ?");
            statement.setString(1, order.getDescription());
            statement.setString(2, order.getClient().toString());
            statement.setDate(3, new java.sql.Date(order.getDateOfCreation().getTime()));
            statement.setDate(4, new java.sql.Date(order.getDateOfCompletionOfWork().getTime()));
            statement.setDouble(5, order.getCost());
            statement.setString(6, order.getStatus());
            statement.setLong(7, order.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
