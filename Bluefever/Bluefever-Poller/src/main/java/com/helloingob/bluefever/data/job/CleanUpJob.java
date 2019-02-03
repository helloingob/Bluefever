package com.helloingob.bluefever.data.job;

import com.helloingob.bluefever.data.ThermostatJobDAO;
import com.helloingob.bluefever.util.LogFileWriter;

public class CleanUpJob {

    public static void execute() {
        LogFileWriter.writeDebugLogLine("============================================================================================");
        if (ThermostatJobDAO.deleteExpiredJobs()) {
            LogFileWriter.writeDebugLogLine("Expired jobs successfully deleted.");
        } else {
            LogFileWriter.writeDebugLogLine("No expired jobs found.");
        }
        LogFileWriter.writeDebugLogLine("============================================================================================");
    }

}
