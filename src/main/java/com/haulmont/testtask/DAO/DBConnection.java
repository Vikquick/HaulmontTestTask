package com.haulmont.testtask.DAO;

import java.sql.*;

/**
 * Created by Виктор on 30.03.2017.
 */
public class DBConnection {
    public static Connection connection;

    public static void startConnection() {

        try {
            Class.forName("org.hsqldb.jdbcDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        try {
            connection = DriverManager.getConnection("jdbc:hsqldb:file:src/main/Database/servicedb", "SA", "");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void closeConnection() {
        try {
            PreparedStatement statement = connection.prepareStatement("SHUTDOWN");
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
