package com.helloingob.bluefever.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.helloingob.bluefever.Settings;
import com.helloingob.bluefever.data.to.ThermostatJobTO;
import com.helloingob.bluefever.util.LogFileWriter;

public class ThermostatJobDAO {

    public static boolean add(ThermostatJobTO thermostatJob) {
        PreparedStatement statement = null;
        Connection connection = null;
        boolean result = false;

        try {
            connection = DatabaseConnection.getInstance().getConnection();
            statement = connection.prepareStatement("INSERT INTO " + Settings.Database.TABLENAME_JOBS + " (active, thermostat_pk, temperature, day_of_week, time, date) VALUES (?,?,?,?,?,?)");
            fillStatement(statement, thermostatJob);
            result = statement.executeUpdate() > 0;
        } catch (Exception e) {
            LogFileWriter.writeErrorLogLine(e);
        } finally {
            DatabaseConnection.getInstance().close(connection, null, statement);
        }
        return result;
    }

    public static boolean update(ThermostatJobTO thermostatJob) {
        PreparedStatement statement = null;
        Connection connection = null;
        ResultSet resultSet = null;
        boolean result = false;

        try {
            connection = DatabaseConnection.getInstance().getConnection();
            statement = connection.prepareStatement("UPDATE " + Settings.Database.TABLENAME_JOBS + " SET active=?, thermostat_pk=?, temperature=?, day_of_week=?, time=?, date=? WHERE pk=?");
            fillStatement(statement, thermostatJob);
            statement.setInt(7, thermostatJob.getPk());
            result = statement.executeUpdate() > 0;
        } catch (Exception e) {
            LogFileWriter.writeErrorLogLine(e);
        } finally {
            DatabaseConnection.getInstance().close(connection, resultSet, statement);
        }
        return result;
    }

    public static List<ThermostatJobTO> get() {
        PreparedStatement statement = null;
        Connection connection = null;
        ResultSet resultSet = null;
        List<ThermostatJobTO> thermostatJobs = new ArrayList<ThermostatJobTO>();

        try {
            connection = DatabaseConnection.getInstance().getConnection();
            statement = connection.prepareStatement("SELECT * FROM " + Settings.Database.TABLENAME_JOBS + " JOIN " + Settings.Database.TABLENAME + " ON " + Settings.Database.TABLENAME_JOBS + ".thermostat_pk = " + Settings.Database.TABLENAME + ".pk");
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                thermostatJobs.add(getDataFromRow(resultSet));
            }
        } catch (Exception e) {
            LogFileWriter.writeErrorLogLine(e);
        } finally {
            DatabaseConnection.getInstance().close(connection, resultSet, statement);
        }
        return thermostatJobs;
    }

    public static List<ThermostatJobTO> get(int pk) {
        PreparedStatement statement = null;
        Connection connection = null;
        ResultSet resultSet = null;
        List<ThermostatJobTO> thermostatJobs = new ArrayList<ThermostatJobTO>();

        try {
            connection = DatabaseConnection.getInstance().getConnection();
            statement = connection.prepareStatement("SELECT * FROM " + Settings.Database.TABLENAME_JOBS + " JOIN " + Settings.Database.TABLENAME + " ON " + Settings.Database.TABLENAME_JOBS + ".thermostat_pk = " + Settings.Database.TABLENAME + ".pk WHERE " + Settings.Database.TABLENAME + ".pk = ?");
            statement.setInt(1, pk);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                thermostatJobs.add(getDataFromRow(resultSet));
            }
        } catch (Exception e) {
            LogFileWriter.writeErrorLogLine(e);
        } finally {
            DatabaseConnection.getInstance().close(connection, resultSet, statement);
        }
        return thermostatJobs;
    }

    public static List<ThermostatJobTO> get(LocalDateTime localDateTime) {
        PreparedStatement statement = null;
        Connection connection = null;
        ResultSet resultSet = null;
        List<ThermostatJobTO> thermostatJobs = new ArrayList<ThermostatJobTO>();
        DateTimeFormatter dateTimeFormatterTime = DateTimeFormatter.ofPattern("HH:mm:ss");
        try {
            connection = DatabaseConnection.getInstance().getConnection();
            statement = connection.prepareStatement("SELECT * FROM " + Settings.Database.TABLENAME_JOBS + " JOIN " + Settings.Database.TABLENAME + " ON " + Settings.Database.TABLENAME_JOBS + ".thermostat_pk = " + Settings.Database.TABLENAME + ".pk WHERE (date BETWEEN ? AND ?) OR (day_of_week = ? AND (time BETWEEN ? AND ?)) AND active=true");
            statement.setTimestamp(1, Timestamp.valueOf(localDateTime.withSecond(0).withNano(0)));
            statement.setTimestamp(2, Timestamp.valueOf(localDateTime.withSecond(59).withNano(0)));
            statement.setInt(3, localDateTime.getDayOfWeek().getValue());
            statement.setTime(4, Time.valueOf(String.format(dateTimeFormatterTime.format(localDateTime.withSecond(0)))));
            statement.setTime(5, Time.valueOf(String.format(dateTimeFormatterTime.format(localDateTime.withSecond(59)))));
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                thermostatJobs.add(getDataFromRow(resultSet));
            }
        } catch (Exception e) {
            LogFileWriter.writeErrorLogLine(e);
        } finally {
            DatabaseConnection.getInstance().close(connection, resultSet, statement);
        }
        return thermostatJobs;
    }

    public static boolean delete(ThermostatJobTO thermostatJob) {
        PreparedStatement statement = null;
        Connection connection = null;
        ResultSet resultSet = null;
        boolean result = false;
        try {
            connection = DatabaseConnection.getInstance().getConnection();
            statement = connection.prepareStatement("DELETE FROM " + Settings.Database.TABLENAME_JOBS + " WHERE pk = ?");
            statement.setInt(1, thermostatJob.getPk());
            result = statement.executeUpdate() > 0;
        } catch (Exception e) {
            LogFileWriter.writeErrorLogLine(e);
        } finally {
            DatabaseConnection.getInstance().close(connection, resultSet, statement);
        }
        return result;
    }
    
    public static boolean deleteExpiredJobs() {
        PreparedStatement statement = null;
        Connection connection = null;
        ResultSet resultSet = null;
        boolean result = false;
        try {
            connection = DatabaseConnection.getInstance().getConnection();
            statement = connection.prepareStatement("DELETE FROM " + Settings.Database.TABLENAME_JOBS + " WHERE date < now()");
            result = statement.executeUpdate() > 0;
        } catch (Exception e) {
            LogFileWriter.writeErrorLogLine(e);
        } finally {
            DatabaseConnection.getInstance().close(connection, resultSet, statement);
        }
        return result;
    }

    private static ThermostatJobTO getDataFromRow(ResultSet resultSet) {
        ThermostatJobTO thermostatJob = new ThermostatJobTO();
        try {
            thermostatJob.setPk(resultSet.getInt("pk"));
            thermostatJob.setActive(resultSet.getBoolean("active"));
            thermostatJob.setThermostatPk(resultSet.getInt("thermostat_pk"));
            thermostatJob.setTemperature(resultSet.getDouble("temperature"));
            Integer dayOfWeek = resultSet.getInt("day_of_week"); //if the value is SQL NULL, the value returned is 0
            if (dayOfWeek != null && dayOfWeek != 0) {
                thermostatJob.setDayOfWeek(dayOfWeek);
            }
            thermostatJob.setTime(resultSet.getTime("time"));
            thermostatJob.setDate(resultSet.getTimestamp("date"));
            thermostatJob.setThermostatName(resultSet.getString("name"));
        } catch (SQLException e) {
            LogFileWriter.writeErrorLogLine(e);
        }
        return thermostatJob;
    }

    private static void fillStatement(PreparedStatement statement, ThermostatJobTO thermostatJob) {
        try {
            statement.setBoolean(1, thermostatJob.isActive());
            statement.setInt(2, thermostatJob.getThermostatPk());
            statement.setDouble(3, thermostatJob.getTemperature());
            if (thermostatJob.getDayOfWeek() == null) {
                statement.setNull(4, java.sql.Types.INTEGER);
            } else {
                statement.setInt(4, thermostatJob.getDayOfWeek());
            }
            if (thermostatJob.getTime() == null) {
                statement.setNull(5, java.sql.Types.TIME);
            } else {
                statement.setTime(5, thermostatJob.getTime());
            }
            if (thermostatJob.getDate() == null) {
                statement.setNull(6, java.sql.Types.TIMESTAMP);
            } else {
                statement.setTimestamp(6, thermostatJob.getDate());
            }
        } catch (SQLException e) {
            LogFileWriter.writeErrorLogLine(e);
        }
    }

}
