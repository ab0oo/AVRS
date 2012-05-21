<?php session_start();
if ( !isset($_SESSION['userid'] ) ) {
    header("location:login.php");
}
?>
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
        <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.3.2/jquery.min.js"></script>
        <script type="text/javascript" src="animatedcollapse.js">
            /***********************************************
             * Animated Collapsible DIV v2.4- (c) Dynamic Drive DHTML code library (www.dynamicdrive.com)
             * This notice MUST stay intact for legal use
             * Visit Dynamic Drive at http://www.dynamicdrive.com/ for this script and 100s more
             ***********************************************/
        </script>
        <script type="text/javascript">
            animatedcollapse.addDiv('instructions', 'fade=1,height=450px');
            animatedcollapse.ontoggle=function($, divobj, state){ //fires each time a DIV is expanded/contracted
            //$: Access to jQuery
                //divobj: DOM reference to DIV being expanded/ collapsed. Use "divobj.id" to get its ID
                //state: "block" or "none", depending on state
            }
            animatedcollapse.init()
        </script>
        <?php include "db_setup.php" ?>
        <?php include "ru.php"?>
    </head>
    <body onload='body_onload()'>
        <div id="header"></div>
        <div id="menu"</div><?php include "menu.php"?></div>
        <div id="container">
        <h2>Rules</h2>
        <a href="javascript:animatedcollapse.toggle('instructions')">
        <img title='Generated button' src='images/instructions_f.png' onmouseover='javascript:this.src="images/instructions_b.png"' onmouseout='javascript:this.src="images/instructions_f.png"' /></a>
        <div id="instructions" style="width: 700px; background: #E7FFCC; display:none">
        <p>Rules are used to link specific callsigns to specific zone or movement triggers.  There are three types of movement types:
        <ul><li>MOVEMENT<li>INCURSION<li>EXCURSION</UL>
        <p>
        A <B>MOVEMENT</B> rule tracks for just that:  movement.  A station's location is monitored for an initial position, and that position is
        stored.  After that, new position updates are compared with the previous position, and if the distance between the two is greater than
        about 50 meters, this rule is tripped.</p>
        <p>
        <B>INCURSION</B> and <B>EXCURSION</B> rules require at least one zone be defined on the <a href="zones.php">zone definition</a> page.
        Once a zone is defined, the APRS-IS stream is monitored for an initial position packet from the monitored station.  Subsequent position
        reports are compared against the previous position and their relationship to the specified zone.</p>
        <p>For example, if you specifiy a zone centered on your house, with a radius of 500 meters, and call it "HOME", you can then set up
        an <B>EXCURSION</B> rule based on the "HOME" zone.  The next time you fire up your tracker in your driveway, transmit a postion that
        makes it to APRS-IS, then drive outside your 500m "HOME" zone and transmit a second packet, the <B>EXCURSION</B> rule will fire.</p>
        <p>The <b>Cycle Time</b> determines the reset period of the rule.  Once a rule has been triggered, it cannot be triggered for
        <b>Cycle Time</b> minutes.  The minimum cycle type is 1 minute.</p>
        <p><b>Zones</b> are defined using the <a href="zones.php">zone definition</a> page.  Note that in addition to any custom zones you
        have defined, there is also a GLOBAL zone.  This zone is generally used with <b>MOVEMENT</b> rules to force APRS-Alert to trigger on any
        movement by a station, regardless of where they are.</p>
        </div>
        <hr>
        <?php
        $conn_string = "host=$host port=5432 dbname=$db_name user=$db_user password=$db_pass options='--client_encoding=UTF8'";
        $dbconn = pg_connect($conn_string) or die( "Unable to connect to database");
        $sql="SELECT * FROM monitored_stations WHERE user_id=".$_SESSION['userid'];
        $result=pg_exec($sql);
        $count=pg_numrows($result);
        if ( $count > 1 ) { ?>
        <INPUT align='center' type='image' title='Add new monitoring rule' onclick='addnewrule_onclick()' src='images/addentry_f.png' onmouseover='javascript:this.src="images/addentry_b.png"' onmouseout='javascript:this.src="images/addentry_f.png"'>
        <FORM id='frmSave' name='frmSave' method='post' style='margin: 0px; padding: 0px;'> 
        <table align='center' width='600' class='datatable' style='border:solid 1px black' id='tblRules'>
            <TR><TH>Monitored<br>Station</TH><TH>Rule Type</TH><TH>Cycle Time<br>(in minutes)</TH><TH>Zone</TH><TH>&nbsp;</TH></TR>
        </table>
        <INPUT type='hidden' name='save' id='save' value='1'> 
        <INPUT type='hidden' name='last' id='last' value='0'> 
        <INPUT type='hidden' name='delete' id='delete' value=''> 
        <?php include "commit.php"?>
        </FORM>
        <?php } else { ?>
        <p> You are unable to add rules until you create <a href="monitored_stations.php">monitored stations</a> to apply the rules to.</p>
        <?php } ?>
        </div>
        <div id="footer"</div><?php include "footer.php"?></div>
</body>
</html>
