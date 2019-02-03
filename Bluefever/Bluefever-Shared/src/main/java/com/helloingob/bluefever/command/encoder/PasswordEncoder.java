package com.helloingob.bluefever.command.encoder;

public class PasswordEncoder {

    public static String encode(int pin) {
        String unpadded = Integer.toHexString(pin);
        String padded = "00000000".substring(unpadded.length()) + unpadded;
        String result = "";
        for (int i = padded.length(); i >= 2; i -= 2) {
            result += padded.substring(i - 2, i);
        }
        return result;
    }

}
