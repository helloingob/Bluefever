package com.helloingob.bluefever;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.Assert;
import org.junit.Test;

import com.helloingob.bluefever.command.encoder.DateCoder;

public class DateCoderTests {

    @Test
    public void test_date_decoder() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        Assert.assertEquals(DateCoder.decode("1f110a0b10").format(formatter), "10.11.2016 17:31");
    }

    @Test
    public void test_date_encode() {
        String dateStr = "10.11.2016 17:31";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        LocalDateTime localDateTime = LocalDateTime.parse(dateStr, formatter);
        Assert.assertEquals(DateCoder.encode(localDateTime).toLowerCase(), "1f110a0b10");
    }

}
