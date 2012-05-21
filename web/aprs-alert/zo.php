<?php    
if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $conn_string = "host=$host port=5432 dbname=$db_name user=$db_user password=$db_pass options='--client_encoding=UTF8'";
    $dbconn = pg_connect($conn_string) or die( "Unable to connect to database");
    foreach ( $_POST as $key => $value ) {
        //echo "Testing $key<br>";
        if ( substr($key,0,7) != "zone_id" ) continue;
        preg_match('/.+(\d+)$/', $key, $idarr);
        $id = $idarr[1];
        //echo "Processing $id for changes<br>";
        $userid = pg_escape_string(stripslashes($_SESSION['userid']));
        $zone = pg_escape_string(stripslashes($_POST['zone_id'.$id]));
        $description = pg_escape_string(stripslashes($_POST['description'.$id]));
        if ( strlen($description) == 0 ) $description = "Undefined";
        $latitude = pg_escape_string(stripslashes($_POST['latitude'.$id]));
        $longitude = pg_escape_string(stripslashes($_POST['longitude'.$id]));
        if ( strpos( $latitude, " ") > 0 ) {
            // this is a DD MM.MMM format message, translate
            $dms = explode(" ",$latitude);
            if ( $dms[0] < 0 ) {
                $latitude = $dms[0] - $dms[1]/60.0;
            } else {
                $latitude = $dms[0] + $dms[1]/60.0;
            }
        }
        if ( strpos( $longitude, " ") > 0 ) {
            // this is a DD MM.MMM format message, translate
            $dms = explode(" ",$longitude);
            if ( $dms[0] < 0 ) {
                $longitude = $dms[0] - $dms[1]/60.0;
            } else {
                $longitude = $dms[0] + $dms[1]/60.0;
            }
        }
        $radius = pg_escape_string(stripslashes($_POST['radius'.$id]));
        if ( $radius < 5 ) $radius = 5;
        $pointDefinition = "geometryFromText('POINT(".$longitude." ".$latitude.")',4326)";
        if ( $value == '0' ) { // this is an insert of a new record
            $sql = "INSERT INTO zones (user_id,description,point_radius,point_geom) VALUES (".
                $userid.",'".$description."',".$radius.",".$pointDefinition.")";
            //echo $sql."<br>";
            $result = pg_query($dbconn,$sql);
            //echo "Result of execute is $result<br>";
        } else { // this is an update to an existing record
            $sql = "UPDATE zones set description='".$description."', point_radius=".$radius.", point_geom=".$pointDefinition.
                "  where zone_id=".$zone;
            //echo $sql."<br>";
            $result = pg_query($dbconn,$sql);
            //echo "Result of execute is $result<br>";
        }
    }
    $deletions = explode(",", $_POST['delete']);
    foreach ( $deletions as $zone_id ) {
        if ( $zone_id == "" ) { continue; }
            //echo "I'll be deleting zone_id $zone_id<br>";
            $sql = "DELETE FROM zones where user_id=".$_SESSION['userid']." and zone_id=".$zone_id;
        $result = pg_query($dbconn,$sql);
        //echo $sql."<br>";
    }
    pg_close($dbconn);
}
?>
<script language='javascript' type='application/javascript'> 
var g_nRow = 0;
var g_delete = "";
var g_zones = new Array();

<?php
$conn_string = "host=$host port=5432 dbname=$db_name user=$db_user password=$db_pass options='--client_encoding=UTF8'";
$dbconn = pg_connect($conn_string) or die( "Unable to connect to database");
if ( $_SESSION['userid'] != NULL ) {
    $sql="SELECT zone_id, description, st_y(point_geom), st_x(point_geom), point_radius FROM zones where user_id=".$_SESSION['userid']." order by zone_id";
    $result=pg_exec($sql);
    $numrows=pg_numrows($result);
    for($ri = 0; $ri < $numrows; $ri++) {
        $row = pg_fetch_array($result,$ri);
        echo "g_zones[g_zones.length] = {
            \"zone_id\":\"".$row['zone_id']."\",
                \"description\":\"".$row['description']."\",
                \"latitude\":\"".$row['st_y']."\",
                \"longitude\":\"".$row['st_x']."\",
                \"radius\":\"".$row['point_radius']."\"};";
    }
}
pg_close($dbconn);
?>

function escapeHTML(s) {
    return (
        s.replace(/&/g,'&amp;').
        replace(/>/g,'&gt;').
        replace(/</g,'&lt;').
        replace(/"/g,'&quot;')
    );
}

function addzone(pndx, allowEdit) {
    var isNew = (pndx < 0  ||  pndx >= g_zones.length);

    var p = (isNew ? new Object():g_zones[pndx]);

    if (isNew) {
        p.zone = "";
        p.zone_id = 0;
        p.user_id = 0;
        p.description = "";
        p.short_form = 1;
    }

    var zone_id = p.zone_id;
    var zone = p.description;
    var rowIndex = (pndx < 0 ? -1:pndx + 1);

    var tbl = document.getElementById("tblZones");
    var row = tbl.insertRow(rowIndex);

    var nRow = (isNew ? g_nRow:pndx);

    row.id = "tr" + (isNew ? g_nRow:pndx);
    row.pndx = pndx;
    row.zone_id = zone_id;

    row.className = (row.rowIndex % 2 == 1 ? "even":"odd");
    var cell = row.insertCell(0);

    if (allowEdit) {
        var el = document.createElement("input");
        el.type = "text";
        el.name = "description" + nRow;
        el.id = el.name;
        el.size = 25;
        el.maxLength = 25;
        el.value = zone;
        el.style.textAlign = "left";
        cell.appendChild(el);

        el.focus();

        el = document.createElement("input");
        el.type = "hidden";
        el.name = "zone_id" + nRow;
        el.id = el.name;
        el.value = p.zone_id;
        cell.appendChild(el);
    } else {
        cell.innerHTML = p.description;
        cell.style.textAlign = "center";
    }

    cell = row.insertCell(1);
    if (allowEdit) {
        el = document.createElement("input");
        el.type = "text";
        el.name = "latitude" + nRow;
        el.id = "latitude" + nRow;
        el.size = 10;
        el.maxLength = 10;
        el.value = p.latitude;
        cell.appendChild(el);
    } else {
        cell.innerHTML = escapeHTML(p.latitude);
    }

    cell = row.insertCell(2);
    if (allowEdit) {
        el = document.createElement("input");
        el.type = "text";
        el.name = "longitude" + nRow;
        el.id = "longitude" + nRow;
        el.size = 10;
        el.maxLength = 10;
        el.value = p.longitude;
        cell.appendChild(el);
    } else {
        cell.innerHTML = escapeHTML(p.longitude);
    }


    cell = row.insertCell(3);
    if (allowEdit) {
        el = document.createElement("input");
        el.type = "text";
        el.name = "radius" + nRow;
        el.id = "radius" + nRow;
        el.size = 10;
        el.maxLength = 10;
        el.value = p.radius;
        cell.appendChild(el);
    } else {
        cell.innerHTML = escapeHTML(p.radius);
    }

    cell = row.insertCell(4);
    cell.style.textAlign = "center";

    if (true) {
        cell.innerHTML =
            "<A href='#' onclick=\"remove_onclick(" + nRow + ",'" + zone_id + "',this);return false;\" title='Remove zone from zone list'><IMG border='0' src='images/remove.png'></A>" +
            (isNew ? "":"&nbsp;<A href='#' onclick=\"edit_onclick(" + nRow + ",'" + zone_id + "',this);return false;\" title='Edit this zone'><IMG border='0' src='images/edit.png'></A>") +
            (isNew ? "":"&nbsp;<A href='#' onclick=\"undo_onclick(" + nRow + ",'" + zone_id + "',this);return false;\" title='Undo changes or undelete this zone'><IMG border='0' src='images/undo.png'></A>");
    }

    if (isNew) g_nRow++;
    return nRow;
}

function addnewzone_onclick(p, nRow) {
    return addzone(-1, true);
}

function cancel_onclick() {
    document.location.href = "./zones.php";
}

function save_onclick() {
    // TODO:  Validation!
    for (var i=0; i<g_nRow; i++)
    {
        var objzone = document.getElementById("zone" + i);
        if (objzone == null) continue; 
        if (objzone.value.length < 1)
        {
            alert("You must enter at least a description for each zone.\r\n\r\nDelete rows you do not want to save.");
            objzone.focus();
            return;
        }
    }

    document.getElementById("last").value = g_nRow;
    document.getElementById("delete").value = g_delete;
    document.getElementById("frmSave").submit();
}

function import_onclick() {
    document.getElementById("divImport").style.display = "block";
    document.getElementById("txtBatch").focus();
}

function cancelbatch_onclick() {
    document.getElementById("txtBatch").value = "";
    document.getElementById("divImport").style.display = "none";
}

function edit_onclick(nRow, tpzone_id, objHref) {
    var tbl = document.getElementById("tblZones");
    var r = document.getElementById("tr" + nRow);

    tbl.deleteRow(nRow + 1);
    addzone(nRow, true);

    // If this player was on the delete list, take him off now
    g_delete = g_delete.replace("," + tpzone_id, "");
}

function undo_onclick(nRow, tpzone_id, objHref) {
    var tbl = document.getElementById("tblZones");
    var r = document.getElementById("tr" + nRow);

    tbl.deleteRow(nRow + 1);
    addzone(nRow, false);

    // If this player was on the delete list, take him off now
    g_delete = g_delete.replace("," + tpzone_id, "");
}

function remove_onclick(nRow, tpzone_id, objHref) {
    var tbl = document.getElementById("tblZones");
    var r = document.getElementById("tr" + nRow);

    // If this was a manually added row, remove it now
    if (tpzone_id == "0") {
        tbl.deleteRow(r.rowIndex);
    } else {
        tbl.deleteRow(nRow + 1);
        addzone(nRow, false);

        r = document.getElementById("tr" + nRow);

        var cls = r.className;
        if (cls.indexOf("deleteRow") < 0) cls += " deleteRow";
        r.className = cls;

        // Make sure this zone is only in the delete list one time
        g_delete = g_delete.replace("," + tpzone_id, "");
        g_delete += "," + tpzone_id;
    }
}

function body_onload() {
    for (var i=0; i<g_zones.length; i++)
    {
        addzone(i, false);
    }
    g_nRow = g_zones.length + 1;
}

</script> 
