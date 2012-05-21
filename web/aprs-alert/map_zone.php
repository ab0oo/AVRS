<?php session_start();
ob_start();
include "db_setup.php";

// Connect to server and select databse.
//pg_connect("$host", "$username", "$password")or die("cannot connect");
$conn_string = "host=$host port=5432 dbname=$db_name user=$db_user password=$db_pass options='--client_encoding=UTF8'";
$dbconn = pg_connect($conn_string) or die( "Unable to connect to database");

$userid = pg_escape_string(stripslashes($_SESSION['userid']));
$zone = pg_escape_string(stripslashes($_POST['zone_id']));
$description = pg_escape_string(stripslashes($_POST['description']));
if ( strlen($description) == 0 ) $description = "Undefined";
$latitude = pg_escape_string(stripslashes($_POST['latitude']));
$longitude = pg_escape_string(stripslashes($_POST['longitude']));
$radius = pg_escape_string(stripslashes($_POST['radius']));
if ( $radius < 5 ) $radius = 5;
$pointDefinition = "geometryFromText('POINT(".$longitude." ".$latitude.")',4326)";
if ( $zone == '0' ) { // this is an insert of a new record
    $sql = "INSERT INTO zones (user_id,description,point_radius,point_geom) VALUES (".
        $userid.",'".$description."',".$radius.",".$pointDefinition.")";
    //echo $sql."<br>";
    $result = pg_query($dbconn,$sql);
    //echo "Result of execute is $result<br>";
}
header("location:zones.php");
?>
