package com.helloingob.bluefever.util;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import org.apache.commons.io.FileUtils;

import com.helloingob.bluefever.Settings;

/** Simple, unefficient file logging */
public class LogFileWriter {

    /** Logs line to error file.
     * @param text the line to log */
    public static void writeErrorLogLine(String text) {
        System.err.println(text);
        File logFile = new File(Settings.Files.ERROR_LOG_FILENAME);
        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            FileUtils.write(logFile, getFormattedDate() + text + "\n", true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeErrorLogLine(String message, Throwable throwable) {
        writeErrorLogLine(message + "\n" + convertThrowable(throwable));
    }

    public static void writeErrorLogLine(Throwable throwable) {
        writeErrorLogLine(convertThrowable(throwable));
    }

    private static String convertThrowable(Throwable throwable) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        throwable.printStackTrace(printWriter);
        return stringWriter.toString();
    }

    /** Logs line to debug file.
     * @param text the line to log */
    public static void writeDebugLogLine(String text) {
        String formattedDate = getFormattedDate();
        System.out.println(formattedDate + text);
        File logFile = new File(Settings.Files.DEBUG_LOG_FILENAME);
        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            FileUtils.write(logFile, formattedDate + text + "\n", true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getFormattedDate() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(Settings.DateTime.LOG_OUTPUT);
        LocalDateTime currentTime = LocalDateTime.now(ZoneId.of(Settings.DateTime.TIMEZONE_ID));
        return "[" + currentTime.format(dateTimeFormatter) + "] ";
    }

}
