<?php session_start() ?>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
    <head>
        <title></title>
        <meta name="generator" content="Bluefish 2.0.2" >
        <meta name="author" content="John Gorkos" >
        <meta name="date" content="2011-04-27T19:32:30-0400" >
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
        <table width="700" style='cell:solid 1px black'>
            <tr>
                <td>
                <?php if ( !empty($_SESSION['error'] ) ) {
                    echo "<table border=\"1\"><TR><TD>";
                    print $_SESSION['error']."<br>";
                    echo "</TD></TR></TABLE>\n";
                    $_SESSION['error'] ="";
                } 
                ?>
                  Sign In<br>
                    <form action="check_login.php" method="post">
                        Username:
                        <input type="text" name="username" value="<?php 
                            if (isset($_SESSION['user_name'] )) {
                                echo $_SESSION['user_name'];
                                session_unregister("user_name");
                            } elseif (isset($_POST['username'])) { echo $username; } 
                            ?>" /><br>
                        Password:  
                        <input type="password" name="password" /><br>
                        <input type="submit" name="submit" value="Log In" />
                    </form>
                    <a href="forgot_password.php">Forgot your password?</a>
                </td>
                <td align="center" valign="center">
                    If you don't have a login to APRS-Alert, you can<br>
                    <a href="register.php">
                    <img title='Generated button' src='register_f.png' onmouseover='javascript:this.src="register_b.png"' onmouseout='javascript:this.src="register_f.png"' /></a>
                </td>
            </tr>
        </table>
        </div>
        <div id="footer"</div><?php include "footer.php"?></div>
    </body>
</html>
