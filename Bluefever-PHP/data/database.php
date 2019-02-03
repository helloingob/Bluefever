<?php

class Connection
{
    function get()
    {
        $servername = "localhost";
        $username = "bluefever";
        $password = "helloingob";
        $dbname = "bluefever";

        $conn = new mysqli ($servername, $username, $password, $dbname);

        if ($conn->connect_error) {
            die ("Connection failed: " . $conn->connect_error);
        }

        return $conn;
    }
}

?>	