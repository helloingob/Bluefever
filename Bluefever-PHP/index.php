<!DOCTYPE html>
<html lang="en">
<head>
    <title>Bluefever Thermostat Management</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="/css/bootstrap/4.0.0-beta/bootstrap.min.css">
    <link rel="stylesheet" href="/css/fontawesome/4.7.0/css/font-awesome.min.css">
    <link rel="stylesheet" href="/css/styles.css">
    <link rel="shortcut icon" href="/favicon.ico" type="image/x-icon">
    <link rel="icon" href="/favicon.ico" type="image/x-icon">
</head>
<body>
<div class="container">
    <?php
    include("data/thermostat.php");
    include("data/thermostatasset.php");
    include("data/thermostatjob.php");

    $thermostatDAO = new ThermostatDAO();
    $thermostatAssetDAO = new ThermostatAssetDAO();
    $thermostatJobDAO = new ThermostatJobDAO();
    $thermostats = $thermostatDAO->get();

    $thermostatActiveIndex = null;
    if (isset($_GET['thermostat'])) {
        if ($thermostatActiveIndex <= sizeof($thermostats)) {
            $thermostatActiveIndex = $_GET['thermostat'];
        } else {
            $thermostatActiveIndex = 0;
        }
    } else {
        $thermostatActiveIndex = 0;
    }
    $thermostat = $thermostats[$thermostatActiveIndex];

    $thermostatAssets = $thermostatAssetDAO->get($thermostat->pk, 24);
    if (count($thermostatAssets) > 0) {
        foreach ($thermostatAssets as $thermostatAsset) {
            $currentTemperatures[] = "[" . $thermostatAsset->date . ", " . $thermostatAsset->currenttemperature . "]";
            $manualTemperatures[] = "[" . $thermostatAsset->date . ", " . $thermostatAsset->manualtemperature . "]";
        }

    }

    $latestThermostatAsset = $thermostatAssetDAO->getLatest($thermostat->pk);
    echo printThermostatChooser($thermostats, $thermostatActiveIndex);
    echo printLatestPoll($latestThermostatAsset);
    ?>
    <div id="chartTemperature" style="width:100%; height:400px;"></div>
    <?php
    echo printThermostatQuickstart($thermostat, $latestThermostatAsset);

    function printThermostatChooser($thermostats, $thermostatActiveIndex)
    {
        $result = "<div class=\"text-center\">";
        $result .= "<form method=\"post\" name=\"postFormChooser\">";
        $max = sizeof($thermostats);
        for ($i = 0; $i <= $max - 1; $i++) {
            if ($i == $thermostatActiveIndex) {
                $buttonActive = "btn-success";
            } else {
                $buttonActive = "btn-secondary";
            }
            $result .= "<button class=\"btn " . $buttonActive . " btn-lg \" style=\"margin-right: 2px;\" type=\"button\" onclick=\"switchThermostat(" . $i . ")\">" . $thermostats[$i]->name . "</button>";
        }
        $result .= "</form>";
        $result .= "</div>";
        $result .= "</div>";
        return $result;
    }

    function printLatestPoll($latestThermostatAsset)
    {
        $result = "<div class=\"text-center\">";
        $result .= "Last Poll: " . date("d.m H:i", substr($latestThermostatAsset->date, 0, 10));

        $output = shell_exec("ps -ef | grep bfcentral.jar");
        if (strpos($output, 'java -jar /opt/bluefever/bfcentral.jar ') !== false) {
            $result .= "&nbsp;&nbsp;<i class=\"fa fa-circle\" aria-hidden=\"true\" style=\"color:#CF000F\"></i>";
        } else {
            $result .= "&nbsp;&nbsp;<i class=\"fa fa-circle\" aria-hidden=\"true\" style=\"color:#66FF66\"></i>";
        }

        $result .= "</div >";
        return $result;
    }

    function printThermostatQuickstart($thermostat, $latestThermostatAsset)
    {
        $hotValue = 28.5;
        if ($latestThermostatAsset->currenttemperature <= $hotValue - 4) {
            $hotValue = $latestThermostatAsset->currenttemperature + 4;
        }
        $result = "<div class=\"text-center\">";
        $result .= "<form method=\"post\" name=\"postFormTemperature\">";
        $result .= "<button class=\"btn btn-lg btn-primary\" style=\"margin-right: 2px;\"  type=\"button\" onclick=\"changeTo(" . $thermostat->pk . ", 7.5, temperatureMessage)" . "\"><i class=\"fa fa-power-off\" aria-hidden=\"true\"></i></button>";
        $result .= "<button class=\"btn btn-lg btn-info\" style=\"margin-right: 2px;\" type=\"button\" onclick=\"changeTo(" . $thermostat->pk . ", " . ($latestThermostatAsset->currenttemperature - 2) . ", temperatureMessage)" . "\">-2</button>";
        $result .= "<button class=\"btn btn-lg btn-info\" type=\"button\"onclick=\"changeTo(" . $thermostat->pk . ", " . ($latestThermostatAsset->currenttemperature - 1) . ", temperatureMessage)" . "\">-1</button>";
        $result .= "<span class=\"currentTemperature align-middle\">&nbsp;" . $latestThermostatAsset->currenttemperature . "&deg;&nbsp;</span>";
        $result .= "<button class=\"btn btn-lg btn-warning\" type=\"button\" onclick=\"changeTo(" . $thermostat->pk . " " . ($latestThermostatAsset->currenttemperature + 1) . ", temperatureMessage)" . "\">+1</button>";
        $result .= "<button class=\"btn btn-lg btn-warning\" style=\"margin-left: 2px;\" type=\"button\" onclick=\"changeTo(" . $thermostat->pk . ", " . ($latestThermostatAsset->currenttemperature + 2) . ", temperatureMessage)" . "\">+2</button>";
        $result .= "<button class=\"btn btn-lg btn-danger\" style=\"margin-left: 2px;\" type=\"button\" onclick=\"changeTo(" . $thermostat->pk . ", " . $hotValue . ", temperatureMessage)" . "\"><i class=\"fa fa-fire\" aria-hidden=\"true\"></i></button>";
        $result .= "<div id=\"temperatureMessage\"><br></div>";
        $result .= "</form>";
        $result .= "</div>";
        $result .= "</div>";
        return $result;
    }

    ?>
</div>
</body>

<script src="/script/jquery/3.2.1/jquery.min.js"></script>
<script src="/script/bootstrap/4.0.0-beta/bootstrap.min.js"></script>
<script src="/script/highcharts/5.0.14/highcharts.js"></script>
<script src="/script/highcharts/5.0.14/themes/grid-light.js"></script>
<script src="/script/scripts.js"></script>
<script>
    Highcharts.setOptions({
        global: {
            timezoneOffset: -2 * 60
        }
    });

    $(function () {
        $('#chartTemperature').highcharts({
            chart: {
                type: 'line',
                backgroundColor: "white",
            },
            title: {
                text: ""
            },
            xAxis: {
                type: 'datetime',
                labels: {
                    enabled: false
                }
            },
            yAxis: {
                title: {
                    enabled: false
                },
                visible: false
            },
            credits: {
                enabled: false
            },
            plotOptions: {
                line: {
                    dataLabels: {
                        enabled: true,
                        color: "#7f8c8d"
                    },
                    marker: {
                        enabled: false
                    }
                }
            },
            legend: {
                enabled: false
            },
            tooltip: {
                xDateFormat: '%d.%m.%Y %H:00',
                pointFormat: " < span style = \"color:{point.color}\">\u25CF</span> {series.name} Temperature: <b>{point.y:,.2f} \u2103</b><br/>"
            },
            series:
                [{
                    name: 'Current',
                    color: 'red',
                    data: [<?php echo join($currentTemperatures, ',') ?>]
                },
                    {
                        name: 'Manual',
                        color: 'blue',
                        data: [<?php echo join($manualTemperatures, ',') ?>]
                    }]
        });
    });
</script>
</html>