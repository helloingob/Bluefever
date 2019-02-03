package com.helloingob.bluefever.command.encoder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateCoder {

    public static LocalDateTime decode(String hexDateString) {
        String minute = String.format("%02d", Integer.parseInt(hexDateString.substring(0, 2), 16));
        String hour = String.format("%02d", Integer.parseInt(hexDateString.substring(2, 4), 16));
        String day = String.format("%02d", Integer.parseInt(hexDateString.substring(4, 6), 16));
        String month = String.format("%02d", Integer.parseInt(hexDateString.substring(6, 8), 16));
        String year = String.format("%02d", Integer.parseInt(hexDateString.substring(8, 10), 16));
        return LocalDateTime.parse(day + month + year + hour + minute, DateTimeFormatter.ofPattern("ddMMyyHHmm"));
    }

    public static String encode(LocalDateTime localDateTime) {
        String year = String.format("%02X", localDateTime.getYear() - 2000);
        String month = String.format("%02X", localDateTime.getMonth().getValue());
        String day = String.format("%02X", localDateTime.getDayOfMonth());
        String hour = String.format("%02X", localDateTime.getHour());
        String minute = String.format("%02X", localDateTime.getMinute());
        return minute + hour + day + month + year;
    }

}
