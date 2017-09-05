<?php

/*

stdClass Object
(
    [uid] =&gt; 6
    [username] =&gt; brandon
    [profile_pic] =&gt; http://media.superfanu.com/profiles/6.1376355495.png
    [profile_pic_cropped] =&gt; http://media.superfanu.com/profiles/6.1376355495_c100.png
    [email] =&gt; brandon@karmaprogressive.com
    [name] =&gt; Brandon Lawrence
    [phonepretty] =&gt; (502) 555-1213
    [address1] =&gt; 962 Baxter Ave.
    [city] =&gt; Louisville
    [state] =&gt; KY
    [zip] =&gt; 40207
    [meta] =&gt; stdClass Object
        (
            [1] =&gt; stdClass Object
                (
                    [label] =&gt; Having_Fun?
                    [tf] =&gt; Yes
                )

            [2] =&gt; stdClass Object
                (
                    [label] =&gt; Student_ID#
                    [tf] =&gt; 1512345
                )

            [3] =&gt; stdClass Object
                (
                    [label] =&gt; User_Type
                    [tf] =&gt; Student
                )

        )

)

*/

	$ch = curl_init( $_POST['url'] );     
	curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
	curl_setopt($ch, CURLOPT_HEADER, false);
	curl_setopt($ch, CURLOPT_ENCODING, "");
	curl_setopt($ch, CURLOPT_USERAGENT, "SuperFanU");
	curl_setopt($ch, CURLOPT_AUTOREFERER, 60);
	curl_setopt($ch, CURLOPT_CONNECTTIMEOUT, 60);
	curl_setopt($ch, CURLOPT_MAXREDIRS, 10);
	curl_setopt($ch, CURLOPT_POST, true);
	curl_setopt($ch, CURLOPT_POSTFIELDS, $_POST);
	
	$returnText	= curl_exec( $ch );
	curl_close( $ch );
	
	$profile = json_decode($returnText);
	//print_r($profile);

		echo '<table class="styled profile-edit-link" border="0">';
		if($profile) {
			echo '<tr class="link"><td><img class="user-pic user-pic-self" src="'.$profile->profile_pic_cropped.'" /><h3><a href="#profile-edit">'.$profile->username.'</a></h3><p class="description"><a href="#profile-edit">'.$profile->email.'</a></p></td></tr>';
		}
		echo '<tr class="link"><td><h4><a href="#notifications">Notifications</a></h4></td></tr>';
		echo '</table>';
		
		if($profile->services) {
			echo '<table class="styled" border="0">';
			foreach($profile->services as $p) {
				if($_POST['platform'] == 'Android') {echo '<tr class="link"><td><h4><a href="startwhamcity://">Light Show</a></h4></td></tr>';}
			}
			echo '</table>';
		}
		
		
		?>
		
		<table class="styled" border="0">
			<tr class="link"><td><h4><a href="#promo">Bonus Points</a></h4></td></tr>
			<tr class="link"><td><h4><a href="#invite">Invite your Friends</a></h4></td></tr>
		</table>
		
		<table class="styled" border="0">
			<tr class="link"><td><h4><a href="#settings">Settings</a></h4></td></tr>
			<!-- <tr class="link"><td><h4><a href="#myqr-code">MyQR Code</a></h4></td></tr> -->
			<?php if($profile->is_admin <> 0) {echo '<tr class="link"><td><h4><a href="#" class="link-track admin-link" data-url="">Admin</a></h4></td></tr>';} ?>
		</table>
		
		<table class="styled" border="0">
			<tr class="link"><td><h4><a href="#help-feedback">Help / Feedback</a></h4></td></tr>
		</table>
		
<!--
		<table class="styled" border="0">
			<tr class="link"><td><h4><a href="http://superfanu.com/mobile/tos" rel="external">Terms of Use</a></h4></td></tr>
			<tr class="link"><td><h4><a href="http://superfanu.com/mobile/privacy" rel="external">Privacy Policy</a></h4></td></tr>
		</table>
-->
		
<!--
		<table class="styled" border="0">
			<tr class="link"><td><h4><a href="#login" class="logoutBtn">Logout</a></h4></td></tr>
		</table>
-->