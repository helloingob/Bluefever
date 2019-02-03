package com.helloingob.bluefever;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.helloingob.bluefever.data.ThermostatAssetDAO;
import com.helloingob.bluefever.data.ThermostatDAO;
import com.helloingob.bluefever.data.to.ThermostatAssetTO;
import com.helloingob.bluefever.data.to.ThermostatTO;

public class DatabaseTests {

    @Test
    public void test_database() {
        ThermostatTO thermostat = new ThermostatTO();
        thermostat.setPk(1);
        thermostat.setName("Living Room");
        thermostat.setAddress("54:4A:16:5D:13:XX");
        thermostat.setPin(0);

        Assert.assertTrue(ThermostatDAO.add(thermostat));
        ThermostatAssetTO thermostatAsset = new ThermostatAssetTO();
        thermostatAsset.setCurrentTemperature(22.5);
        thermostatAsset.setHighTemperature(22.5);
        thermostatAsset.setLowTemperature(22.5);
        thermostatAsset.setOffsetTemperature(22.5);
        thermostatAsset.setManualTemperature(22.5);
        thermostatAsset.setThermostatPk(thermostat.getPk());

        Assert.assertTrue(ThermostatAssetDAO.persist(thermostatAsset));
        Assert.assertTrue(ThermostatDAO.deviceExists(thermostat));

        List<ThermostatTO> thermostats = ThermostatDAO.get();
        Assert.assertEquals(thermostats.size(), 1);
    }

}
