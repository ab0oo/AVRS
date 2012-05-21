<?php session_start(); ?>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<title>Examples of usage</title>
<meta name="generator" content="Bluefish 2.0.2" >
<meta name="author" content="John Gorkos" >
<meta name="date" content="2011-03-28T19:31:54-0400" >
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
APRS-Alert provides a route for non-APRS users to be informed via "normal" communications
channels about the movements and activities of APRS-equipped stations.  Note the any
station that injects packets to the APRS-IS network can be monited via APRS-Alert; smartphones
with APRS apps are treated just the same as D710s, TinyTrackers, and TNCs with raw NMEA
inputs.<br>
<h2>Examples of usage</h2><br>
<h3>Race Event Management</h3><br>
<p>Local area hams have been asked to monitor three vehicles supporting a 100-mile
bicycle race.  These vehicles are equipped with APRS trackers and have the self-
descriptive tactical callsigns of "LEAD", "TRAIL", and "SAG".</p>
<p>The race director is not a ham, but has asked to be notified whenever the lead and
trail vehicles pass certain checkpoints on the course.  The race director DOES have a
cellphone with SMS capability.</p>
<p>The local Ham coordinator, N0LID, logs into his APRS-Alert account and adds the three
tactical callsigns to his "Monitored Stations" list.  He then creates new Zones around
the checkpoints the race director is interested in.  In this case, the race director wants
to know when riders hit the 25 mile mark, the 50 mile mark, the 75 mile mark, and are 2 miles
from the finish line.  N0LID creates point/radius zones at each of these 4 locations, centered
on the lat/long of the actual point on the road and extending for 1000 meters in each direction.
Since the trackers beacon every 60 seconds, a cyclist would have to be moving 75 mph
to cross the zone completely between beacons.</p>
<p>Once the zones are created, N0LID gets the cell phone number of the race director and asks
her what carrier she uses.  He then creates a new notification address using (for example)
2125551212@tmomail.com and labeling it "Race Director".</p>
<p>Finally, N0LID creates a new rule, instructing APRS-Alert to notify "Race Director" any time on
the day of the race (Saturday seems like a good example) that station "LEAD" enters the
"25 MILE" zone.  He then continues to create "zone incursion" rules for the "50 MILE",
"75 MILE", and "NEAR FINISH" zones.</p>
<p>On race day, the race director will get a series of SMS messages on her phone informing
her of events like "LEAD has entered zone 25-MILE bearing 265 at 22 mph".  In this way, she
stays abreast of the progress of the race and the location of her key elements throughout
the day.  N0LID is a hero, and the "magic" of ham radio is realized through the minimal use
of personnel and a marriage of RF and "traditional" communications paths.</p>
</div>
        <div id="footer"</div><?php include "footer.php"?></div>
</body>
</html>
