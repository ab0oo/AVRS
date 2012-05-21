<?php session_start();
?>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
    <head>
        <title></title>
        <meta name="generator" content="Bluefish 2.0.2" >
        <meta name="author" content="John Gorkos" >
        <meta name="date" content="2011-04-27T19:06:46-0400" >
        <meta name="copyright" content="">
        <meta name="keywords" content="">
        <meta name="description" content="">
        <meta name="ROBOTS" content="NOINDEX, NOFOLLOW">
        <meta http-equiv="content-type" content="text/html; charset=UTF-8">
        <meta http-equiv="content-type" content="application/xhtml+xml; charset=UTF-8">
        <meta http-equiv="content-style-type" content="text/css">
        <meta http-equiv="expires" content="0">
        <link href="main.css" rel="stylesheet" type="text/css">
        <?php include "db_setup.php";
            $conn_string = "host=$host port=5432 dbname=$db_name user=$db_user password=$db_pass options='--client_encoding=UTF8'";
            $dbconn = pg_connect($conn_string) or die( "Unable to connect to database");
            $sql="select user_id, username, timezone, measurement_system,email from users where user_id=".$_SESSION['userid'];
            $result=pg_exec($sql);
            $row = pg_fetch_array($result,0);
            $tz = $row['timezone'];
            $email = $row['email'];
            $ms = $row['measurement_system'];
        ?>
    </head>
    <body>
        <div id="header"></div>
        <div id="menu"</div><?php include "menu.php"?></div>
        <div id="container">
We promise to only use your contact information for notifications that you ask
for, and for operational notifications about APRS-Alert.  We respect
your privacy and will never expose your contact information to 
outside parties.<br>
        <p>
        <?php
            if ( !empty($_SESSION['error']) && strlen($_SESSION['error']) > 0 ) { ?>
            <table border="1"><TR><TD><?php echo $_SESSION['error']?></TD></TR></table>
        <?php }
        ?>
<form action="process_preferences.php" method="post">
<p>Username:  <?php echo $_SESSION['username']?> </p>
<i>leave password fields blank to keep your old password</i>
<p>New Password:  
<input type="password" name="password1" /></p>
<p>New Password Again:  
<input type="password" name="password2" /></p><br>
Primary Email Address (used for APRS-Alert administration, not station notifications):<br>
<input type="text" size="30" name="email" value="<?php echo $email ?>"><br><br>
Select your timezone.  This timezone will be used for notification
windows.  All time selections from this point forward will be in your
local time, as defined by the timezone you select here.<br>
<?php echo "looking for $tz<br>"?>
<select name="timezone" size="0">
<?php
foreach ( timezone_identifiers_list() as $key => $value ) {
    echo "<option";
    if ( $value == $tz ) { echo " selected"; }
    echo ">$value</option>\n";
}
?>
</select>
<br>
<p>Soon, this site will auto-convert units for you.  Please select your preferred units of measure</P>
<input type="radio" name="units" value="sae" checked> SAE<br>
<input type="radio" name="units" value="metric"> Metric<br>
<p><input type="submit" name="submit" value="Save Changes"/></p>
</form>
</div>
</body>
</html>
