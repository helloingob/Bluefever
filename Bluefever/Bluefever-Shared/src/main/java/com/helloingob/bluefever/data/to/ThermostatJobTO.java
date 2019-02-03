package com.helloingob.bluefever.data.to;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class ThermostatJobTO {

    private Integer pk;
    private Boolean active;
    private Integer thermostatPk;
    private String thermostatName;
    private Double temperature;
    private Integer dayOfWeek;
    private Time time;
    private Timestamp date;

    public Integer getPk() {
        return pk;
    }

    public void setPk(Integer pk) {
        this.pk = pk;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Integer getThermostatPk() {
        return thermostatPk;
    }

    public void setThermostatPk(Integer thermostatPk) {
        this.thermostatPk = thermostatPk;
    }

    public String getThermostatName() {
        return thermostatName;
    }

    public void setThermostatName(String thermostatName) {
        this.thermostatName = thermostatName;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Integer getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(Integer dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    @SuppressWarnings("deprecation")
    public LocalDateTime getNextExecution() {
        //regular
        if (getDayOfWeek() != null) {
            int currentDayOfWeek = LocalDate.now().getDayOfWeek().getValue();
            int daysToAdd = getDayOfWeek() - currentDayOfWeek;
            if (daysToAdd < 0) {
                daysToAdd = 7 + daysToAdd;
            }
            LocalDateTime localDateTime = LocalDateTime.now().withHour(getTime().getHours()).withMinute(getTime().getMinutes());
            return localDateTime.plusDays(daysToAdd);
        } else {
            //specific
            return getDate().toLocalDateTime();
        }
    }

    @Override
    public String toString() {
        return "ThermostatJobTO [pk=" + pk + ", active=" + active + ", thermostatPk=" + thermostatPk + ", thermostatName=" + thermostatName + ", temperature=" + temperature + ", dayOfWeek=" + dayOfWeek + ", time=" + time + ", date=" + date + "]";
    }

}
