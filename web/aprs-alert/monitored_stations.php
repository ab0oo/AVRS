<?php session_start() ?>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
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
        <?php include "ms.php"?>
    </head>
    <body onload='body_onload()'>
        <div id="header"></div>
        <div id="menu"</div><?php include "menu.php"?></div>
        <div id="container">
        <h2>Monitored Stations</h2>
        <p>Here are the stations you can monitor for activity or movement.  Identifying the callsign of stations you are interested
        in is the first step in using APRS-Alert.  Note that case does not matter, but SSID does.  No wildcards are allowed.</p>
        <INPUT align='center' type='image' title='Add new address to notification list' onclick='addnewcallsign_onclick()' src='images/addentry_f.png' onmouseover='javascript:this.src="images/addentry_b.png"' onmouseout='javascript:this.src="images/addentry_f.png"'>
        <FORM id='frmSave' name='frmSave' method='post' style='margin: 0px; padding: 0px;'> 
        <table align='center' width='600' class='datatable' style='border:solid 1px black' id='tblStations'>
            <TR><TH>Callsign</TH><TH>&nbsp;</TH></TR>
        </table>
        <INPUT type='hidden' name='save' id='save' value='1'> 
        <INPUT type='hidden' name='last' id='last' value='0'> 
        <INPUT type='hidden' name='delete' id='delete' value=''> 
        <?php include "commit.php"?>
        </FORM>
        </div>
        <div id="footer"</div><?php include "footer.php"?></div>
    </body>
</html>
