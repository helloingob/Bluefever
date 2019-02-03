package com.helloingob.bluefever;

import org.junit.Assert;
import org.junit.Test;

import com.helloingob.bluefever.command.encoder.TemperatureCoder;
import com.helloingob.bluefever.data.to.ThermostatAssetTO;

public class TemperatureCoderTest {

    @Test
    public void test_manual_temperature_encode() {
        Assert.assertEquals("802c8080808080", TemperatureCoder.encodeTemperature(22.0));
    }

    @Test
    public void test_offset_temperature_encode() {
        Assert.assertEquals("808080800a8080", TemperatureCoder.encodeTemperatureOffset(5.0));
    }

    @Test
    public void test_manual_temperature_decode() {
        final double DELTA = 1e-15;
        ThermostatAssetTO thermostatAsset = TemperatureCoder.decode("802c8080008080");
        Assert.assertEquals(thermostatAsset.getManualTemperature(), 22.0, DELTA);
    }

    @Test
    public void test_OFF_temperature_deencode() {
        final double DELTA = 1e-15;
        ThermostatAssetTO thermostatAsset = TemperatureCoder.decode("240f202a00040a");
        Assert.assertEquals(thermostatAsset.getManualTemperature(), 7.5, DELTA);
    }

    @Test
    public void test_ON_temperature_deencode() {
        final double DELTA = 1e-15;
        ThermostatAssetTO thermostatAsset = TemperatureCoder.decode("2539202a00040a");
        Assert.assertEquals(thermostatAsset.getManualTemperature(), 28.5, DELTA);
    }

    @Test
    public void test_current_temperature_with_offset_decode() {
        final double DELTA = 1e-15;
        ThermostatAssetTO thermostatAsset = TemperatureCoder.decode("2b261e2402040a");
        Assert.assertEquals(thermostatAsset.getManualTemperature(), 19.0, DELTA);
        Assert.assertEquals(thermostatAsset.getOffsetTemperature(), 1.0, DELTA);
    }

}
