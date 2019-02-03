<?php
include_once("data/database.php");

class ThermostatJobTO
{
    public $pk;
    public $thermostat_pk;
    public $active;
    public $temperature;
    public $dayOfWeek;
    public $time;
    public $date;
    public $nextExecution;
}

class ThermostatJobDAO
{
    function get($pk)
    {
        $connection = new Connection ();
        $conn = $connection->get();

        $sql = "SELECT * FROM thermostat_jobs JOIN thermostats ON thermostat_jobs.thermostat_pk = thermostats.pk WHERE thermostats.pk = " . $pk;
        $result = $conn->query($sql);
        while ($row = $result->fetch_assoc()) {
            $thermostatJob = new ThermostatJobTO ();
            $thermostatJob->pk = $row ["pk"];
            $thermostatJob->thermostat_pk = $row ["thermostat_pk"];
            $thermostatJob->active = $row ["active"];
            $thermostatJob->temperature = $row ["temperature"];
            $thermostatJob->dayOfWeek = $row ["day_of_week"];
            $thermostatJob->time = $row ["time"];

            $originalDate = $row ["date"];
            $newDate = date("d.m H:i", strtotime($originalDate));
            $thermostatJob->date = $newDate;

            if ($thermostatJob->dayOfWeek == NULL) {
                $thermostatJob->nextExecution = strtotime($row ["date"]);
            } else {
                $currentDayOfWeek = date("N");
                $daysToAdd = $thermostatJob->dayOfWeek - $currentDayOfWeek;
                if ($daysToAdd < 0) {
                    $daysToAdd = 7 + $daysToAdd;
                }
                $constructedDate = strtotime($thermostatJob->time);
                $constructedDate = strtotime("+" . $daysToAdd . " day", $constructedDate);
                $thermostatJob->nextExecution = $constructedDate;
            }

            $thermostatJobs [] = $thermostatJob;
        }
        $conn->close();
        usort($thermostatJobs, function ($a, $b) {
            return $a->nextExecution <=> $b->nextExecution;
        });
        return $thermostatJobs;
    }

    function add($thermostatJob)
    {
        $connection = new Connection ();
        $conn = $connection->get();

        $sql = "INSERT INTO thermostat_jobs (active, thermostat_pk, temperature, day_of_week, time, date) VALUES (?,?,?,?,?,?);";

        $statement = $conn->prepare($sql);
        $statement->bind_param("iidiss", $thermostatJob->active, $thermostatJob->thermostat_pk, $thermostatJob->temperature, $thermostatJob->dayOfWeek, $thermostatJob->time, $thermostatJob->date);
        $statement->execute();

        echo $statement->error;

        $statement->close();
        $conn->close();

        return true;
    }
}

?>