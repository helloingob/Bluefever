package com.helloingob.bluefever;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.helloingob.bluefever.command.exec.CommandInterpreter;
import com.helloingob.bluefever.data.to.ThermostatAssetTO;
import com.helloingob.bluefever.data.to.ThermostatTO;

public class InterpreterTests {

    @Test
    public void test_output_device_information() {
        ThermostatTO thermostat = new ThermostatTO();
        List<String> consoleOutput = Arrays.asList("Characteristic value was written successfully", "Characteristic value/descriptor: 43 4f 42 4c 30 31 32 32", "Characteristic value/descriptor: 30 2e 30 2e 34", "Characteristic value/descriptor: 45 55 52 4f 74 72 6f 6e 69 63 20 47 6d 62 48", "Characteristic value/descriptor: 43 6f 6d 65 74 20 42 6c 75 65");
        CommandInterpreter.getDeviceInformation(consoleOutput, thermostat);
        Assert.assertEquals("COBL0122", thermostat.getFirmware());
        Assert.assertEquals("0.0.4", thermostat.getSoftware());
        Assert.assertEquals("EUROtronic GmbH", thermostat.getManufacturer());
        Assert.assertEquals("Comet Blue", thermostat.getDevicename());
    }

    @Test
    public void test_output_temperature() {
        ThermostatTO thermostat = new ThermostatTO();
        List<String> consoleOutput = Arrays.asList("Characteristic value was written successfully", "Characteristic value/descriptor: 2d 24 1e 24 0a 08 1e");
        ThermostatAssetTO thermostatAsset = CommandInterpreter.getTemperature(consoleOutput, thermostat);
        Assert.assertEquals(Double.valueOf(22.5), thermostatAsset.getCurrentTemperature());
        Assert.assertEquals(Double.valueOf(18.0), thermostatAsset.getManualTemperature());
        Assert.assertEquals(Double.valueOf(18.0), thermostatAsset.getHighTemperature());
        Assert.assertEquals(Double.valueOf(15.0), thermostatAsset.getLowTemperature());
        Assert.assertEquals(Double.valueOf(5.0), thermostatAsset.getOffsetTemperature());
    }

}
