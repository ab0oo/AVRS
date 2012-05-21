<?php    
$debug = false;
if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $conn_string = "host=$host port=5432 dbname=$db_name user=$db_user password=$db_pass options='--client_encoding=UTF8'";
    $dbconn = pg_connect($conn_string) or die( "Unable to connect to database");
    if ( $debug ) echo "<PRE>\n";
    if ( $debug ) print_r($_POST);
    if ( $debug ) echo "</PRE>\n";
    foreach ( $_POST as $key => $value ) {
        //echo "Testing $key<br>";
        if ( substr($key,0,4) != "n_id" ) continue;
        preg_match('/.+(\d+)$/', $key, $idarr);
        $id = $idarr[1];
        //echo "Processing $id for changes<br>";
        $userid = pg_escape_string(stripslashes($_SESSION['userid']));
        $n_id = pg_escape_string(stripslashes($_POST['n_id'.$id]));
        $na_id = pg_escape_string(stripslashes($_POST['na_id'.$id]));
        $rule_id = pg_escape_string(stripslashes($_POST['r_id'.$id]));
        $start_time = pg_escape_string(stripslashes($_POST['starthour'.$id])) * 60 + pg_escape_string(stripslashes($_POST['startmin'.$id]));
        $end_time = pg_escape_string(stripslashes($_POST['endhour'.$id])) * 60 + pg_escape_string(stripslashes($_POST['endmin'.$id]));
        if ( $end_time == 0 ) $end_time = 1440;
        $valid_days = 0;
        if ( !empty($_POST['valid_Su'.$id]) ) $valid_days+=1;
        if ( !empty($_POST['valid_Mo'.$id]) ) $valid_days+=2;
        if ( !empty($_POST['valid_Tu'.$id]) ) $valid_days+=4;
        if ( !empty($_POST['valid_We'.$id]) ) $valid_days+=8;
        if ( !empty($_POST['valid_Th'.$id]) ) $valid_days+=16;
        if ( !empty($_POST['valid_Fr'.$id]) ) $valid_days+=32;
        if ( !empty($_POST['valid_Sa'.$id]) ) $valid_days+=64;
        if ( $value == '0' ) { // this is an insert of a new record
            $sql = "INSERT INTO notifications (user_id,start_time,end_time,valid_days,rule_id,na_id) VALUES ("
                .$userid.",".$start_time.",".$end_time.",".$valid_days.",".$rule_id.",".$na_id.")";
            if ( $debug ) echo $sql."<br>";
            $result = pg_query($dbconn,$sql);
            if ( $debug ) echo "Result of execute is $result<br>";
        } else { // this is an update to an existing record
            $sql = "UPDATE notifications set start_time=".$start_time.
                ", end_time=".$end_time.", valid_days=".$valid_days.",rule_id=".$rule_id.",na_id=".$na_id." where n_id=".$n_id;
            if ( $debug ) echo $sql."<br>";
            $result = pg_query($dbconn,$sql);
            if ( $debug ) echo "Result of execute is $result<br>";
        }
    }
    $deletions = explode(",", $_POST['delete']);
    foreach ( $deletions as $n_id ) {
        if ( $n_id == "" ) { continue; }
        if ( $debug ) echo "I'll be deleting rule_id $n_id<br>";
        $sql = "DELETE FROM notifications WHERE n_id=".$n_id." AND user_id=".$_SESSION['userid'];
        $result = pg_exec($sql);
        if ( $debug) echo "$sql<br>$result<br>";
    }
    pg_close($dbconn);
}
?>
<script language='javascript' type='application/javascript'> 
var g_nRow = 0;
var g_delete = "";
var g_notifications = new Array();
var g_addresses = new Array();
var g_rules = new Array();
var days = new Array("Su", "Mo", "Tu", "We", "Th", "Fr", "Sa");

<?php
$conn_string = "host=$host port=5432 dbname=$db_name user=$db_user password=$db_pass options='--client_encoding=UTF8'";
$dbconn = pg_connect($conn_string) or die( "Unable to connect to database");
if ( isset($_SESSION['userid']) && $_SESSION['userid'] != NULL ) {
    //$sql="select n_id, user_id, to_char(interval '00:00' + start_time* interval '1 minute', 'HH24:MI') as start_time, to_char(interval '00:00' + end_time* interval '1 minute', 'HH24:MI') as end_time, valid_days, rule_id as rule_id, na_id from notifications r where r.user_id=".$_SESSION['userid']." order by rule_id";
    $sql="select n_id, user_id, start_time, end_time, valid_days, rule_id as rule_id, na_id from notifications r where r.user_id=".$_SESSION['userid']." order by rule_id";
    $result=pg_exec($sql);
    $numrows=pg_numrows($result);
    for($ri = 0; $ri < $numrows; $ri++) {
        $row = pg_fetch_array($result,$ri);
        $days = $row['valid_days'];
        $su = ( ($days & 1) == 1 ? 1 : 0 );
        $mo = ( ($days & 2) == 2 ? 1 : 0 );
        $tu = ( ($days & 4) == 4 ? 1 : 0 );
        $we = ( ($days & 8) == 8 ? 1 : 0 );
        $th = ( ($days & 16) == 16 ? 1 : 0 );
        $fr = ( ($days & 32) == 32 ? 1 : 0 );
        $sa = ( ($days & 64) == 64 ? 1 : 0 );
        echo "g_notifications[g_notifications.length] = {
            \"n_id\":\"".$row['n_id']."\",
                \"user_id\":\"".$row['user_id']."\",
                \"na_id\":\"".$row['na_id']."\",
                \"start_time\":\"".$row['start_time']."\",
                \"end_time\":\"".$row['end_time']."\",
                \"valid\": new Array( ".$su.",".$mo.",".$tu.",".$we.",".$th.",".$fr.",".$sa." ),
                \"rule_id\":\"".$row['rule_id']."\"};\n";
    }

    $sql="select r.rule_id as rule_id, s.callsign,r.ruletype,z.description,r.cycle_time from rules r left outer join zones z on (r.zone_id=z.zone_id), monitored_stations s where r.user_id=".$_SESSION['userid']." and s.station_id=r.station_id order by rule_id";
    $result=pg_exec($sql);
    $numrows=pg_numrows($result);
    for($ri = 0; $ri < $numrows; $ri++) {
        $row = pg_fetch_array($result,$ri);
        $description = $row['callsign']." ".$row['ruletype']." ".$row['description'];
        echo "g_rules[g_rules.length] = { \"rule_id\":\"".$row['rule_id']."\", \"description\":\"".$description."\"};\n";
    }

    $sql="select na_id, description from notification_addresses where user_id=".$_SESSION['userid']." order by na_id";
    $result=pg_exec($sql);
    $numrows=pg_numrows($result);
    for($ri = 0; $ri < $numrows; $ri++) {
        $row = pg_fetch_array($result,$ri);
        echo "g_addresses[g_addresses.length] = { \"na_id\":\"".$row['na_id']."\", \"description\":\"".$row['description']."\"};\n";
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
    var isNew = (pndx < 0  ||  pndx >= g_notifications.length);

    var p = (isNew ? new Object():g_notifications[pndx]);

    if (isNew) {
        p.n_id = 0;
        p.na_id = 0;
        p.rule_id = 0;
        p.zone_id = 0;
        p.starthour = 0;
        p.startmin = 0;
        p.endhour = 24;
        p.endmin = 0;
        p.valid = new Array( 1,1,1,1,1,1,1);
    }

    var n_id = p.n_id;
    var rule_id = p.rule_id;

    var rowIndex = (pndx < 0 ? -1:pndx + 1);

    var tbl = document.getElementById("tblNotifications");
    var row = tbl.insertRow(rowIndex);

    var nRow = (isNew ? g_nRow:pndx);

    row.id = "tr" + (isNew ? g_nRow:pndx);
    row.pndx = pndx;
    row.rule_id = p.rule_id;

    row.className = (row.rowIndex % 2 == 1 ? "even":"odd");
    var cell = row.insertCell(0);

    if (allowEdit) {
        var el = document.createElement("select");
        el.name = "r_id" + nRow;
        el.id = "r_id" + nRow;
        el.value = p.n_id;
        el.style.width = "100%";
        for (var i=0; i<g_rules.length; i++) {
            var thisOne = false;
            if ( g_rules[i].rule_id == rule_id ) { thisOne = true; }
            var desc = g_rules[i].description;
            el.options[i] = new Option(desc,g_rules[i].rule_id, thisOne, thisOne);
        }
        cell.appendChild(el);

        el = document.createElement("input");
        el.type = "hidden";
        el.name = "rule_id" + nRow;
        el.id = el.name;
        el.value = p.rule_id;
        cell.appendChild(el);

        el = document.createElement("input");
        el.type = "hidden";
        el.name = "n_id" + nRow;
        el.id = p.n_id;
        el.value = p.n_id;
        cell.appendChild(el);

    } else {
        for (var i=0; i<g_rules.length; i++) {
            var d = g_rules[i];
            if ( d.rule_id == p.rule_id ) {
                cell.innerHTML = d.description;
            }
        }
        cell.style.textAlign = "center";
    }

    cell = row.insertCell(1);
    if (allowEdit) {
        el = document.createElement("select");
        el.name = "starthour" + nRow;
        el.id = "starthour" + nRow;
        el.value = 0;
        var sh = Math.floor(p.start_time / 60 );
        for (var i=0; i<24; i++) {
            var thisOne = false;
            if ( i == sh ) { thisOne = true; }
            el.options[i] = new Option(i,i,thisOne,thisOne);
        }
        cell.appendChild(el);
        cell.appendChild(document.createTextNode(":"));
        el = document.createElement("select");
        el.name = "startmin" + nRow;
        el.id = "startmin" + nRow;
        el.value = 0;
        var sm = Math.floor(p.start_time % 60 );
        for (var i=0; i<60; i++) {
            var thisOne = false;
            if ( i == sm ) { thisOne = true; }
            el.options[i] = new Option(i,i,thisOne,thisOne);
        }
        cell.appendChild(el);

    } else {
        var sh_txt = "";
        var sm_txt = "";
        var sh = Math.floor(p.start_time / 60 );
        if ( sh < 10 ) { sh_txt = "0"+sh; } else { sh_txt = sh; }
        var sm = Math.floor(p.start_time % 60 );
        if ( sm < 10 ) { sm_txt = "0"+sm; } else { sm_txt = sm; }
        cell.innerHTML = escapeHTML(sh_txt+":"+sm_txt);
    }

    cell = row.insertCell(2);
    if (allowEdit) {
        el = document.createElement("select");
        el.name = "endhour" + nRow;
        el.id = "endhour" + nRow;
        el.value = 0;
        var eh = Math.floor(p.end_time / 60 );
        for (var i=0; i<24; i++) {
            var thisOne = false;
            if ( i == eh ) { thisOne = true; }
            el.options[i] = new Option(i,i,thisOne,thisOne);
        }
        cell.appendChild(el);
        cell.appendChild(document.createTextNode(":"));
        el = document.createElement("select");
        el.name = "endmin" + nRow;
        el.id = "endmin" + nRow;
        el.value = 0;
        var em = Math.floor(p.end_time % 60 );
        for (var i=0; i<60; i++) {
            var thisOne = false;
            if ( i == em ) { thisOne = true; }
            el.options[i] = new Option(i,i,thisOne,thisOne);
        }
        cell.appendChild(el);
    } else {
        var eh_txt = "";
        var em_txt = "";
        var eh = Math.floor(p.end_time / 60 );
        if ( eh < 10 ) { eh_txt = "0"+eh; } else { eh_txt = eh; }
        var em = Math.floor(p.end_time % 60 );
        if ( em < 10 ) { em_txt = "0"+em; } else { em_txt = em; }
        cell.innerHTML = escapeHTML(eh_txt+":"+em_txt);
    }

    cell = row.insertCell(3);
    if (allowEdit) {
        // Sunday
        for ( var i = 0; i < days.length; i++ ) {
            el = document.createElement("input");
            el.type="checkbox";
            el.name = "valid_"+days[i] + nRow;
            el.id = "valid_"+days[i] + nRow;
            el.checked =  ( p.valid[i] == 1 ? true : false ) ;
            cell.appendChild(el);
            cell.appendChild(document.createTextNode(days[i]));
            if ( i == 1 || i == 3 || i ==5 ) {
                cell.appendChild(document.createElement("br"));
            }
        }
    } else {
        for ( var i =0; i<days.length; i++) {
            var el = document.createTextNode(days[i]);
            if ( p.valid[i] == 1 ){
                cell.appendChild(document.createTextNode(days[i]));
                cell.appendChild(document.createTextNode(" "));
            }
        }
    }

    cell = row.insertCell(4);
    if (allowEdit) {
        var el = document.createElement("select");
        el.name = "na_id" + nRow;
        el.id = "na_id" + nRow;
        el.value = p.na_id;
        el.style.width = "100%";
        for (var i=0; i<g_addresses.length; i++) {
            var thisOne = false;
            if ( g_addresses[i].na_id == p.na_id ) { thisOne = true; }
            var desc = g_addresses[i].description+"["+g_addresses[i].na_id+"]";
            el.options[i] = new Option(g_addresses[i].description, g_addresses[i].na_id,thisOne,thisOne );
        }
        cell.appendChild(el);
    } else {
        for (var i=0; i<g_addresses.length; i++) {
            var d = g_addresses[i];
            if ( d.na_id == p.na_id ) {
                cell.innerHTML = d.description;
            }
        }
    }

    cell = row.insertCell(5);
    cell.style.textAlign = "center";

    if (true) {
        cell.innerHTML =
            "<A href='#' onclick=\"remove_onclick(" + nRow + ",'" + n_id + "',this);return false;\" title='Remove rule from rule list'><IMG border='0' src='images/remove.png'></A>" +
            (isNew ? "":"&nbsp;<A href='#' onclick=\"edit_onclick(" + nRow + ",'" + n_id + "',this);return false;\" title='Edit this rule'><IMG border='0' src='images/edit.png'></A>") +
            (isNew ? "":"&nbsp;<A href='#' onclick=\"undo_onclick(" + nRow + ",'" + n_id + "',this);return false;\" title='Undo changes or undelete this rule'><IMG border='0' src='images/undo.png'></A>");
    }

    if (isNew) g_nRow++;
    return nRow;
}

function addnewrule_onclick(p, nRow) {
    return addrule(-1, true);
}

function cancel_onclick() {
    document.location.href = "./notifications.php";
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
    var tbl = document.getElementById("tblNotifications");
    var r = document.getElementById("tr" + nRow);

    tbl.deleteRow(nRow + 1);
    addrule(nRow, true);

    // If this player was on the delete list, take him off now
    g_delete = g_delete.replace("," + tprule_id, "");
}

function undo_onclick(nRow, tprule_id, objHref) {
    var tbl = document.getElementById("tblNotifications");
    var r = document.getElementById("tr" + nRow);

    tbl.deleteRow(nRow + 1);
    addrule(nRow, false);

    // If this player was on the delete list, take him off now
    g_delete = g_delete.replace("," + tprule_id, "");
}

function remove_onclick(nRow, tprule_id, objHref) {
    var tbl = document.getElementById("tblNotifications");
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
    for (var i=0; i<g_notifications.length; i++)
    {
        addrule(i, false);
    }
    g_nRow = g_notifications.length + 1;
}

</script> 
