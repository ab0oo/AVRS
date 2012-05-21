<?php 
session_start();
include "db_setup.php";
$conn_string = "host=$host port=5432 dbname=$db_name user=$db_user password=$db_pass options='--client_encoding=UTF8'";
$dbconn = pg_connect($conn_string) or die( "Unable to connect to database");
if ( !isset($_GET['id'] ) ) {
    session_register("error");
    $_SESSION['error'] = "Your registration ID is invalid or out of date.  Please email admin@aprs-alert.net for help.";
    header("location:login.php");
    exit;
} else {
    $sql = "SELECT username, password, timezone, measurement_system, email FROM pending_users where hash='".$_GET['id']."'";
    $result=pg_exec($sql);
    $numrows=pg_numrows($result);
    if ( $numrows == 0 ) {
        session_register("error");
        $_SESSION['error'] = "Your registration ID is invalid or out of date.  Please email admin@aprs-alert.net for help.";
        header("location:index.php");
        exit;
    }
    $row = pg_fetch_assoc($result, 0);
    $sql = "INSERT INTO users (username, password, timezone, measurement_system, email) VALUES ('".
           $row['username']."','".$row['password']."','".$row['timezone']."',upper('".$row['measurement_system']."'),'".$row['email']."')";
    $result=pg_exec($sql);
    session_register("user_name");
    $_SESSION['user_name'] = $row['username'];
    $sql = "DELETE FROM pending_users WHERE hash='".$_GET['id']."'";
    $result=pg_exec($sql);
    header("location:login.php");
    exit;
}
?>
