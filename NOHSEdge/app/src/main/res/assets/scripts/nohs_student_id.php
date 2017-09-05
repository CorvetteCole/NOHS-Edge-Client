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
	
	$json = json_decode($returnText);
	//print_r($profile);

		echo '<div id="nohs-student-id">';

		if( $json->error ) {
			
			echo '<p class="error">'.$json->error.'</p>';
			echo '<p id="refresh-sports-pass">Tap to try again</p>';
			
		} else {
			
			
			echo '<img style="border: 5px solid '.$json->color.'" class="profile-pic" src="'.$json->img.'">';
			echo '<h2>'.$json->name.'</h2>';
			echo '<p class="type" style="background-color: '.$json->color.'; color: '.$json->text_color.'">'.$json->type.'</p>';
			echo '<img class="qrcode" src="'.$json->qrcode.'">';
			echo '<p class="wnumber">'.$json->sid.'</p>';
			
			
		}
		
		echo '</div>';
		?>
