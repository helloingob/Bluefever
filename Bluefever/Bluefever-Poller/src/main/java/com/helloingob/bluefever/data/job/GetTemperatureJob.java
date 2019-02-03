package com.helloingob.bluefever.data.job;

import java.time.Duration;
import java.time.Instant;

import com.helloingob.bluefever.command.exec.CommandExecuter;
import com.helloingob.bluefever.data.ThermostatAssetDAO;
import com.helloingob.bluefever.data.ThermostatDAO;
import com.helloingob.bluefever.data.to.ThermostatAssetTO;
import com.helloingob.bluefever.data.to.ThermostatTO;
import com.helloingob.bluefever.util.LogFileWriter;

public class GetTemperatureJob {

    public static void execute() {
        LogFileWriter.writeDebugLogLine("============================================================================================");
        Instant start = Instant.now();
        LogFileWriter.writeDebugLogLine("Start...");

        for (ThermostatTO thermostat : ThermostatDAO.get()) {
            CommandExecuter commandExecuter = new CommandExecuter(thermostat);
            //new device
            if (thermostat.getFirmware() == null) {
                LogFileWriter.writeDebugLogLine("New Device detected (" + thermostat.getName() + "). Get Metadata...");
                commandExecuter.getDeviceInformation();
                if (ThermostatDAO.update(thermostat)) {
                    LogFileWriter.writeDebugLogLine("Metadata successfully updated.");
                } else {
                    LogFileWriter.writeDebugLogLine("Metadata NOT updated!");
                }
            }
            LogFileWriter.writeDebugLogLine("Getting Temperature (" + thermostat.getName() + ")");
            //poll temperature
            ThermostatAssetTO thermostatAsset = commandExecuter.getTemperature();
            if (thermostatAsset != null) {
                if (ThermostatAssetDAO.persist(thermostatAsset)) {
                    LogFileWriter.writeDebugLogLine("Temperature successfully persisted (" + thermostat.getName() + ": " + thermostatAsset.getCurrentTemperature() + " C)");
                } else {
                    LogFileWriter.writeDebugLogLine("Temperature NOT persisted! (" + thermostat.getName() + ")");
                }
            }
        }
        Instant end = Instant.now();
        LogFileWriter.writeDebugLogLine("Finished. (~" + Duration.between(start, end).getSeconds() + "s)");
        LogFileWriter.writeDebugLogLine("============================================================================================");
    }

}
