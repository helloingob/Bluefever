package com.helloingob.bluefever;

import org.junit.Assert;
import org.junit.Test;

import com.helloingob.bluefever.command.encoder.PasswordEncoder;

public class PasswordEncoderTests {

    @Test
    public void test_datecoder() {
        int pin = 123;
        Assert.assertTrue(PasswordEncoder.encode(pin).equals(encodePasswordOriginal(String.valueOf(pin))));

        pin = 000000000;
        Assert.assertTrue(PasswordEncoder.encode(pin).equals(encodePasswordOriginal(String.valueOf(pin))));

        pin = 999999999;
        Assert.assertTrue(PasswordEncoder.encode(pin).equals(encodePasswordOriginal(String.valueOf(pin))));

    }

    private static String encodePasswordOriginal(String pin) {
        if (pin == null || pin.isEmpty()) {
            pin = "981723";
        }
        String pinData = Integer.toHexString(Integer.parseInt(pin));
        while (pinData.length() < 8) {
            pinData = "0" + pinData;
        }
        return LSBfirstWithHexString(pinData);
    }

    public static String LSBfirstWithHexString(String string) {
        int i;
        String[] splitData = new String[(string.length() / 2)];
        for (i = 0; i < splitData.length; i++) {
            splitData[i] = string.substring(i * 2, (i * 2) + 2);
        }
        string = "";
        for (i = splitData.length - 1; i >= 0; i--) {
            string = string + splitData[i];
        }
        return string;
    }

}
