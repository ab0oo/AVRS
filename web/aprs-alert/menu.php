<?php

if (empty($_SESSION['count'])) {
 $_SESSION['count'] = 1;
} else {
 $_SESSION['count']++;
}
?>
<table align='center' width='748'>
<tr>
<td align='left'>
<?php
    if ( !empty($_SESSION['username'])) {
        print "<a href=\"main.php\">Home</a>";
    }
?>
</td>
<td align="center" valign="center" >
    <a href="about.php">About</a>&nbsp&nbsp<a href="news.php">News</a>
</td>
<td align="right">
<?php
    if ( empty($_SESSION['username'])) {
        print "<a href=\"login.php\">Register/Sign-In</a>";
    } else {
        print "<a href=\"preferences.php\">".$_SESSION['username']."</a> logged in.&nbsp&nbsp";
        print "<a href=\"logout.php\">Log out</a>";
    }
?>
</tr>
</table>

<?php
    if ( !empty($_SESSION['username'])) {
?>
<HR>
<table width=748>
<TR>
<TD><a href="monitored_stations.php">Monitored<br>Stations</a></TD>
<TD><a href="notification_addresses.php">Notification<br>Addresses</a></TD>
<TD><a href="zones.php">Zones</a></TD>
<TD><a href="rules.php">Rules</a></TD>
<TD><a href="notifications.php">Notifications</a></TD>
</TR>
</table>
<?php } ?>
