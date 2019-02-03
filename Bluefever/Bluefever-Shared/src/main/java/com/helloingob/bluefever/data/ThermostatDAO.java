package com.helloingob.bluefever.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.helloingob.bluefever.Settings;
import com.helloingob.bluefever.data.to.ThermostatTO;
import com.helloingob.bluefever.util.LogFileWriter;

public class ThermostatDAO {

    public static List<ThermostatTO> get() {
        PreparedStatement statement = null;
        Connection connection = null;
        ResultSet resultSet = null;
        List<ThermostatTO> thermostats = new ArrayList<ThermostatTO>();

        try {
            connection = DatabaseConnection.getInstance().getConnection();
            statement = connection.prepareStatement("SELECT * FROM " + Settings.Database.TABLENAME);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                thermostats.add(getDataFromRow(resultSet));
            }
        } catch (Exception e) {
            LogFileWriter.writeErrorLogLine(e);
        } finally {
            DatabaseConnection.getInstance().close(connection, resultSet, statement);
        }
        return thermostats;
    }
    
    public static ThermostatTO get(int pk) {
        PreparedStatement statement = null;
        Connection connection = null;
        ResultSet resultSet = null;
        ThermostatTO thermostat = null;

        try {
            connection = DatabaseConnection.getInstance().getConnection();
            statement = connection.prepareStatement("SELECT * FROM " + Settings.Database.TABLENAME +" WHERE pk = ?");
            statement.setInt(1, pk);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                thermostat = getDataFromRow(resultSet);
            }
        } catch (Exception e) {
            LogFileWriter.writeErrorLogLine(e);
        } finally {
            DatabaseConnection.getInstance().close(connection, resultSet, statement);
        }
        return thermostat;
    }

    private static ThermostatTO getDataFromRow(ResultSet resultSet) {
        ThermostatTO thermostat = new ThermostatTO();
        try {
            thermostat.setPk(resultSet.getInt("pk"));
            thermostat.setName(resultSet.getString("name"));
            thermostat.setAddress(resultSet.getString("address"));
            thermostat.setPin(resultSet.getInt("pin"));
            thermostat.setFirmware(resultSet.getString("firmware"));
            thermostat.setSoftware(resultSet.getString("software"));
            thermostat.setManufacturer(resultSet.getString("manufacturer"));
            thermostat.setDevicename(resultSet.getString("devicename"));
        } catch (SQLException e) {
            LogFileWriter.writeErrorLogLine(e);
        }
        return thermostat;
    }

    public static boolean update(ThermostatTO thermostat) {
        PreparedStatement statement = null;
        Connection connection = null;
        ResultSet resultSet = null;
        boolean result = false;

        try {
            connection = DatabaseConnection.getInstance().getConnection();
            statement = connection.prepareStatement("UPDATE " + Settings.Database.TABLENAME + " SET name=?, pin=?, firmware=?, software=?, manufacturer=?, devicename=? WHERE address=?");
            statement.setString(1, thermostat.getName());
            statement.setInt(2, thermostat.getPin());
            statement.setString(3, thermostat.getFirmware());
            statement.setString(4, thermostat.getSoftware());
            statement.setString(5, thermostat.getManufacturer());
            statement.setString(6, thermostat.getDevicename());
            statement.setString(7, thermostat.getAddress());

            result = statement.executeUpdate() > 0;
        } catch (Exception e) {
            LogFileWriter.writeErrorLogLine(e);
        } finally {
            DatabaseConnection.getInstance().close(connection, resultSet, statement);
        }
        return result;
    }

    public static boolean deviceExists(ThermostatTO thermostat) {
        PreparedStatement statement = null;
        Connection connection = null;
        ResultSet resultSet = null;
        boolean result = false;

        try {
            connection = DatabaseConnection.getInstance().getConnection();
            statement = connection.prepareStatement("SELECT COUNT(1) FROM " + Settings.Database.TABLENAME + " WHERE address = ?");
            statement.setString(1, thermostat.getAddress());

            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                if (resultSet.getInt(1) > 0) {
                    result = true;
                }
            }
        } catch (Exception e) {
            LogFileWriter.writeErrorLogLine(e);
        } finally {
            DatabaseConnection.getInstance().close(connection, resultSet, statement);
        }
        return result;
    }

    public static boolean anyDeviceExists() {
        PreparedStatement statement = null;
        Connection connection = null;
        ResultSet resultSet = null;
        boolean result = false;

        try {
            connection = DatabaseConnection.getInstance().getConnection();
            statement = connection.prepareStatement("SELECT COUNT(1) FROM " + Settings.Database.TABLENAME);

            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                if (resultSet.getInt(1) > 0) {
                    result = true;
                }
            }
        } catch (Exception e) {
            LogFileWriter.writeErrorLogLine(e);
        } finally {
            DatabaseConnection.getInstance().close(connection, resultSet, statement);
        }
        return result;
    }

    public static boolean add(ThermostatTO thermostat) {
        PreparedStatement statement = null;
        Connection connection = null;
        boolean result = false;

        try {
            connection = DatabaseConnection.getInstance().getConnection();
            statement = connection.prepareStatement("INSERT INTO " + Settings.Database.TABLENAME + " (name, address, pin) VALUES (?,?,?)");
            statement.setString(1, thermostat.getName());
            statement.setString(2, thermostat.getAddress());
            statement.setInt(3, thermostat.getPin());
            result = statement.executeUpdate() > 0;
        } catch (Exception e) {
            LogFileWriter.writeErrorLogLine(e);
        } finally {
            DatabaseConnection.getInstance().close(connection, null, statement);
        }
        return result;
    }

    public static boolean delete(ThermostatTO thermostat) {
        PreparedStatement statement = null;
        Connection connection = null;
        ResultSet resultSet = null;
        boolean result = false;
        try {
            connection = DatabaseConnection.getInstance().getConnection();
            statement = connection.prepareStatement("DELETE FROM " + Settings.Database.TABLENAME + " WHERE pk = ?");
            statement.setInt(1, thermostat.getPk());
            result = statement.executeUpdate() > 0;
        } catch (Exception e) {
            LogFileWriter.writeErrorLogLine(e);
        } finally {
            DatabaseConnection.getInstance().close(connection, resultSet, statement);
        }
        return result;
    }

}
