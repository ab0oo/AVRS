<?php session_start();
if ( !isset($_SESSION['userid'] ) ) {
    header("location:login.php");
}
?>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
    <head>
        <title>APRS-Alert Zone Definition</title>
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
        <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.3.2/jquery.min.js"></script>
        <script type="text/javascript" src="animatedcollapse.js">
            /***********************************************
             * Animated Collapsible DIV v2.4- (c) Dynamic Drive DHTML code library (www.dynamicdrive.com)
             * This notice MUST stay intact for legal use
             * Visit Dynamic Drive at http://www.dynamicdrive.com/ for this script and 100s more
             ***********************************************/
        </script>
        <script type="text/javascript">
            animatedcollapse.addDiv('instructions', 'fade=1,height=350px');
            animatedcollapse.ontoggle=function($, divobj, state){ //fires each time a DIV is expanded/contracted
            //$: Access to jQuery
                //divobj: DOM reference to DIV being expanded/ collapsed. Use "divobj.id" to get its ID
                //state: "block" or "none", depending on state
            }
            animatedcollapse.init()
        </script>
        <?php include "db_setup.php" ?>
        <?php include "zo.php"?>
    </head>
    <body onload='body_onload()'>
        <div id="header"></div>
        <div id="menu"</div><?php include "menu.php"?></div>
        <div id="container">
        <h2>Monitored Zones</h2>
        <a href="javascript:animatedcollapse.toggle('instructions')">
        <img title='Generated button' src='images/instructions_f.png' onmouseover='javascript:this.src="images/instructions_b.png"' onmouseout='javascript:this.src="images/instructions_f.png"' /></a>
        <div id="instructions" style="width: 700px; background: #E7FFCC; display:none">
        <p>Monitored Zones are the heart of APRS-Alert's flexibility.  A zone can be as simple as a circular area defined as a center
        and radius, to complex multi-point polygons.  Right now, the APRS-Alert logic supports complex polygons, including polys
        with holes and crossing edges.  However, defining polygons via a web interface requires some very complex web programming,
        and faced with the decision to delay APRS-Alert for several months, or push a more limited zone description interface, I went
        with the latter</p>
        <p>Below is a table that allows you to define an arbitrary number of zones as point and radius.  Each zone should have a short
        description, and each zone can be used as many times as you'd like (for example, you can set up zone "HOME" as a 1000-meter
        radius around your home QTH, and then set up rules to tell you when station N1ABC, N1DEF, or N1GHI) enter that zone.</p>
        <p>Right now, all zone radii are defined in meters.  Remember that 1 mile is 1609 meters, give or take a few centimeters.  A wise
        APRS-Alert user will take into effect things like anticipated beacon rate of the monitored stations and speed limits on local roads.
        <p>For example, a ham that lives along a straight part of a major highway might not "catch" many stations even with a 800 meter 
        (1 mile across) zone, because trackers with corner-pegging or SmartBeaconing may be waiting up to three minutes between beacons, 
        during which the tracked station might cover 3 miles or more.  Conversely, a ham living in the middle of a neighborhood 
        with winding roads would be well served with a much smaller (300 meter) zone, since smart trackers will beacon multiple times 
        as the vehicle navigates the side streets.</p>
        </div>
        <hr>
        <p>Note, the format for latitude and longitude is "DDD.DDDDD" with South and West negative.  For example, the N4NE-1 Digipeater in Cumming, GA is at 34.2363, -84.160346.</p>
        <p>This page will also accept "DD MM.MMMMM" as a format, so standard APRS formatted positions will work.  Just use a space in place of the ° symbol.  For example, N4NE-1 is at "34 7.53" "84 8.21".</p>
        <INPUT align='center' type='image' title='Add new zone to zone list' onclick='addnewzone_onclick()' src='images/addentry_f.png' onmouseover='javascript:this.src="images/addentry_b.png"' onmouseout='javascript:this.src="images/addentry_f.png"'>
        <a href="mapit.php">
        <img align='center' title='Map-IF Button' src='images/map_if_f.png' onmouseover='javascript:this.src="images/map_if_b.png"' onmouseout='javascript:this.src="images/map_if_f.png"' /></a>
        <FORM id='frmSave' name='frmSave' method='post' style='margin: 0px; padding: 0px;'> 
        <table align='center' width='600' class='datatable' style='border:solid 1px black' id='tblZones'>
            <TR><TH>Description</TH><TH>Center<br>Latitude</TH><TH>Center<br>Longitude</TH><TH>Radius</TH><TH>&nbsp;</TH></TR>
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
