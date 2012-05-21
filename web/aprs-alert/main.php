<?php session_start(); ?>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<?php
if ( empty($_SESSION['username']) || !(isset($_SESSION['username']) )) {
    header("location:index.php");
}
?>
<html>
    <head>
        <title></title>
        <meta name="generator" content="Bluefish 2.0.2" >
        <meta name="author" content="John Gorkos" >
        <meta name="date" content="2011-04-27T19:20:41-0400" >
        <meta name="copyright" content="">
        <meta name="keywords" content="">
        <meta name="description" content="">
        <meta name="ROBOTS" content="NOINDEX, NOFOLLOW">
        <meta http-equiv="content-type" content="text/html; charset=UTF-8">
        <meta http-equiv="content-type" content="application/xhtml+xml; charset=UTF-8">
        <meta http-equiv="content-style-type" content="text/css">
        <meta http-equiv="expires" content="0">
        <link href="main.css" rel="stylesheet" type="text/css">
        <?php include "db_setup.php" ?>
        <?php include "na.php"?>
    </head>
    <body>
        <div id="header"></div>
        <div id="menu"</div><?php include "menu.php"?></div>
        <div id="container">
        <h2>Welcome to APRS-Alert, <?php print $_SESSION['username']?></h2>
        <?php print "I have you in timezone ".$_SESSION['tz']?><br>
<?php
$conn_string = "host=$host port=5432 dbname=$db_name user=$db_user password=$db_pass options='--client_encoding=UTF8'";
$dbconn = pg_connect($conn_string) or die( "Unable to connect to database");
if ( $_SESSION['userid'] != NULL ) {
    $sql = "select station_id from monitored_stations where user_id=".$_SESSION['userid'];
    $result=pg_exec($sql);
    $numrows=pg_numrows($result);
    echo "<p>Currently, you have ".$numrows." <a href=\"monitored_stations.php\">monitored stations</a>.</P>\n";

    $sql = "select na_id from notification_addresses where user_id=".$_SESSION['userid'];
    $result=pg_exec($sql);
    $numrows=pg_numrows($result);
    echo "<p>You have ".$numrows." <a href=\"notification_addresses.php\">notification addresses</a>.</P>\n";

    $sql = "select zone_id from zones where user_id=".$_SESSION['userid'];
    $result=pg_exec($sql);
    $numrows=pg_numrows($result);
    echo "<p>You have ".$numrows." <a href=\"zones.php\">zones</a> defined.</P>\n";

    $sql = "select rule_id from rules where user_id=".$_SESSION['userid'];
    $result=pg_exec($sql);
    $numrows=pg_numrows($result);
    echo "<p>You have ".$numrows." <a href=\"rules.php\">rules</a> defined.</P>\n";

    $sql = "select n_id from notifications where user_id=".$_SESSION['userid'];
    $result=pg_exec($sql);
    $numrows=pg_numrows($result);
    echo "<p>You have ".$numrows." <a href=\"notifications.php\">notifications</a> defined.</P>\n";
}
?>
    <p>Setting up APRS-Alert can be a bit daunting, especially at first.  Here's a quick overview</p>
    <ul>
    <li>Set up <a href="notification_addresses.php">notification addresses</a>.  You can have as many as you'd like.  
Home email, office email, cell phone SMS address, etc.  There's a handy list to help you find the correct email domain 
for your mobile phone provider.
    <li>Decide which stations you're interested in, and create <a href="monitored_stations.php">monitored stations</a>
entries for them.
    <li>If you are adventurous, create a few <a href="zones.php">zones</a> around areas that interest you.  If 
you're not interested in creating zones, you can monitor for any movement by any of your monitored stations.
    <li>Next, set up the <a href="rules.php">rules</a> for alerts.  You can have as many rules as you'd like: just
because a rule "fires" doesn't mean you'll get a notification (that comes later).  This way, you can have rules that
you seldom use in place, and just enable notification on them when you want it.
    <li>Finally, evetything gets tied together with notifications.  Each rule you defined will have a descriptor.  Just
use the dropdowns to select the rule you want notified on, the day and time windows you want to be notified, and what
notification address to send the alerts to.
    </ul>
        <div id="footer"</div><?php include "footer.php"?></div>
</body>
</html>
