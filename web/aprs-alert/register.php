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
</head>
<body>
<div id="header"></div>
<div id="menu"</div><?php include "menu.php"?></div>
<div id="container">
Registration for APRS-Alert is straightforward and easy.  We promise
to only use your contact information for notifications that you ask
for, and for operational notifications about APRS-Alert.  We respect
your privacy and will never expose your contact information to 
outside parties.<br>
<p>
<?php
    if ( !empty($_SESSION['error']) && strlen($_SESSION['error']) > 0 ) { ?>
    <table border="1"><TR><TD><?php echo $_SESSION['error']?></TD></TR></table>
    <? }
?>
<form action="process_registration.php" method="post">
<p>Username:  
<input type="text" name="username" value="<?php if (isset($_POST['username1'])) { echo $_POST['username1']; } ?>" /></p>
<p>Password:  
<input type="password" name="password1" /></p>
<p>Password Again:  
<input type="password" name="password2" /></p><br>
Primary Email Address (used for APRS-Alert administration, not station notifications):<br>
<input type="text" size="30" name="email"><br><br>
<p>Select your timezone.  This timezone will be used for notification
windows.  All time selections from this point forward will be in your
local time, as defined by the timezone you select here.</p>
<select name="timezone" size="0">
<?php
    $zones = timezone_identifiers_list();
    foreach ( $zones as $key => $value ) {
        echo "<option>$value</option>\n";
    }
?>
</select>
<br>
<p>Soon, this site will auto-convert units for you.  Please select your preferred units of measure</P>
<input type="radio" name="units" value="sae" checked> SAE<br>
<input type="radio" name="units" value="metric"> Metric<br>
<p><input type="submit" name="submit" value="Register Now"/></p>
<p>After you hit "Register Now", an email will be sent to your primary email account
with a link you can either click or paste.  Following this link will complete your
registration and activate your APRS-Alert account.</P.
</form>
</div>
<?
    $_SESSION = array();
?>
</body>
</html>
