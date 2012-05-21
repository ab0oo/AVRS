<?php    
if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $conn_string = "host=$host port=5432 dbname=$db_name user=$db_user password=$db_pass options='--client_encoding=UTF8'";
    $dbconn = pg_connect($conn_string) or die( "Unable to connect to database");
    //echo "<PRE>\n";
    //print_r($_POST);
    //echo "</PRE>\n";
    foreach ( $_POST as $key => $value ) {
        //echo "Testing $key<br>";
        if ( substr($key,0,7) != "rule_id" ) continue;
        preg_match('/.+(\d+)$/', $key, $idarr);
        $id = $idarr[1];
        //echo "Processing $id for changes<br>";
        $userid = pg_escape_string(stripslashes($_SESSION['userid']));
        $rule_id = pg_escape_string(stripslashes($_POST['rule_id'.$id]));
        $zone_id = pg_escape_string(stripslashes($_POST['zone_id'.$id]));
        $station_id = pg_escape_string(stripslashes($_POST['station_id'.$id]));
        $cycle_time = pg_escape_string(stripslashes($_POST['cycle_time'.$id]));
        $ruletype = pg_escape_string(stripslashes($_POST['ruletype'.$id]));
        if ( $value == '0' ) { // this is an insert of a new record
            $sql = "INSERT INTO rules (user_id,station_id,ruletype,cycle_time,zone_id) VALUES ("
                .$userid.",".$station_id.",'".$ruletype."',".$cycle_time.",".$zone_id.")";
            //echo $sql."<br>";
            $result = pg_query($dbconn,$sql);
            //echo "Result of execute is $result<br>";
        } else { // this is an update to an existing record
            $sql = "UPDATE rules set zone_id=".$zone_id.
                ", station_id=".$station_id.", ruletype='".$ruletype."',cycle_time=".$cycle_time." where rule_id=".$rule_id;
            //echo $sql."<br>";
            $result = pg_query($dbconn,$sql);
            //echo "Result of execute is $result<br>";
        }
    }
    $deletions = explode(",", $_POST['delete']);
    foreach ( $deletions as $rule_id ) {
        if ( $rule_id == "" ) { continue; }
            //echo "I'll be deleting rule_id $rule_id<br>";
            $sql = "DELETE FROM rules where user_id=".$_SESSION['userid']." and rule_id=".$rule_id;
        $result = pg_query($dbconn,$sql);
        //echo $sql."<br>";
    }
    pg_close($dbconn);
}
?>
<script language='javascript' type='application/javascript'> 
var g_nRow = 0;
var g_delete = "";
var g_rules = new Array();
var g_stations = new Array();
var g_zones = new Array();
var ruletypes = new Array("MOVEMENT", "INCURSION", "EXCURSION");

g_zones[g_zones.length] = { "zone_id":"0", "description":"Global Zone"}; 

<?php
$conn_string = "host=$host port=5432 dbname=$db_name user=$db_user password=$db_pass options='--client_encoding=UTF8'";
$dbconn = pg_connect($conn_string) or die( "Unable to connect to database");
if ( $_SESSION['userid'] != NULL ) {
    $sql="select rule_id, station_id, ruletype, cycle_time, zone_id from rules r where r.user_id=".$_SESSION['userid']." order by rule_id";
    $result=pg_exec($sql);
    $numrows=pg_numrows($result);
    for($ri = 0; $ri < $numrows; $ri++) {
        $row = pg_fetch_array($result,$ri);
        echo "g_rules[g_rules.length] = {
            \"rule_id\":\"".$row['rule_id']."\",
                \"station_id\":\"".$row['station_id']."\",
                \"ruletype\":\"".$row['ruletype']."\",
                \"cycle_time\":\"".$row['cycle_time']."\",
                \"zone_id\":\"".$row['zone_id']."\"};\n";
    }
    $sql="SELECT * FROM monitored_stations where user_id=".$_SESSION['userid']." order by station_id";
    $result=pg_exec($sql);
    $numrows=pg_numrows($result);
    for($ri = 0; $ri < $numrows; $ri++) {
        $row = pg_fetch_array($result,$ri);
        echo "g_stations[g_stations.length] = { \"station_id\":\"".$row['station_id']."\", \"callsign\":\"".$row['callsign']."\"};\n";
    }
    $sql="SELECT zone_id, description FROM zones where user_id=".$_SESSION['userid']." order by zone_id";
    $result=pg_exec($sql);
    $numrows=pg_numrows($result);
    for($ri = 0; $ri < $numrows; $ri++) {
        $row = pg_fetch_array($result,$ri);
        echo "g_zones[g_zones.length] = { \"zone_id\":\"".$row['zone_id']."\", \"description\":\"".$row['description']."\"};\n";
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

function addrule(pndx, allowEdit) {
    var isNew = (pndx < 0  ||  pndx >= g_rules.length);

    var p = (isNew ? new Object():g_rules[pndx]);

    if (isNew) {
        p.rule = "";
        p.rule_id = 0;
        p.user_id = 0;
        p.zone_id = 0;
        p.cycle_time = 30;
        p.ruletype = 'MOVEMENT';
    }

    var rule_id = p.rule_id;

    var rowIndex = (pndx < 0 ? -1:pndx + 1);

    var tbl = document.getElementById("tblRules");
    var row = tbl.insertRow(rowIndex);

    var nRow = (isNew ? g_nRow:pndx);

    row.id = "tr" + (isNew ? g_nRow:pndx);
    row.pndx = pndx;
    row.rule_id = rule_id;

    row.className = (row.rowIndex % 2 == 1 ? "even":"odd");
    var cell = row.insertCell(0);

    if (allowEdit) {
        var el = document.createElement("select");
        el.name = "station_id" + nRow;
        el.id = el.name;
        //el.value = p.callsign;
        el.style.width = "100%";
        for (var i=0; i<g_stations.length; i++) {
            var d = g_stations[i].station_id;
            el.options[i] = new Option(g_stations[i].callsign, d, p.callsign == d, p.callsign == d);
        }
        cell.appendChild(el);

        el.focus();

        el = document.createElement("input");
        el.type = "hidden";
        el.name = "rule_id" + nRow;
        el.id = el.name;
        el.value = p.rule_id;
        cell.appendChild(el);
    } else {
        for (var i=0; i<g_stations.length; i++) {
            var d = g_stations[i];
            if ( d.station_id == p.station_id ) {
                cell.innerHTML = d.callsign;
            }
        }
        cell.style.textAlign = "center";
    }

    cell = row.insertCell(1);
    if (allowEdit) {
        el = document.createElement("select");
        el.name = "ruletype" + nRow;
        el.id = "ruletype" + nRow;
        el.value = p.ruletype;
        for (var i=0; i<ruletypes.length; i++) {
            var thisOne = false;
            if ( ruletypes[i] == p.ruletype) { thisOne = true; }
            var d = ruletypes[i];
            el.options[i] = new Option(ruletypes[i], d, thisOne, thisOne );
        }
        cell.appendChild(el);
    } else {
        cell.innerHTML = escapeHTML(p.ruletype);
    }

    cell = row.insertCell(2);
    if (allowEdit) {
        el = document.createElement("input");
        el.type = "text";
        el.name = "cycle_time" + nRow;
        el.id = "cycle_time" + nRow;
        el.size = 5;
        el.maxLength = 5;
        el.value = p.cycle_time;
        cell.appendChild(el);
    } else {
        cell.innerHTML = escapeHTML(p.cycle_time);
    }

    cell = row.insertCell(3);
    if (allowEdit) {
        el = document.createElement("select");
        el.name = "zone_id" + nRow;
        el.id = "zone_id" + nRow;
        for (var i=0; i<g_zones.length; i++) {
            var d = g_zones[i].zone_id;
            el.options[i] = new Option(g_zones[i].description, d, p.zone_id == d, p.zone_id == d);
        }
        var found = false;
        for (var i=0; i<g_zones.length; i++) {
            var d = g_zones[i];
            if ( d.zone_id == p.zone_id ) {
                el.value = escapeHTML(d.description);
                found = true;
            }
        }
        if ( !found ) {
            el.value = "GLOBAL";
        }
        cell.appendChild(el);
    } else {
        var found = false;
        for (var i=0; i<g_zones.length; i++) {
            var d = g_zones[i];
            if ( d.zone_id == p.zone_id ) {
                cell.innerHTML = escapeHTML(d.description);
                found = true;
            }
        }
        if ( !found ) {
            cell.innerHTML = "GLOBAL";
        }
    }

    cell = row.insertCell(4);
    cell.style.textAlign = "center";

    if (true) {
        cell.innerHTML =
            "<A href='#' onclick=\"remove_onclick(" + nRow + ",'" + rule_id + "',this);return false;\" title='Remove rule from rule list'><IMG border='0' src='images/remove.png'></A>" +
            (isNew ? "":"&nbsp;<A href='#' onclick=\"edit_onclick(" + nRow + ",'" + rule_id + "',this);return false;\" title='Edit this rule'><IMG border='0' src='images/edit.png'></A>") +
            (isNew ? "":"&nbsp;<A href='#' onclick=\"undo_onclick(" + nRow + ",'" + rule_id + "',this);return false;\" title='Undo changes or undelete this rule'><IMG border='0' src='images/undo.png'></A>");
    }

    if (isNew) g_nRow++;
    return nRow;
}

function addnewrule_onclick(p, nRow) {
    return addrule(-1, true);
}

function cancel_onclick() {
    document.location.href = "./rules.php";
}

function save_onclick() {
    // TODO:  Validation!
    for (var i=0; i<g_nRow; i++) {
        var objrule = document.getElementById("rule" + i);
        if (objrule == null) continue; 
        if (objrule.value.length < 1)
        {
            alert("You must enter at least one rule for each notification.\r\n\r\nDelete rows you do not want to save.");
            objrule.focus();
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

function edit_onclick(nRow, tprule_id, objHref) {
    var tbl = document.getElementById("tblRules");
    var r = document.getElementById("tr" + nRow);

    tbl.deleteRow(nRow + 1);
    addrule(nRow, true);

    // If this player was on the delete list, take him off now
    g_delete = g_delete.replace("," + tprule_id, "");
}

function undo_onclick(nRow, tprule_id, objHref) {
    var tbl = document.getElementById("tblRules");
    var r = document.getElementById("tr" + nRow);

    tbl.deleteRow(nRow + 1);
    addrule(nRow, false);

    // If this player was on the delete list, take him off now
    g_delete = g_delete.replace("," + tprule_id, "");
}

function remove_onclick(nRow, tprule_id, objHref) {
    var tbl = document.getElementById("tblRules");
    var r = document.getElementById("tr" + nRow);

    // If this was a manually added row, remove it now
    if (tprule_id == "0") {
        tbl.deleteRow(r.rowIndex);
    } else {
        tbl.deleteRow(nRow + 1);
        addrule(nRow, false);

        r = document.getElementById("tr" + nRow);

        var cls = r.className;
        if (cls.indexOf("deleteRow") < 0) cls += " deleteRow";
        r.className = cls;

        // Make sure this rule is only in the delete list one time
        g_delete = g_delete.replace("," + tprule_id, "");
        g_delete += "," + tprule_id;
    }
}

function body_onload() {
    for (var i=0; i<g_rules.length; i++)
    {
        addrule(i, false);
    }
    g_nRow = g_rules.length + 1;
}

</script> 
