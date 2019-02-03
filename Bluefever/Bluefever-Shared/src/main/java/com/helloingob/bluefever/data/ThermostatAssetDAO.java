package com.helloingob.bluefever.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.helloingob.bluefever.Settings;
import com.helloingob.bluefever.data.to.ThermostatAssetTO;
import com.helloingob.bluefever.util.LogFileWriter;

public class ThermostatAssetDAO {

    public static boolean persist(ThermostatAssetTO thermostatAsset) {
        PreparedStatement statement = null;
        Connection connection = null;
        boolean result = false;

        try {
            connection = DatabaseConnection.getInstance().getConnection();
            statement = connection.prepareStatement("INSERT INTO " + Settings.Database.TABLENAME_ASSET + " (thermostat_pk, date, currentTemperature, manualTemperature, highTemperature, lowTemperature, offsetTemperature) VALUES (?,?,?,?,?,?,?)");
            statement.setInt(1, thermostatAsset.getThermostatPk());
            statement.setTimestamp(2, thermostatAsset.getDate());

            if (thermostatAsset.getCurrentTemperature() == null) {
                statement.setNull(3, java.sql.Types.DOUBLE);
            } else {
                statement.setDouble(3, thermostatAsset.getCurrentTemperature());
            }

            if (thermostatAsset.getManualTemperature() == null) {
                statement.setNull(4, java.sql.Types.DOUBLE);
            } else {
                statement.setDouble(4, thermostatAsset.getManualTemperature());
            }

            if (thermostatAsset.getHighTemperature() == null) {
                statement.setNull(5, java.sql.Types.DOUBLE);
            } else {
                statement.setDouble(5, thermostatAsset.getHighTemperature());
            }

            if (thermostatAsset.getLowTemperature() == null) {
                statement.setNull(6, java.sql.Types.DOUBLE);
            } else {
                statement.setDouble(6, thermostatAsset.getLowTemperature());
            }

            if (thermostatAsset.getOffsetTemperature() == null) {
                statement.setNull(7, java.sql.Types.DOUBLE);
            } else {
                statement.setDouble(7, thermostatAsset.getOffsetTemperature());
            }

            result = statement.executeUpdate() > 0;
        } catch (Exception e) {
            LogFileWriter.writeErrorLogLine(e);
        } finally {
            DatabaseConnection.getInstance().close(connection, null, statement);
        }
        return result;
    }

    public static ThermostatAssetTO getLastAssets(int pk) {
        PreparedStatement statement = null;
        Connection connection = null;
        ResultSet resultSet = null;

        try {
            connection = DatabaseConnection.getInstance().getConnection();
            statement = connection.prepareStatement("SELECT * FROM " + Settings.Database.TABLENAME_ASSET + " WHERE thermostat_pk=? ORDER BY date DESC LIMIT 1");
            statement.setInt(1, pk);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                return getDataFromRow(resultSet);
            }
        } catch (Exception e) {
            LogFileWriter.writeErrorLogLine(e);
        } finally {
            DatabaseConnection.getInstance().close(connection, resultSet, statement);
        }
        return null;
    }

    public static List<ThermostatAssetTO> getAssets(int pk) {
        PreparedStatement statement = null;
        Connection connection = null;
        ResultSet resultSet = null;
        List<ThermostatAssetTO> thermostatAssets = new ArrayList<ThermostatAssetTO>();

        try {
            connection = DatabaseConnection.getInstance().getConnection();
            statement = connection.prepareStatement("SELECT * FROM " + Settings.Database.TABLENAME_ASSET + " WHERE thermostat_pk=? ORDER BY date DESC");
            statement.setInt(1, pk);

            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                thermostatAssets.add(getDataFromRow(resultSet));
            }
        } catch (Exception e) {
            LogFileWriter.writeErrorLogLine(e);
        } finally {
            DatabaseConnection.getInstance().close(connection, resultSet, statement);
        }
        return thermostatAssets;
    }

    private static ThermostatAssetTO getDataFromRow(ResultSet resultSet) {
        ThermostatAssetTO thermostatAsset = new ThermostatAssetTO();
        try {
            thermostatAsset.setPk(resultSet.getInt("pk"));
            thermostatAsset.setThermostatPk(resultSet.getInt("thermostat_pk"));
            thermostatAsset.setDate(resultSet.getTimestamp("date"));
            thermostatAsset.setCurrentTemperature(resultSet.getDouble("currentTemperature"));
            thermostatAsset.setManualTemperature(resultSet.getDouble("manualTemperature"));
            thermostatAsset.setHighTemperature(resultSet.getDouble("highTemperature"));
            thermostatAsset.setLowTemperature(resultSet.getDouble("lowTemperature"));
            thermostatAsset.setOffsetTemperature(resultSet.getDouble("offsetTemperature"));
        } catch (SQLException e) {
            LogFileWriter.writeErrorLogLine(e);
        }
        return thermostatAsset;
    }

}
