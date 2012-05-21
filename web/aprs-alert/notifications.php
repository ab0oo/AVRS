<?php session_start();
if ( !isset($_SESSION['userid'] ) ) {
    header("location:login.php");
}
?>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
    <head>
        <title>Notifications</title>
        <meta name="generator" content="Bluefish 2.0.2" >
        <meta name="author" content="John Gorkos" >
        <meta name="date" content="2011-03-28T18:48:34-0400" >
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
            animatedcollapse.addDiv('instructions', 'fade=1,height=150px');
            animatedcollapse.ontoggle=function($, divobj, state){ //fires each time a DIV is expanded/contracted
            //$: Access to jQuery
                //divobj: DOM reference to DIV being expanded/ collapsed. Use "divobj.id" to get its ID
                //state: "block" or "none", depending on state
            }
            animatedcollapse.init()
        </script>
        <?php include "db_setup.php" ?>
        <?php include "no.php"?>
    </head>
    <body onload='body_onload()'>
        <div id="header"></div>
        <div id="menu"</div><?php include "menu.php"?></div>
        <div id="container">
            <h2>Notifications</h2>
            <a href="javascript:animatedcollapse.toggle('instructions')">
            <img title='Generated button' src='images/instructions_f.png' onmouseover='javascript:this.src="images/instructions_b.png"' onmouseout='javascript:this.src="images/instructions_f.png"' /></a>
            <div id="instructions" style="width: 700px; background: #E7FFCC; display:none">
            <p>There are currently two types of notification:  short and long.  Short messages are
            intended for small-screen devices like mobile phones or pagers.  A typical short
            message will look like this:</p>
            <p>N4AC-9 is moving.  4.06 miles bearing 108.7 from Montgomery, AL</p>
            <p>A long notification will contain all of the information of a short message, and then some.
            A typical long notification message will look like this:</p>
            (long notification, blah blah blah)<br>
            </div>
            <hr>
        <?php
        $conn_string = "host=$host port=5432 dbname=$db_name user=$db_user password=$db_pass options='--client_encoding=UTF8'";
        $dbconn = pg_connect($conn_string) or die( "Unable to connect to database");
        $sql="SELECT * FROM rules WHERE user_id=".$_SESSION['userid'];
        $result=pg_exec($sql);
        $count=pg_numrows($result);
        if ( $count > 1 ) { ?>
        <INPUT align='center' type='image' title='Add new address to notification list' onclick='addnewrule_onclick()' src='images/addentry_f.png' onmouseover='javascript:this.src="images/addentry_b.png"' onmouseout='javascript:this.src="images/addentry_f.png"'>
        <FORM id='frmSave' name='frmSave' method='post' style='margin: 0px; padding: 0px;'> 
        <table align='center' width='740' class='datatable' style='border:solid 1px black' id='tblNotifications'>
            <TR><TH>Rule</TH><TH>Start<br>Time</TH><TH>End<br>Time</TH><TH>Valid Days</TH><TH>Notification Address</TH><TH>&nbsp;</TH></TR>
        </table>
        <INPUT type='hidden' name='save' id='save' value='1'> 
        <INPUT type='hidden' name='last' id='last' value='0'> 
        <INPUT type='hidden' name='delete' id='delete' value=''> 
        <?php include "commit.php"?>
        </FORM>
        <?php } else { ?>
        <p> You are unable to add notifications until you create <a href="rules.php">rules</a> to be notified about.</p>
        <?php } ?>
        <div id="footer"</div><?php include "footer.php"?></div>
    </div>
</body>
</html>
