<?php
include_once("data/database.php");
include_once("data/thermostatjob.php");

$errors = array(); // To store errors
$form_data = array(); // Pass back the data to `form.php`

/* Validate the form on the server side */
if (empty ($_POST ['id'])) {
    $errors ['name'] = 'Id cannot be blank';
}
if (empty ($_POST ['value'])) {
    $errors ['name'] = 'Value cannot be blank';
}
if ($_POST ['id'] < 1) {
    $errors ['name'] = 'Id has to be >= 1';
}
if ($_POST ['value'] < 7.5) {
    $errors ['name'] = 'Value has to be <= 7.5';
}
if ($_POST ['value'] > 28.5) {
    $errors ['name'] = 'Value has to be <= 28.5';
}
if (!empty ($errors)) { // If errors in validation
    $form_data ['success'] = false;
    $form_data ['errors'] = $errors;
} else { // If not, process the form, and return true on success
    if (saveTemperature($_POST ['id'], $_POST ['value'])) {
        $form_data ['success'] = true;
        $form_data ['posted'] = 'Temperature set.';
    } else {
        $form_data ['success'] = false;
        $errors ['name'] = 'Something went wrong.';
        $form_data ['errors'] = $errors;
    }
}

// Return the data back to form.php
echo json_encode($form_data);
function saveTemperature($id, $value)
{
    $thermostatJob = new ThermostatJobTO ();
    $thermostatJob->thermostat_pk = $id;
    $thermostatJob->active = true;
    $thermostatJob->temperature = $value;
    $thermostatJob->date = date('Y-m-d H:i:s', strtotime("+1 min"));

    $thermostatJobDAO = new ThermostatJobDAO ();
    return $thermostatJobDAO->add($thermostatJob);
}

?>