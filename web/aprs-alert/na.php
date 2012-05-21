<?php    
if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $conn_string = "host=$host port=5432 dbname=$db_name user=$db_user password=$db_pass options='--client_encoding=UTF8'";
    $dbconn = pg_connect($conn_string) or die( "Unable to connect to database");
    foreach ( $_POST as $key => $value ) {
        //echo "Testing $key<br>";
        if ( substr($key,0,5) != "na_id" ) continue;
        preg_match('/.+(\d+)$/', $key, $idarr);
        $id = $idarr[1];
        //echo "Processing $id for changes<br>";
        $userid = pg_escape_string(stripslashes($_SESSION['userid']));
        $address = pg_escape_string(stripslashes($_POST['address'.$id]));
        $description = pg_escape_string(stripslashes($_POST['description'.$id]));
        $short_form = pg_escape_string(stripslashes($_POST['short_form'.$id]));
        $na_id = pg_escape_string(stripslashes($_POST['na_id'.$id]));
        if ( $value == '0' ) { // this is an insert of a new record
            $sql = "INSERT INTO notification_addresses (user_id,address,description,short_form) VALUES (".
                $userid.",'".$address."','".$description."',".$short_form.")";
            //echo $sql."<br>";
            $result = pg_query($dbconn,$sql);
            //echo "Result of execute is $result<br>";
        } else { // this is an update to an existing record
            $sql = "UPDATE notification_addresses set address='".$address.
                "', description='".$description."', short_form=".$short_form." where na_id=".$na_id;
            //echo $sql."<br>";
            $result = pg_query($dbconn,$sql);
            //echo "Result of execute is $result<br>";
        }
    }
    $deletions = explode(",", $_POST['delete']);
    foreach ( $deletions as $na_id ) {
        if ( $na_id == "" ) { continue; }
            //echo "I'll be deleting na_id $na_id<br>";
            $sql = "DELETE FROM notification_addresses where user_id=".$_SESSION['userid']." and na_id=".$na_id;
        $result = pg_query($dbconn,$sql);
        //echo $sql."<br>";
    }
    pg_close($dbconn);
}
?>
<script language='javascript' type='application/javascript'> 
var g_nRow = 0;
var g_delete = "";
var g_addresses = new Array();

<?php
$conn_string = "host=$host port=5432 dbname=$db_name user=$db_user password=$db_pass options='--client_encoding=UTF8'";
$dbconn = pg_connect($conn_string) or die( "Unable to connect to database");
if ( isset($_SESSION['userid'] ) && $_SESSION['userid'] != NULL ) {
    $sql="SELECT * FROM notification_addresses where user_id=".$_SESSION['userid']." order by na_id";
    $result=pg_exec($sql);
    $numrows=pg_numrows($result);
    for($ri = 0; $ri < $numrows; $ri++) {
        $row = pg_fetch_array($result,$ri);
        echo "g_addresses[g_addresses.length] = {
            \"na_id\":\"".$row['na_id']."\",
                \"address\":\"".$row['address']."\",
                \"description\":\"".$row['description']."\",
                \"short_form\":\"".$row['short_form']."\"};";
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

function addaddress(pndx, allowEdit) {
    var isNew = (pndx < 0  ||  pndx >= g_addresses.length);

    var p = (isNew ? new Object():g_addresses[pndx]);

    if (isNew) {
        p.address = "";
        p.na_id = 0;
        p.user_id = 0;
        p.description = "";
        p.short_form = 1;
    }

    var na_id = p.na_id;

    var rowIndex = (pndx < 0 ? -1:pndx + 1);

    var tbl = document.getElementById("tblAddresses");
    var row = tbl.insertRow(rowIndex);

    var nRow = (isNew ? g_nRow:pndx);

    row.id = "tr" + (isNew ? g_nRow:pndx);
    row.pndx = pndx;
    row.na_id = na_id;

    row.className = (row.rowIndex % 2 == 1 ? "even":"odd");
    var cell = row.insertCell(0);

    if (allowEdit) {
        var el = document.createElement("input");
        el.type = "text";
        el.name = "address" + nRow;
        el.id = el.name;
        el.size = 30;
        el.maxLength = 50;
        el.value = p.address;
        el.style.textAlign = "center";
        cell.appendChild(el);

        el.focus();

        el = document.createElement("input");
        el.type = "hidden";
        el.name = "na_id" + nRow;
        el.id = el.name;
        el.value = p.na_id;
        cell.appendChild(el);
    } else {
        cell.innerHTML = p.address;
        cell.style.textAlign = "center";
    }

    cell = row.insertCell(1);
    if (allowEdit) {
        el = document.createElement("input");
        el.type = "text";
        el.name = "description" + nRow;
        el.id = "description" + nRow;
        el.size = 25;
        el.maxLength = 25;
        el.value = p.description;
        cell.appendChild(el);
    } else {
        cell.innerHTML = escapeHTML(p.description);
    }


    cell = row.insertCell(2);
    if (allowEdit) {
        sel = document.createElement('select');
        sel.style.width = "50%";
        sel.name = "short_form" + nRow;
        sel.id = "short_form" + nRow;
        sel.options[0] = new Option('Yes', 'true', p.short_form == 't', p.short_form == 't');
        sel.options[1] = new Option('No', 'false', p.short_form == 'f', p.short_form == 'f');
        cell.appendChild(sel);
    } else {
        var shortform = p.short_form;
        //if (shortform == "t") shortform = "Yes";
        shortform = "Yes";
        if (shortform == "f") shortform = "No";

        cell.innerHTML = shortform;
        cell.style.textAlign = "center";
    }

    cell = row.insertCell(3);
    cell.style.textAlign = "center";

    if (true) {
        cell.innerHTML =
            "<A href='#' onclick=\"remove_onclick(" + nRow + ",'" + na_id + "',this);return false;\" title='Remove address from address list'><IMG border='0' src='images/remove.png'></A>" +
            (isNew ? "":"&nbsp;<A href='#' onclick=\"edit_onclick(" + nRow + ",'" + na_id + "',this);return false;\" title='Edit this address'><IMG border='0' src='images/edit.png'></A>") +
            (isNew ? "":"&nbsp;<A href='#' onclick=\"undo_onclick(" + nRow + ",'" + na_id + "',this);return false;\" title='Undo changes or undelete this address'><IMG border='0' src='images/undo.png'></A>");
    }

    if (isNew) g_nRow++;
    return nRow;
}

function addnewaddress_onclick(p, nRow) {
    return addaddress(-1, true);
}

function cancel_onclick() {
    document.location.href = "notification_addresses.php";
}

function save_onclick() {
    // TODO:  Validation!
    for (var i=0; i<g_nRow; i++)
    {
        var objaddress = document.getElementById("address" + i);
        if (objaddress == null) continue; 
        if (objaddress.value.length < 1)
        {
            alert("You must enter at least an address for each notification.\r\n\r\nDelete rows you do not want to save.");
            objaddress.focus();
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

function edit_onclick(nRow, tpna_id, objHref) {
    var tbl = document.getElementById("tblAddresses");
    var r = document.getElementById("tr" + nRow);

    tbl.deleteRow(nRow + 1);
    addaddress(nRow, true);

    // If this player was on the delete list, take him off now
    g_delete = g_delete.replace("," + tpna_id, "");
}

function undo_onclick(nRow, tpna_id, objHref) {
    var tbl = document.getElementById("tblAddresses");
    var r = document.getElementById("tr" + nRow);

    tbl.deleteRow(nRow + 1);
    addaddress(nRow, false);

    // If this player was on the delete list, take him off now
    g_delete = g_delete.replace("," + tpna_id, "");
}

function remove_onclick(nRow, tpna_id, objHref) {
    var tbl = document.getElementById("tblAddresses");
    var r = document.getElementById("tr" + nRow);

    // If this was a manually added row, remove it now
    if (tpna_id == "0") {
        tbl.deleteRow(r.rowIndex);
    } else {
        tbl.deleteRow(nRow + 1);
        addaddress(nRow, false);

        r = document.getElementById("tr" + nRow);

        var cls = r.className;
        if (cls.indexOf("deleteRow") < 0) cls += " deleteRow";
        r.className = cls;

        // Make sure this address is only in the delete list one time
        g_delete = g_delete.replace("," + tpna_id, "");
        g_delete += "," + tpna_id;
    }
}

function body_onload() {
    for (var i=0; i<g_addresses.length; i++)
    {
        addaddress(i, false);
    }
    g_nRow = g_addresses.length + 1;
}

</script> 
