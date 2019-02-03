<?php
include_once("data/database.php");

class ThermostatAssetTO
{
    public $date;
    public $currenttemperature;
    public $manualtemperature;
}

class ThermostatAssetDAO
{
    function get($pk, $limit)
    {
        $connection = new Connection ();
        $conn = $connection->get();

        $sql = "SELECT * FROM thermostat_assets WHERE thermostat_pk = " . $pk . " AND date > DATE_SUB(DATE(NOW()), INTERVAL 24 HOUR) ORDER BY date DESC LIMIT " . $limit;
        $result = $conn->query($sql);
        while ($row = $result->fetch_assoc()) {
            $thermostatAsset = new ThermostatAssetTO ();
            $thermostatAsset->date = strtotime($row ["date"]);
            $thermostatAsset->date = $thermostatAsset->date * 1000;
            $thermostatAsset->currenttemperature = $row ["currenttemperature"];
            $thermostatAsset->manualtemperature = $row ["manualtemperature"];
            $thermostatAssets [] = $thermostatAsset;
        }

        $conn->close();
        return $thermostatAssets;
    }

    function getLatest($pk)
    {
        $connection = new Connection ();
        $conn = $connection->get();

        $sql = "SELECT * FROM thermostat_assets WHERE thermostat_pk = " . $pk . " ORDER BY date DESC LIMIT 1";
        $result = $conn->query($sql);
        $row = $result->fetch_assoc();
        $thermostatAsset = new ThermostatAssetTO ();
        $thermostatAsset->date = strtotime($row ["date"]);
        $thermostatAsset->date = $thermostatAsset->date * 1000;
        $thermostatAsset->currenttemperature = $row ["currenttemperature"];
        $thermostatAsset->manualtemperature = $row ["manualtemperature"];

        $conn->close();
        return $thermostatAsset;
    }
}