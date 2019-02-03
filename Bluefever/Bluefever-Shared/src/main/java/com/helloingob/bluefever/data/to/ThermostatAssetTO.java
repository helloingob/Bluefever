package com.helloingob.bluefever.data.to;

import java.sql.Timestamp;
import java.util.Date;

public class ThermostatAssetTO {

    private Integer pk;
    private Integer thermostatPk;
    private Timestamp date;
    private Double currentTemperature;
    private Double manualTemperature;
    private Double highTemperature;
    private Double lowTemperature;
    private Double offsetTemperature;

    public ThermostatAssetTO() {
        Date date = new Date();
        this.date = new Timestamp(date.getTime());
    }

    public Integer getPk() {
        return pk;
    }

    public void setPk(Integer pk) {
        this.pk = pk;
    }

    public Integer getThermostatPk() {
        return thermostatPk;
    }

    public void setThermostatPk(Integer thermostatPk) {
        this.thermostatPk = thermostatPk;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public Double getCurrentTemperature() {
        return currentTemperature;
    }

    public void setCurrentTemperature(Double currentTemperature) {
        this.currentTemperature = currentTemperature;
    }

    public Double getManualTemperature() {
        return manualTemperature;
    }

    public void setManualTemperature(Double manualTemperature) {
        this.manualTemperature = manualTemperature;
    }

    public Double getHighTemperature() {
        return highTemperature;
    }

    public void setHighTemperature(Double highTemperature) {
        this.highTemperature = highTemperature;
    }

    public Double getLowTemperature() {
        return lowTemperature;
    }

    public void setLowTemperature(Double lowTemperature) {
        this.lowTemperature = lowTemperature;
    }

    public Double getOffsetTemperature() {
        return offsetTemperature;
    }

    public void setOffsetTemperature(Double offsetTemperature) {
        this.offsetTemperature = offsetTemperature;
    }

    @Override
    public String toString() {
        return "ThermostatAssetTO [pk=" + pk + ", thermostatPk=" + thermostatPk + ", date=" + date + ", currentTemperature=" + currentTemperature + ", manualTemperature=" + manualTemperature + ", highTemperature=" + highTemperature + ", lowTemperature=" + lowTemperature + ", offsetTemperature=" + offsetTemperature + "]";
    }

}
