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
            animatedcollapse.addDiv('providers', 'fade=1,height=1050px');
            animatedcollapse.ontoggle=function($, divobj, state){ //fires each time a DIV is expanded/contracted
            //$: Access to jQuery
                //divobj: DOM reference to DIV being expanded/ collapsed. Use "divobj.id" to get its ID
                //state: "block" or "none", depending on state
            }
            animatedcollapse.init()
        </script>
        <?php include "db_setup.php" ?>
        <?php include "na.php"?>
    </head>
    <body onload='body_onload()'>
        <div id="header"></div>
        <div id="menu"</div><?php include "menu.php"?></div>
        <div id="container">
        <h2>Notification Addresses</h2><br>
        <p>Here are the notification addresses you have set up.  You can add additional addresses, and remove or edit existing
        addresses using the icons in the list.  You may also send test messages to any notification address.</P>
        <a href="javascript:animatedcollapse.toggle('providers')">
        <img title='Generated button' src='instructions_f.png' onmouseover='javascript:this.src="instructions_b.png"' onmouseout='javascript:this.src="instructions_f.png"' /></a>
        <div id="providers" style="width: 700px; background: #E7FFCC; display:none">
        <TABLE align="center" width="600" border="1">
        <TR><TH>Provider</TH><TH>Phone Address</TH></TR>
        <TR><TD><img src="images/virginsm.jpg"/></TD><TD>phone#@vmobl.com</TD></TR>
        <TR><TD><img src="images/beyondsm.gif"/></TD><TD>phone#@txt.att.net</TD></TR>
        <TR><TD><img src="images/at&tsm.jpg"/></TD><TD>phone#@txt.att.net</TD></TR>
        <TR><TD><img src="images/verizon_sm.jpg"/></TD><TD>phone#@vtext.com</TD></TR>
        <TR><TD><img src="images/centenial.png"/></TD><TD>phone#@cwemail.com</TD></TR>
        <TR><TD><img src="images/cellularsouth.png"/></TD><TD>phone#@csouth1.com</TD></TR>
        <TR><TD><img src="images/boost_sm.jpg"/></TD><TD>phone#@myboostmobile.com</TD></TR>
        <TR><TD><img src="images/nextel_sm.gif"/></TD><TD>phone#@messaging.nextel.com</TD></TR>
        <TR><TD><img src="images/sprint_sm.gif"/></TD><TD>phone#@messaging.sprintpcs.com</TD></TR>
        <TR><TD><img src="images/tmobile_sm.gif"/></TD><TD>phone#@tmomail.net</TD></TR>
        <TR><TD><img src="images/alltel_sm.gif"/></TD><TD>phone#@message.alltel.com</TD></TR>
        <TR><TD><img src="images/qwest_sm.gif"/></TD><TD>phone#@qwestmp.com</TD></TR>
        <TR><TD><img src="images/metropcs_sm.gif"/></TD><TD>phone#@mymetropcs.com</TD></TR>
        <TR><TD><img src="images/cricket_sm.gif"/></TD><TD>phone#mms.mycricket.com</TD></TR>
        <TR><TD><img src="images/bell_sm.gif"/></TD><TD>phone#@txt.bellmobility.ca</TD></TR>
        <TR><TD><img src="images/telus_sm.gif"/></TD><TD>phone#@msg.telus.com</TD></TR>
        <TR><TD><img src="images/rogers_sm.gif"/></TD><TD>hone#@pcs.rogers.com</TD></TR>
        <TR><TD><img src="images/fido_sm.gif"/></TD><TD>phone#@fido.ca</TD></TR>
        </TABLE><br>
        If you have others, please <a href="mailto: admin@aprs-alert.net">mail</a> them to us.<br>
        </div>
        <hr>
        <INPUT align='center' type='image' title='Add new address to notification list' onclick='addnewaddress_onclick()' src='addentry_f.png' onmouseover='javascript:this.src="addentry_b.png"' onmouseout='javascript:this.src="addentry_f.png"'>
        <FORM id='frmSave' name='frmSave' method='post' style='margin: 0px; padding: 0px;'> 
        <table align='center' width='600' class='datatable' style='border:solid 1px black' id='tblAddresses'>
            <TR><TH>Address</TH><TH>Description</TH><TH>Short Form?</TH><TH>&nbsp;</TH></TR>
        </table>
        <?php include "commit.php"?>
        <INPUT type='hidden' name='save' id='save' value='1'>
        <INPUT type='hidden' name='last' id='last' value='0'>
        <INPUT type='hidden' name='delete' id='delete' value=''> 
        </FORM>
        </div>
        <div id="footer"</div><?php include "footer.php"?></div>
    </body>
</html>
