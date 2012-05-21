<?php session_start(); ?>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<?php
if ( empty($_SESSION['username'])) {
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
    </head>
    <body>
        <div id="header"></div>
        <div id="menu"</div><?php include "menu.php"?></div>
        <div id="container">
        <h2>Welcome to APRS-Alert</h2>
        <p> APRS-Alert is the front-end half of a application designed to bridge the gap between Amateur Radio and
        contemporary SMS/Text-messaging capabilities.  It consists of roughly 3000 SLOC of Javascript/PHP, backed
        by approximately 7000 SLOC of pure Java (named Wedjat), and tied together with a custom Postgres Database 
        schema.</p>
        <p>APRS-Alert/Wedjat is part of a larger project to bring open-source, pure JAVA APRS parsers and utility code
        to the global amateur radio community.  The fruits of this can be found on <a href="http://avrs.sourceforge.net/">
        our AVRS SourceForge page</a>.</p>
        <p>If you want to help, use the code, critique or constructively criticize, please drop me a line at 
        <a href="mailto:admin@aprs-alert.net">my APRS-Alert</a> email address<p>
        <p>I hope you find APRS-Alert useful.</p>
        <p>John Gorkos, AB0OO</p>
        <div id="footer"</div><?php include "footer.php"?></div>
</body>
</html>
