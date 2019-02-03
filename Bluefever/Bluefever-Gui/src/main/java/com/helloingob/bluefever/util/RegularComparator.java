package com.helloingob.bluefever.util;

import java.util.Comparator;

import org.zkoss.zul.Listitem;

import com.helloingob.bluefever.data.to.ThermostatJobTO;

public class RegularComparator implements Comparator<Listitem> {
    private boolean asc = true;

    public RegularComparator(boolean asc) {
        this.asc = asc;
    }

    public int compare(Listitem listitem1, Listitem listitem2) {
        ThermostatJobTO thermostatJob1 = listitem1.getValue();
        ThermostatJobTO thermostatJob2 = listitem2.getValue();
        return (asc ? 1 : -1) * isRegular(thermostatJob1).compareTo(isRegular(thermostatJob2));
    }

    private Boolean isRegular(ThermostatJobTO thermostatJob) {
        if (thermostatJob.getDayOfWeek() != null) {
            return true;
        }
        return false;
    }

}
