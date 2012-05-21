<?php 
session_start();
include "db_setup.php";
if ( isset($_POST['mypassword1']) ) {
    $id = $_POST['ID'];
    $mypassword1 = $_POST['mypassword1'];
    $mypassword2 = $_POST['mypassword2'];
    $myusername1 = $_SESSION['username1'];
    if ( strlen($mypassword1) < 3 ) {
        session_register("username1");
        $_SESSION['username1'] = $myusername;
        session_register("error");
        $_SESSION['error'] .= "The password needs to be longer than 3 characters.  Really.";
        header("location:forgot_password.php?ID=$id");
        exit;
    }

    if ( $mypassword1 != $mypassword2 ) {
        session_register("username1");
        $_SESSION['username1'] = $myusername1;
        session_register("error");
        $_SESSION['error'] .= "The passwords need to match";
        header("location:forgot_password.php?ID=$id");
        exit;
    }
    
    if ( strlen($mypassword1) < 3 ) {
        session_register("username1");
        $_SESSION['username1'] = $myusername1;
        session_register("error");
        $_SESSION['error'] .= "The password needs to be longer than 3 characters.  Really.";
        header("location:forgot_password.php?ID=$id");
        exit;
    }
    $conn_string = "host=$host port=5432 dbname=$db_name user=$db_user password=$db_pass options='--client_encoding=UTF8'";
    $dbconn = pg_connect($conn_string) or die( "Unable to connect to database");
    $sql = "DELETE from forgotten_password where hash ='".$_POST['ID']."'";
    $result=pg_exec($sql);
    $sql = "UPDATE users set password = md5('".$mypassword1."') where username=upper('".$myusername1."')";
    $result=pg_exec($sql);
    pg_close($dbconn);
    session_unregister("error");
    header("location:login.php");
    exit;
}
?>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
    <head>
        <title>APRS-Alert Password Reset</title>
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
    </head>
    <body onload='body_onload()'>
        <div id="header"></div>
        <div id="menu"</div><?php include "menu.php"?></div>
        <div id="container">
    <?php
    if ( isset($_POST['forgotten_username']) ) { 
        $conn_string = "host=$host port=5432 dbname=$db_name user=$db_user password=$db_pass options='--client_encoding=UTF8'";
        $dbconn = pg_connect($conn_string) or die( "Unable to connect to database");
        $sql = "SELECT email from users where username = upper('".$_POST['forgotten_username']."')";
        $result=pg_exec($sql);
        $count=pg_numrows($result);
        if ( $count > 0 ) {
            $row = pg_fetch_assoc($result, 0);
            $email_address = $row['email'];
            $hash = md5(mt_rand());
            $subject = "APRS-Alert Password Reset!";
            $body = "Hi,\n\nWe have received your password reset request for APRS-Alert.net, and are sending this email\n";
            $body .="to your contact email address so you can set up a new password.   Please either click the following link or\n";
            $body .="paste it into a browser window to complete the password reset process.\n";
            $body .="http://www.aprs-alert.net/forgot_password.php?ID=$hash\n\n";
            $body .=" de John, AB0OO\n";
            $headers = "From: alert@aprs-alert.net\r\n" .
                "X-Mailer: php";
            if (mail($email_address, $subject, $body, $headers)) {
                $sql="INSERT INTO forgotten_password (hash, username ) values ( ".
                "'".$hash."', upper('".$_POST['forgotten_username']."'))";
                $result=pg_exec($sql);
            }
            echo "<h2>On the way!</h2>";
            echo "<p>We've sent an email to your contact email address.";
        } else {
            echo "<h2>Oh Snap!</h2>";
            echo "<p>We can't seem to find an account that matches that username.<br>";
            unset($_POST['forgot_password']);
            echo "<a href=\"forgot_password.php\">Try again?</a>";
        
        } 
    } elseif ( isset($_GET['ID']) ) {
        $conn_string = "host=$host port=5432 dbname=$db_name user=$db_user password=$db_pass options='--client_encoding=UTF8'";
        $dbconn = pg_connect($conn_string) or die( "Unable to connect to database");
        $sql = "SELECT username from forgotten_password where hash ='".$_GET['ID']."'";
        $result=pg_exec($sql);
        $count=pg_numrows($result);
        if ( $count == 1 ) {
            $row = pg_fetch_assoc($result, 0);
            session_register("username1");
            $_SESSION['username1'] = $row['username'];
            echo "<h2>Welcome back, ".$row['username']."!</h2>";
            if ( isset($_GET['ERR']) ) {
                echo "<TABLE border=\"1\"><TR><TD>".$_GET['ERR']."</TD><TR></TABLE><br>";
            }
?>
        <p>Please use this form to create a new password for future logins to APRS-Alert</p>
        <?php if ( isset($_SESSION['error']) ) {
            echo "<TABLE border=\"1\"><TR><TD>".$_SESSION['error']."</TD></TR></TABLE><BR>";
        } ?>
        <form action="forgot_password.php" method="post" style='margin: 0px; padding: 0px;'>
        <p>Enter your new password here: <input type="password" name="mypassword1"/></p>
        <p>Enter it again: <input type="password" name="mypassword2"/></p>
        <input type="hidden" name="ID" value="<?php echo $_GET['ID']?>">
        <input type="submit" name="submit" value="Reset Password" /><br>
        <?php
        }
    } else { ?>
        <h2>Forgotten Password</h2>
        <p>Nothing is more frustrating than taking the time and making the effort to come up with a 
        great password, only to completely forget what it was.  It happens to the best of us.
        Unfortunately, we can't tell you what that great password is.  As soon as you gave it to us,
        we mangled it beyond recognition.  To log in, we take whatever you give us, mangle it the same
        way, and see if the two mangled versions match.</p>
        <p>To fix this, please give us your username here, and we'll take it from there.  We'll send you
        an encoded link to your contact email address, and when you follow that link back to our site,
        you'll get the chance to set a new (more memorable) password.</p>
        <FORM action="forgot_password.php" method="post" style='margin: 0px; padding: 0px;'> 
        Enter your username here:<input type="text" name="forgotten_username"/>&nbsp
        <input type="submit" name="submit" value="Help Me!" /><br>
        </FORM>
    <?php }
    ?>
        </div>
        <div id="footer"</div><?php include "footer.php"?></div>
    </body>
</html>
