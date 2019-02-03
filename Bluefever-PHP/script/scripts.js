function changeTo(id, temperature, temperatureMessage) {
    $(temperatureMessage).empty();
    var postForm = {
        'id': id,
        'value': temperature
    };

    $.ajax({
        type: 'POST',
        url: 'api.php',
        data: postForm,
        dataType: 'json',
        success: function (data) {
            if (!data.success) {
                if (data.errors.name) {
                    $(temperatureMessage).append('<br><div class=\"alert alert-danger\"><strong>Fail!</strong> ' + data.errors.name + '</div>');
                }
            } else {
                $(temperatureMessage).append('<br><div class=\"alert alert-success\"><strong> Success!</strong> ' + data.posted + '</div>');
            }
        }
    });

};

function switchThermostat(id) {
    var url = window.location.href;
    url = '?thermostat=' + id;
    window.location.href = url;
}

function addZero(i) {
    if (i < 10) {
        i = "0" + i;
    }
    return i;
}

function getCurrentTime() {
    var dt = new Date();
    var h = addZero(dt.getHours());
    var m = addZero(dt.getMinutes());
    var s = addZero(dt.getSeconds());
    return h + ":" + m + ":" + s;
}
