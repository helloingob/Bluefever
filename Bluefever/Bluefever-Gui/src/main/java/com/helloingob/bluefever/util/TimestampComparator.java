package com.helloingob.bluefever.util;

import java.util.Comparator;

import org.zkoss.zul.Listitem;

import com.helloingob.bluefever.data.to.ThermostatJobTO;

public class TimestampComparator implements Comparator<Listitem> {
    private boolean asc = true;

    public TimestampComparator(boolean asc) {
        this.asc = asc;
    }

    public int compare(Listitem listitem1, Listitem listitem2) {
        ThermostatJobTO thermostatJob1 = listitem1.getValue();
        ThermostatJobTO thermostatJob2 = listitem2.getValue();
        return (asc ? 1 : -1) * thermostatJob1.getNextExecution().compareTo(thermostatJob2.getNextExecution());
    }

}
