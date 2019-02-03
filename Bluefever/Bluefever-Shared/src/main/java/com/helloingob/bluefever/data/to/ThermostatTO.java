package com.helloingob.bluefever.data.to;

public class ThermostatTO {

    private Integer pk;
    private String name;
    private String address;
    private Integer pin;
    private String firmware;
    private String software;
    private String manufacturer;
    private String devicename;

    public Integer getPk() {
        return pk;
    }

    public void setPk(Integer pk) {
        this.pk = pk;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getPin() {
        return pin;
    }

    public void setPin(Integer pin) {
        this.pin = pin;
    }

    public String getFirmware() {
        return firmware;
    }

    public void setFirmware(String firmware) {
        this.firmware = firmware;
    }

    public String getSoftware() {
        return software;
    }

    public void setSoftware(String software) {
        this.software = software;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getDevicename() {
        return devicename;
    }

    public void setDevicename(String devicename) {
        this.devicename = devicename;
    }

    @Override
    public String toString() {
        return "ThermostatTO [pk=" + pk + ", name=" + name + ", address=" + address + ", pin=" + pin + ", firmware=" + firmware + ", software=" + software + ", manufacturer=" + manufacturer + ", devicename=" + devicename + "]";
    }

}
