package com.helloingob.bluefever.command.exec;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import com.helloingob.bluefever.Settings;
import com.helloingob.bluefever.command.encoder.DateCoder;
import com.helloingob.bluefever.command.encoder.TemperatureCoder;
import com.helloingob.bluefever.data.to.ThermostatAssetTO;
import com.helloingob.bluefever.data.to.ThermostatTO;
import com.helloingob.bluefever.util.LogFileWriter;

public class CommandInterpreter {

    public static void getDeviceInformation(List<String> consoleOutput, ThermostatTO thermostat) {
        if (consoleOutput != null && consoleOutput.size() == 5) {
            thermostat.setFirmware(getStringFromHex(consoleOutput.get(1)));
            thermostat.setSoftware(getStringFromHex(consoleOutput.get(2)));
            thermostat.setManufacturer(getStringFromHex(consoleOutput.get(3)));
            thermostat.setDevicename(getStringFromHex(consoleOutput.get(4)));
        } else {
            LogFileWriter.writeDebugLogLine("[ERROR] " + thermostat.getAddress() + " Invalid/Empty output returned!");
        }
    }

    public static boolean verifyCommand(List<String> consoleOutput, ThermostatTO thermostat) {
        if (consoleOutput != null && consoleOutput.size() == 2) {
            return true;
        } else {
            LogFileWriter.writeDebugLogLine("[ERROR] " + thermostat.getAddress() + " Invalid/Empty output returned!");
            return false;
        }
    }

    public static ThermostatAssetTO getTemperature(List<String> consoleOutput, ThermostatTO thermostat) {
        if (consoleOutput != null && consoleOutput.size() == 2) {
            consoleOutput.set(1, consoleOutput.get(1).replace(Settings.Command.OUTPUT_PREFIX, "").replaceAll(" ", ""));
            ThermostatAssetTO thermostatAsset = TemperatureCoder.decode(consoleOutput.get(1));
            thermostatAsset.setThermostatPk(thermostat.getPk());
            return thermostatAsset;
        } else {
            LogFileWriter.writeDebugLogLine("[ERROR] " + thermostat.getAddress() + " Invalid/Empty output returned!");
        }
        return null;
    }

    public static LocalDateTime getLocalDateTime(List<String> consoleOutput, ThermostatTO thermostat) {
        if (consoleOutput != null && consoleOutput.size() == 2) {
            consoleOutput.set(1, consoleOutput.get(1).replace(Settings.Command.OUTPUT_PREFIX, "").replaceAll(" ", ""));
            return DateCoder.decode(consoleOutput.get(1));
        } else {
            LogFileWriter.writeDebugLogLine("[ERROR] " + thermostat.getAddress() + " Invalid/Empty output returned!");
        }
        return null;
    }

    private static String getStringFromHex(String input) {
        input = input.replace(Settings.Command.OUTPUT_PREFIX, "").replaceAll(" ", "");
        byte[] bytes = null;
        try {
            bytes = Hex.decodeHex(input.toCharArray());
            return new String(bytes, StandardCharsets.UTF_8);
        } catch (DecoderException e) {
            LogFileWriter.writeErrorLogLine(e);
        }
        return null;
    }

}
