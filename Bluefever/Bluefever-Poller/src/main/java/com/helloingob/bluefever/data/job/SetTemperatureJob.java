package com.helloingob.bluefever.data.job;

import com.helloingob.bluefever.command.exec.CommandExecuter;
import com.helloingob.bluefever.data.ThermostatDAO;
import com.helloingob.bluefever.data.to.ThermostatJobTO;
import com.helloingob.bluefever.data.to.ThermostatTO;
import com.helloingob.bluefever.util.LogFileWriter;

public class SetTemperatureJob {

    public static void execute(ThermostatJobTO thermostatJob) {
        LogFileWriter.writeDebugLogLine("============================================================================================");
        ThermostatTO thermostat = ThermostatDAO.get(thermostatJob.getThermostatPk());
        if (thermostat != null) {
            CommandExecuter commandExecuter = new CommandExecuter(thermostat);
            if (commandExecuter.setTemperature(thermostatJob.getTemperature())) {
                LogFileWriter.writeDebugLogLine(thermostatJob.getThermostatName() + " set to " + thermostatJob.getTemperature() + " C");
            } else {
                LogFileWriter.writeDebugLogLine("[ERROR] Unable to set temperature (" + thermostatJob.getThermostatName() + ")");
            }
        } else {
            LogFileWriter.writeDebugLogLine("[ERROR] Unable to find thermostat (" + thermostatJob.getThermostatName() + ")");
        }
        LogFileWriter.writeDebugLogLine("============================================================================================");
    }

}
