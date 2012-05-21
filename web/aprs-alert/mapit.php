<?php session_start();
if ( !isset($_SESSION['userid'] ) ) {
    header("location:login.php");
}
?>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" >
    <head>
        <title>APRS-Alert Google Map Zone Selector</title>
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
        <script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=false"></script>
        <script type="text/javascript">
        var map;
        var marker;
        var radius = 2000;
        var startPoint;
        var g_numPoints = 40;
        
        function GenerateMap()
        {
            //Center the map
            var lat = parseFloat(document.getElementById("latitude").value) ;
            var lng = parseFloat(document.getElementById("longitude").value) ;
            var markerPoint = new google.maps.LatLng(lat, lng);
            startPoint = new google.maps.LatLng(lat, lng);
            // Create the map
            var mapOptions = {
            zoom: 3,
            mapTypeId: google.maps.MapTypeId.ROADMAP,
            center: startPoint
            };
        
            map = new google.maps.Map(document.getElementById("Map"), mapOptions);
            //map.addControl(new GLargeMapControl());
        
        
            marker = new google.maps.Marker({
            map:map,
            draggable:true,
            position: markerPoint
            });
        
            google.maps.event.addListener(marker, 'drag', function() { drawCircle(); } );
            //Draw a circle on map around startPoint, with radius 2 miles and 40 points
            drawCircle();
        }     
        
        var poly = [] ; 
        var line ; 
        
        // Draw a circle on map around center (radius in miles)
        // Modified by Jeremy Schneider based on http://maps.huge.info/dragcircle2.htm
        function drawCircle()
        {
            poly = [] ; 
            var lat = marker.getPosition().lat() ;
            var lng = marker.getPosition().lng() ;
            //var lat = parseFloat(document.getElementById("latitude").value) ;
            //var lng = parseFloat(document.getElementById("longitude").value) ;
            var d2r = Math.PI/180 ;                // degrees to radians
            var r2d = 180/Math.PI ;                // radians to degrees
            var dynamicRadius = parseFloat(document.getElementById("dynradius").value);
            var latElement = document.getElementById("latitude");
            var lngElement = document.getElementById("longitude");
            var radElement = document.getElementById("radius");
            latElement.value=lat.toFixed(5);
            lngElement.value=lng.toFixed(5);
            radElement.value=dynamicRadius;
            var Clat = (dynamicRadius/6356752) * r2d ;      //  using 3963 as earth's radius
            //var Clat = (radius/3963) * r2d ;      //  using 3963 as earth's radius
            var Clng = Clat/Math.cos(lat*d2r);
        
            //Add each point in the circle
            for (var i = 0 ; i < g_numPoints ; i++)
            {
                var theta = Math.PI * (i / (g_numPoints / 2)) ;
                Cx = lng + (Clng * Math.cos(theta)) ;
                Cy = lat + (Clat * Math.sin(theta)) ;
                poly.push(new google.maps.LatLng(Cy,Cx)) ;
            }
        
            //Remove the old line if it exists
            if(line)
            {
                line.setMap(null);
            }
        
            //Add the first point to complete the circle
            poly.push(poly[0]) ;
        
            //Create a line with the points from poly, red, 3 pixels wide, 80% opaque
            //line = new GPolyline(poly,'#FF0000', 3, 0.8) ;
            line = new google.maps.Polyline({
            path: poly,
            strokeColor: "#FF0000",
            strokeOpacity: 1.0,
            strokeWeight: 2
            });
            line.setMap(map);
            //map.addOverlay(line) ;
        }
        
        function toggleBounce() {
        
            if (marker.getAnimation() != null) {
            marker.setAnimation(null);
            } else {
            marker.setAnimation(google.maps.Animation.BOUNCE);
            }
        }
        
        function changeRadius(delta) {
            var dynradiusField = document.getElementById("dynradius");
            var radiusEntry = parseFloat(dynradiusField.value);
            radiusEntry+=delta;
            if ( radiusEntry < 50 ) { radiusEntry = 50; }
            dynradiusField.value =  radiusEntry;
            var radiusField = document.getElementById("radius");
            radiusField.value=radiusEntry;
            drawCircle();
        }
        
        </script>
        
    </head>
    <body onload="GenerateMap()">
        <div id="header"></div>
        <div id="menu"</div><?php include "menu.php"?></div>
        <div id="container">
        <h2>APRS-Alert/Google Maps Zone Describer</h2>
        <?php if ( !empty($_SESSION['error'] ) ) {
            echo "<table border=\"1\"><TR><TD>";
            print $_SESSION['error']."<br>";
            echo "</TD></TR></TABLE>\n";
            $_SESSION['error'] ="";
        } 
        ?>
        <p>Using the Google map interface, put the map marker on the center of your zone.  Then, using the zone radius buttons, shrink or expand your zone to the proper size.  Don't forget to enter a descriptive name for your zone at the bottom of the form before submitting it.</P>
        <table>
        <tr>
            <td>
                <div id="Map" style="width: 400px; height: 400px; border: solid 2px black;" ></div>
            </td>
            <td>
                <table width="300" style="width: 300px" align="center">
                    <tr><td><input type="image" src="images/plus5000.png" onClick="changeRadius(5000)"/></td></tr>
                    <tr><td><input type="image" src="images/plus1000.png" onClick="changeRadius(1000)"/></td></tr>
                    <tr><td><input type="image" src="images/plus500.png" onClick="changeRadius(500)"/></td></tr>
                    <tr><td><input type="image" src="images/plus100.png" onClick="changeRadius(100)"/></td></tr>
                    <tr><td><input type="image" src="images/plus50.png" onClick="changeRadius(50)"/></td></tr>
                    <tr><td><input type="text" id="dynradius" value="2000" size="6" onChange="drawCircle()"><br>meter radius</tr></td>
                    <tr><td><input type="image" src="images/minus50.png" onClick="changeRadius(-50)"/></td></tr>
                    <tr><td><input type="image" src="images/minus100.png" onClick="changeRadius(-100)"/></td></tr>
                    <tr><td><input type="image" src="images/minus500.png" onClick="changeRadius(-500)"/></td></tr>
                    <tr><td><input type="image" src="images/minus1000.png" onClick="changeRadius(-1000)"/></td></tr>
                    <tr><td><input type="image" src="images/minus5000.png" onClick="changeRadius(-5000)"/></td></tr>
                </table>
            </td>
        </tr>
        </table>
        <FORM action="map_zone.php" method='post' style='margin: 0px; padding: 0px;'> 
        <p>Latitude <input type="text" id="latitude" name="latitude" size="10" value="37.5" onChange="drawCircle()">
        &nbsp Longitude <input type="text" name="longitude" id="longitude" size="10" value="-92.166" onChange="drawCircle()">
        &nbsp Zone Name <input type="text" name="description" id="description" size="30" value="undefined"></p>
        <input type="hidden" name="zone_id" value="0"/>
        <input type="hidden" name="radius" id="radius" value="2000"/>
        <INPUT align='center' type='image' title='Add new zone to zone list' src='addentry_f.png' onmouseover='javascript:this.src="addentry_b.png"' onmouseout='javascript:this.src="addentry_f.png"'>
        </form>
    </body>
</html>

