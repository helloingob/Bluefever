package com.helloingob.bluefever;

public class Settings {

    public static class Files {
        public static final String DEBUG_LOG_FILENAME = "/opt/bluefever/debug.log";
        public static final String ERROR_LOG_FILENAME = "/opt/bluefever/error.log";
    }

    public static class Database {
        public static final String TABLENAME = "thermostats";
        public static final String TABLENAME_ASSET = "thermostat_assets";
        public static final String TABLENAME_JOBS = "thermostat_jobs";
        public static final String CONNECTION_STRING = "jdbc:mariadb://localhost:3306/bluefever?user=bluefever&password=helloingob";
    }

    public static class General {
        public static final int CONNECTION_RETRIES = 3;
    }

    public static class DateTime {
        public static final String LOG_OUTPUT = "dd.MM.yy HH:mm:ss.SSS";
        public static final String TIMEZONE_ID = "Europe/Berlin";
    }

    public static class Command {
        public final static int EXEC_TIMEOUT_MINUTES = 1;

        public final static String VALUE_WRITTEN = "Characteristic value was written successfully";
        public final static String OUTPUT_PREFIX = "Characteristic value/descriptor: ";
        public final static String BLUETOOTH_RESTART_OUTPUT = "[ ok ] Restarting bluetooth (via systemctl): bluetooth.service.";
    }

}
