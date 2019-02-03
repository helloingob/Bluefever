package com.helloingob.bluefever.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import com.helloingob.bluefever.Settings;
import com.helloingob.bluefever.util.LogFileWriter;

public class DatabaseConnection {

    private static DatabaseConnection instance = null;

    static {
        try {
            instance = new DatabaseConnection();
        } catch (Exception e) {
            LogFileWriter.writeErrorLogLine(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public static DatabaseConnection getInstance() {
        return instance;
    }

    public Connection getConnection() {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            return DriverManager.getConnection(Settings.Database.CONNECTION_STRING);
        } catch (Exception e) {
            LogFileWriter.writeErrorLogLine(e.getClass().getName() + ": " + e.getMessage());
        }
        return null;
    }

    public void close(Connection connection, ResultSet resultSet, Statement statement) {
        try {
            if (statement != null) {
                statement.close();
            }

            if (resultSet != null) {
                resultSet.close();
            }

            if (connection != null) {
                connection.close();
            }
        } catch (Exception e) {
            LogFileWriter.writeErrorLogLine(e.getClass().getName() + ": " + e.getMessage());
        }
    }

}
