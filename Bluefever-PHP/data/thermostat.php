<?php
include_once("data/database.php");

class ThermostatTO
{
    public $pk;
    public $name;
    public $address;
    public $pin;
    public $firmware;
    public $software;
    public $manufacturer;
    public $devicename;
}

class ThermostatDAO
{
    function get()
    {
        $connection = new Connection ();
        $conn = $connection->get();

        $result = $conn->query("SELECT * FROM thermostats ORDER BY pk DESC");
        while ($row = $result->fetch_assoc()) {
            $thermostat = new ThermostatTO ();
            $thermostat->pk = $row ["pk"];
            $thermostat->name = $row ["name"];
            $thermostat->address = $row ["address"];
            $thermostat->pin = $row ["pin"];
            $thermostat->firmware = $row ["firmware"];
            $thermostat->software = $row ["software"];
            $thermostat->manufacturer = $row ["manufacturer"];
            $thermostat->devicename = $row ["devicename"];
            $thermostats [] = $thermostat;
        }

        $conn->close();

        usort($thermostats, function ($a, $b) {
            return $b->pk <=> $a->pk;
        });

        return $thermostats;
    }
}

?>