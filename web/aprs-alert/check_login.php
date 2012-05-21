<?php
ob_start();
include "db_setup.php";

// Connect to server and select databse.
//pg_connect("$host", "$username", "$password")or die("cannot connect");
$conn_string = "host=$host port=5432 dbname=$db_name user=$db_user password=$db_pass options='--client_encoding=UTF8'";
$dbconn = pg_connect($conn_string) or die( "Unable to connect to database");
//mysql_select_db("$db_name")or die("cannot select DB");

// Define $myusername and $mypassword
$myusername=strtoupper($_POST['username']);
$mypassword=$_POST['password'];

// To protect MySQL injection (more detail about MySQL injection)
$myusername = stripslashes($myusername);
$mypassword = stripslashes($mypassword);
$myusername = pg_escape_string($myusername);
$mypassword = pg_escape_string($mypassword);

$sql="SELECT * FROM $tbl_name WHERE upper(username)='$myusername' and password=md5('$mypassword')";
$result=pg_exec($sql);

// pg_numrows is counting table row
$count=pg_numrows($result);
// If result matched $myusername and $mypassword, table row must be 1 row

if($count==1){
    $row = pg_fetch_assoc($result, 0);
    // Register $myusername, $mypassword and redirect to file "login_success.php"
    session_register("username");
    $_SESSION['username'] = $row['username'];
    session_register("userid");
    $_SESSION['userid'] = $row['user_id'];
    session_register("timezone");
    $_SESSION['tz'] = $row['timezone'];
    $_SESSION['units'] = $row['measurement_system'];
    header("location:main.php");
    exit;
}
else {
    session_register("error");
    $_SESSION['error'] = "Incorrect password or unknown user ID";
    header("location:login.php");
    exit;
}

ob_end_flush();
?>
