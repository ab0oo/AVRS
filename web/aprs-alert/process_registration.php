<?php
ob_start();
session_start();
include "db_setup.php";
// Connect to server and select databse.
//pg_connect("$host", "$username", "$password")or die("cannot connect");
$conn_string = "host=$host port=5432 dbname=$db_name user=$db_user password=$db_pass options='--client_encoding=UTF8'";
$dbconn = pg_connect($conn_string) or die( "Unable to connect to database");
//mysql_select_db("$db_name")or die("cannot select DB");

// Define $myusername and $mypassword
$myusername=strtoupper($_POST['username']);
$mypassword1=$_POST['password1'];
$mypassword2=$_POST['password2'];

// To protect MySQL injection (more detail about MySQL injection)
$myusername = pg_escape_string(stripslashes($myusername));
$mypassword1 = pg_escape_string(stripslashes($mypassword1));
$mypassword2 = pg_escape_string(stripslashes($mypassword2));

$sql="SELECT * FROM users WHERE upper(username)='$myusername'";
$result=pg_exec($sql);
$count=pg_numrows($result);
if ( $count > 0 ) {
    session_register("error");
    $_SESSION['error'] = "The account you attempted to register already exists.";
    header("location:register.php");
    exit;
}

// pg_numrows is counting table row
$count=pg_numrows($result);
// If result matched $myusername and $mypassword, table row must be 1 row
if ( strlen($mypassword1) < 3 ) {
    session_register("username1");
    $_SESSION['username1'] = $myusername;
    session_register("error");
    $_SESSION['error'] .= "The password needs to be longer than 3 characters.  Really.";
    header("location:register.php");
    exit;
}

if ( $mypassword1 != $mypassword2 ) {
    session_register("username1");
    $_SESSION['username1'] = $myusername;
    session_register("error");
    $_SESSION['error'] .= "The passwords need to match";
    header("location:register.php");
    exit;
}

if ( strlen($mypassword1) < 3 ) {
    session_register("username1");
    $_SESSION['username1'] = $myusername;
    session_register("error");
    $_SESSION['error'] .= "The password needs to be longer than 3 characters.  Really.";
    header("location:register.php");
    exit;
}

$email_address = $_POST['email'];
if (!filter_var($email_address, FILTER_VALIDATE_EMAIL)) {
    session_register("username1");
    $_SESSION['username1'] = $myusername;
    session_register("error");
    $_SESSION['error'] .= "That email address just doesn't look valid to me..";
    header("location:register.php");
    exit;
}

// ok, if we're here, we've passed the validity checks.
$hash = md5(mt_rand());

$subject = "APRS-Alert Registration!";
$body = "Hi,\n\nWe have received your account request for APRS-Alert.net, and need to verify your email\n";
$body .="address to finalize your account.  Please either click on the following link, or copy-paste it\n";
$body .="into a browser window to complete your registration.\n";
$body .="http://www.aprs-alert.net/finalize?id=$hash\n\n";
$body .=" Thanks for giving APRS-Alert a try!\n";
$body .=" de John, AB0OO\n";
$headers = "From: alert@aprs-alert.net\r\n" .
     "X-Mailer: php";
if (mail($email_address, $subject, $body, $headers)) {
    $sql="INSERT INTO pending_users (hash, username, password, timezone, measurement_system, email ) values ( ".
         "'".$hash."', upper('".$myusername."'),md5('".$mypassword1."'),'".$_POST['timezone']."','".$_POST['units']."','".$_POST['email']."')";
    $result=pg_exec($sql);
    header("location:register_complete.php");
    exit;
} else {
  echo("<p>Message delivery failed...</p>");
}


ob_end_flush();
?>
