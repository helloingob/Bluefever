package com.helloingob.bluefever.command.encoder;

import com.helloingob.bluefever.data.to.ThermostatAssetTO;

public class TemperatureCoder {

    /** Encodes the temperature for MANUAL! */
    public static String encodeTemperature(Double temperature) {
        return "80" + getHexFromDouble(temperature) + "8080808080";
    }

    /** Encodes the temperature offset. This is used for the current temperature (+offset). */
    public static String encodeTemperatureOffset(Double offset) {
        return "80808080" + getHexFromDouble(offset) + "8080";
    }

    public static ThermostatAssetTO decode(String hexInput) {
        ThermostatAssetTO thermostatAsset = new ThermostatAssetTO();
        thermostatAsset.setCurrentTemperature(getDoubleFromHex(hexInput.substring(0, 2)));
        thermostatAsset.setManualTemperature(getDoubleFromHex(hexInput.substring(2, 4)));
        thermostatAsset.setLowTemperature(getDoubleFromHex(hexInput.substring(4, 6)));
        thermostatAsset.setHighTemperature(getDoubleFromHex(hexInput.substring(6, 8)));
        thermostatAsset.setOffsetTemperature(getDoubleFromHex(hexInput.substring(8, 10)));
        return thermostatAsset;
    }

    private static String getHexFromDouble(Double input) {
        input *= 2;
        String unpadded = Integer.toHexString(input.intValue());
        return "00".substring(unpadded.length()) + unpadded;
    }

    private static Double getDoubleFromHex(String input) {
        Integer doubleValue = Integer.parseInt(input, 16);
        return doubleValue.doubleValue() / 2;
    }

}
