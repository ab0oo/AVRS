<?php    
if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $conn_string = "host=$host port=5432 dbname=$db_name user=$db_user password=$db_pass options='--client_encoding=UTF8'";
    $dbconn = pg_connect($conn_string) or die( "Unable to connect to database");
    foreach ( $_POST as $key => $value ) {
        //echo "Testing $key<br>";
        if ( substr($key,0,10) != "station_id" ) continue;
        preg_match('/.+(\d+)$/', $key, $idarr);
        $id = $idarr[1];
        //echo "Processing $id for changes<br>";
        $userid = pg_escape_string(stripslashes($_SESSION['userid']));
        $callsign = pg_escape_string(stripslashes($_POST['callsign'.$id]));
        //$description = pg_escape_string(stripslashes($_POST['description'.$id]));
        $station_id = pg_escape_string(stripslashes($_POST['station_id'.$id]));
        if ( $value == '0' ) { // this is an insert of a new record
            $sql = "INSERT INTO monitored_stations (user_id,callsign) VALUES (".$userid.",'".$callsign."')";
            //echo $sql."<br>";
            $result = pg_query($dbconn,$sql);
            //echo "Result of execute is $result<br>";
        } else { // this is an update to an existing record
            $sql = "UPDATE monitored_stations set callsign=upper('".$callsign."') where station_id=".$station_id;
            //echo $sql."<br>";
            $result = pg_query($dbconn,$sql);
            //echo "Result of execute is $result<br>";
        }
    }
    $deletions = explode(",", $_POST['delete']);
    foreach ( $deletions as $station_id ) {
        if ( $station_id == "" ) { continue; }
            //echo "I'll be deleting station_id $station_id<br>";
            $sql = "DELETE FROM monitored_stations where user_id=".$_SESSION['userid']." and station_id=".$station_id;
        $result = pg_query($dbconn,$sql);
        //echo $sql."<br>";
    }
    pg_close($dbconn);
}
?>
<script language='javascript' type='application/javascript'> 
var g_nRow = 0;
var g_delete = "";
var g_stations = new Array();

<?php
$conn_string = "host=$host port=5432 dbname=$db_name user=$db_user password=$db_pass options='--client_encoding=UTF8'";
$dbconn = pg_connect($conn_string) or die( "Unable to connect to database");
if ( $_SESSION['userid'] != NULL ) {
    $sql="SELECT * FROM monitored_stations where user_id=".$_SESSION['userid'];
    $result=pg_exec($sql);
    $numrows=pg_numrows($result);
    for($ri = 0; $ri < $numrows; $ri++) {
        $row = pg_fetch_array($result,$ri);
        echo "g_stations[g_stations.length] = {
            \"station_id\":\"".$row['station_id']."\",
                \"callsign\":\"".$row['callsign']."\"};";
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

function addcallsign(pndx, allowEdit) {
    var isNew = (pndx < 0  ||  pndx >= g_stations.length);

    var p = (isNew ? new Object():g_stations[pndx]);

    if (isNew) {
        p.callsign = "";
        p.station_id = 0;
        p.user_id = 0;
    }

    var station_id = p.station_id;

    var rowIndex = (pndx < 0 ? -1:pndx + 1);

    var tbl = document.getElementById("tblStations");
    var row = tbl.insertRow(rowIndex);

    var nRow = (isNew ? g_nRow:pndx);

    row.id = "tr" + (isNew ? g_nRow:pndx);
    row.pndx = pndx;
    row.station_id = station_id;

    row.className = (row.rowIndex % 2 == 1 ? "even":"odd");
    var cell = row.insertCell(0);

    if (allowEdit) {
        var el = document.createElement("input");
        el.type = "text";
        el.name = "callsign" + nRow;
        el.id = el.name;
        el.size = 15;
        el.maxLength = 15;
        el.value = p.callsign;
        el.style.textAlign = "center";
        cell.appendChild(el);

        el.focus();

        el = document.createElement("input");
        el.type = "hidden";
        el.name = "station_id" + nRow;
        el.id = el.name;
        el.value = p.station_id;
        cell.appendChild(el);
    } else {
        cell.innerHTML = p.callsign;
        cell.style.textAlign = "center";
    }

//    cell = row.insertCell(1);
//    if (allowEdit) {
//        el = document.createElement("input");
//        el.type = "text";
//        el.name = "description" + nRow;
//        el.id = "description" + nRow;
//        el.size = 25;
//        el.maxLength = 25;
//        el.value = p.description;
//        cell.appendChild(el);
//    } else {
//        cell.innerHTML = escapeHTML(p.description);
//    }


    cell = row.insertCell(1);
    cell.style.textAlign = "center";

    if (true) {
        cell.innerHTML =
            "<A href='#' onclick=\"remove_onclick(" + nRow + ",'" + station_id + "',this);return false;\" title='Remove callsign from callsign list'><IMG border='0' src='images/remove.png'></A>" +
            (isNew ? "":"&nbsp;<A href='#' onclick=\"edit_onclick(" + nRow + ",'" + station_id + "',this);return false;\" title='Edit this callsign'><IMG border='0' src='imags/edit.png'></A>") +
            (isNew ? "":"&nbsp;<A href='#' onclick=\"undo_onclick(" + nRow + ",'" + station_id + "',this);return false;\" title='Undo changes or undelete this callsign'><IMG border='0' src='images/undo.png'></A>");
    }

    if (isNew) g_nRow++;
    return nRow;
}

function addnewcallsign_onclick(p, nRow) {
    return addcallsign(-1, true);
}

function cancel_onclick() {
    document.location.href = "./monitored_stations.php";
}

function save_onclick() {
    // TODO:  Validation!
    for (var i=0; i<g_nRow; i++)
    {
        var objcallsign = document.getElementById("callsign" + i);
        if (objcallsign == null) continue; 
        if (objcallsign.value.length < 1)
        {
            alert("You must enter at least callsign for each monitored station.\r\n\r\nDelete rows you do not want to save.");
            objcallsign.focus();
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

function edit_onclick(nRow, tpstation_id, objHref) {
    var tbl = document.getElementById("tblStations");
    var r = document.getElementById("tr" + nRow);

    tbl.deleteRow(nRow + 1);
    addcallsign(nRow, true);

    // If this player was on the delete list, take him off now
    g_delete = g_delete.replace("," + tpstation_id, "");
}

function undo_onclick(nRow, tpstation_id, objHref) {
    var tbl = document.getElementById("tblStations");
    var r = document.getElementById("tr" + nRow);

    tbl.deleteRow(nRow + 1);
    addcallsign(nRow, false);

    // If this player was on the delete list, take him off now
    g_delete = g_delete.replace("," + tpstation_id, "");
}

function remove_onclick(nRow, tpstation_id, objHref) {
    var tbl = document.getElementById("tblStations");
    var r = document.getElementById("tr" + nRow);

    // If this was a manually added row, remove it now
    if (tpstation_id == "0") {
        tbl.deleteRow(r.rowIndex);
    } else {
        tbl.deleteRow(nRow + 1);
        addcallsign(nRow, false);

        r = document.getElementById("tr" + nRow);

        var cls = r.className;
        if (cls.indexOf("deleteRow") < 0) cls += " deleteRow";
        r.className = cls;

        // Make sure this callsign is only in the delete list one time
        g_delete = g_delete.replace("," + tpstation_id, "");
        g_delete += "," + tpstation_id;
    }
}

function body_onload() {
    for (var i=0; i<g_stations.length; i++)
    {
        addcallsign(i, false);
    }
    g_nRow = g_stations.length + 1;
}

</script> 
