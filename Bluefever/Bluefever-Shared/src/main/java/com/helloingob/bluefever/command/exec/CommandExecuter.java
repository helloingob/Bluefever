package com.helloingob.bluefever.command.exec;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.helloingob.bluefever.Settings;
import com.helloingob.bluefever.command.encoder.DateCoder;
import com.helloingob.bluefever.command.encoder.PasswordEncoder;
import com.helloingob.bluefever.command.encoder.TemperatureCoder;
import com.helloingob.bluefever.command.exec.CommandMerger.Handle;
import com.helloingob.bluefever.data.to.ThermostatAssetTO;
import com.helloingob.bluefever.data.to.ThermostatTO;
import com.helloingob.bluefever.util.LogFileWriter;

public class CommandExecuter {

    private ThermostatTO thermostat;

    public enum ExecutionType {
        READ, WRITE
    };

    public CommandExecuter(ThermostatTO thermostat) {
        this.thermostat = thermostat;
    }

    public ThermostatTO getDeviceInformation() {
        CommandMerger commandMerger = new CommandMerger(thermostat.getAddress());
        commandMerger.authenticate(PasswordEncoder.encode(thermostat.getPin())); //technically unnecessary
        commandMerger.addReadCommand(Handle.FIRMWARE);
        commandMerger.addReadCommand(Handle.SOFTWARE);
        commandMerger.addReadCommand(Handle.MANUFACTURER);
        commandMerger.addReadCommand(Handle.DEVICENAME);
        CommandInterpreter.getDeviceInformation(executeCommand(commandMerger.getCommandline()), thermostat);
        return thermostat;
    }

    public boolean setPassword(int password) {
        CommandMerger commandMerger = new CommandMerger(thermostat.getAddress());
        commandMerger.authenticate(PasswordEncoder.encode(thermostat.getPin()));
        commandMerger.addWriteCommand(Handle.AUTHENTICATE, PasswordEncoder.encode(password));
        return CommandInterpreter.verifyCommand(executeCommand(commandMerger.getCommandline()), thermostat);
    }

    public LocalDateTime getLocalDateTime() {
        CommandMerger commandMerger = new CommandMerger(thermostat.getAddress());
        commandMerger.authenticate(PasswordEncoder.encode(thermostat.getPin()));
        commandMerger.addReadCommand(Handle.DATE);
        return CommandInterpreter.getLocalDateTime(executeCommand(commandMerger.getCommandline()), thermostat);
    }

    public boolean setDate(LocalDateTime localDateTime) {
        CommandMerger commandMerger = new CommandMerger(thermostat.getAddress());
        commandMerger.authenticate(PasswordEncoder.encode(thermostat.getPin()));
        commandMerger.addWriteCommand(Handle.DATE, DateCoder.encode(localDateTime));
        return CommandInterpreter.verifyCommand(executeCommand(commandMerger.getCommandline()), thermostat);
    }

    public boolean setTemperature(Double temperature) {
        CommandMerger commandMerger = new CommandMerger(thermostat.getAddress());
        commandMerger.authenticate(PasswordEncoder.encode(thermostat.getPin()));
        commandMerger.addWriteCommand(Handle.TEMPERATURE, TemperatureCoder.encodeTemperature(temperature));
        return CommandInterpreter.verifyCommand(executeCommand(commandMerger.getCommandline()), thermostat);
    }

    public boolean setTemperatureOffset(Double temperatureOffset) {
        CommandMerger commandMerger = new CommandMerger(thermostat.getAddress());
        commandMerger.authenticate(PasswordEncoder.encode(thermostat.getPin()));
        commandMerger.addWriteCommand(Handle.TEMPERATURE, TemperatureCoder.encodeTemperatureOffset(temperatureOffset));
        return CommandInterpreter.verifyCommand(executeCommand(commandMerger.getCommandline()), thermostat);
    }

    public ThermostatAssetTO getTemperature() {
        CommandMerger commandMerger = new CommandMerger(thermostat.getAddress());
        commandMerger.authenticate(PasswordEncoder.encode(thermostat.getPin()));
        commandMerger.addReadCommand(Handle.TEMPERATURE);
        return CommandInterpreter.getTemperature(executeCommand(commandMerger.getCommandline()), thermostat);
    }

    /** Runs command {@link Settings.General.CONNECTION_RETRIES} times.
     * @param command the gattool bash command
     * @param verifyOutput checks output for {@link CommandHelper.OUTPUT_PREFIX} and {@link CommandHelper.VALUE_WRITTEN}
     * @param executionType READ/WRITE command */
    private List<String> executeCommand(String command) {
        List<String> consoleOutput = new LinkedList<String>();
        for (int i = 1; i <= Settings.General.CONNECTION_RETRIES; i++) {
            //log only tries >= 2
            if (i > 1) {
                LogFileWriter.writeDebugLogLine("Try #" + i + " ...");
            }
            consoleOutput = run(command);
            if (verifyConsoleOutput(consoleOutput, getExecutionTypeFromCommand(command))) {
                return consoleOutput;
            } else {
                LogFileWriter.writeDebugLogLine("=> '" + consoleOutput.toString() + "'");
            }
        }
        return null;
    }

    /** Detects if a command contains read or write commands. */
    private ExecutionType getExecutionTypeFromCommand(String command) {
        if (command.contains("--char-read")) {
            return ExecutionType.READ;
        } else {
            return ExecutionType.WRITE;
        }
    }

    /** Check if both commands were executed successfully. 
     * @param consoleOutput the command's console output. 
     * @param executionType READ/WRITE command */
    private boolean verifyConsoleOutput(List<String> consoleOutput, ExecutionType executionType) {
        if (consoleOutput.size() > 0) {
            if (consoleOutput.toString().contains("Connection refused")) {
                LogFileWriter.writeDebugLogLine("[ERROR] Connection refused");
                return false;
            } else {
                if (consoleOutput.toString().contains("Device or resource busy")) {
                    LogFileWriter.writeDebugLogLine("[ERROR] Device or resource busy, trying to restart bluetooth service ...");
                    rebootDevice();
                    return false;
                } else {
                    if (consoleOutput.size() >= 2) {
                        switch (executionType) {
                        //getTemperature, getLocalDateTime, ...
                        case READ: {
                            if (consoleOutput.get(0).equals(Settings.Command.VALUE_WRITTEN) && consoleOutput.get(1).contains(Settings.Command.OUTPUT_PREFIX)) {
                                return true;
                            } else {
                                LogFileWriter.writeDebugLogLine("[ERROR] No OUTPUT_PREFIX & VALUE_WRITTEN found");
                                return false;
                            }
                        }
                        //setTemperature, setLocalDateTime, ...
                        case WRITE: {
                            if (consoleOutput.get(0).equals(Settings.Command.VALUE_WRITTEN) && consoleOutput.get(1).equals(Settings.Command.VALUE_WRITTEN)) {
                                return true;
                            } else {
                                LogFileWriter.writeDebugLogLine("[ERROR] No double VALUE_WRITTEN found");
                                return false;
                            }
                        }
                        default:
                            return false;
                        }
                    } else {
                        LogFileWriter.writeDebugLogLine("[ERROR] Output not two-lined");
                        return false;
                    }
                }
            }
        } else {
            LogFileWriter.writeDebugLogLine("[ERROR] Empty output returned");
            return false;
        }
    }

    private static void rebootDevice() {
        List<String> consoleOutput = run("sudo /etc/init.d/bluetooth status");
        LogFileWriter.writeErrorLogLine("bluetoothd status (/etc/init.d/bluetooth status):");
        LogFileWriter.writeErrorLogLine("==================================");
        LogFileWriter.writeErrorLogLine(String.join("\n", consoleOutput));
        LogFileWriter.writeErrorLogLine("----------------------------------");
        LogFileWriter.writeErrorLogLine("Reboot device, adapter stuck.");
        LogFileWriter.writeErrorLogLine("==================================");
        run("sudo reboot");
    }

    /** Runs command in bash. 
     * No content check! 
     * @param command the gattool bash command 
     * @return the console output */
    private static List<String> run(String command) {
        List<String> output = new ArrayList<String>();
        try {
            Process process = Runtime.getRuntime().exec(new String[] { "bash", "-c", command });
            new Thread(new InputStreamHandler(process.getInputStream(), output)).start();
            new Thread(new InputStreamHandler(process.getErrorStream(), output)).start();
            if (process.waitFor(Settings.Command.EXEC_TIMEOUT_MINUTES, TimeUnit.MINUTES)) {
                return output;
            } else {
                process.destroyForcibly();
                LogFileWriter.writeDebugLogLine("[ERROR] The process timed out");
            }
        } catch (Exception e) {
            LogFileWriter.writeErrorLogLine(e);
        }
        return output;
    }

}
