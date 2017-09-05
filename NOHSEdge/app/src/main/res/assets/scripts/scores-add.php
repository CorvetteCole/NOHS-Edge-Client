<?php

	$ch = curl_init( $_POST['url'] );     
	curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
	curl_setopt($ch, CURLOPT_HEADER, false);
	curl_setopt($ch, CURLOPT_ENCODING, "");
	curl_setopt($ch, CURLOPT_USERAGENT, "SuperFanU");
	curl_setopt($ch, CURLOPT_AUTOREFERER, 60);
	curl_setopt($ch, CURLOPT_CONNECTTIMEOUT, 60);
	curl_setopt($ch, CURLOPT_MAXREDIRS, 10);
	curl_setopt($ch, CURLOPT_SSL_VERIFYHOST , 0);
	curl_setopt($ch, CURLOPT_SSL_VERIFYPEER , false);
	curl_setopt($ch, CURLOPT_POST, true);
	curl_setopt($ch, CURLOPT_POSTFIELDS, $_POST);
	
	$returnText	= curl_exec( $ch );
	curl_close( $ch );
	
	$json = json_decode($returnText);
		$scores = $json->scores;
		
		
	// Open a window (maybe modal) with the list of games to score
	// User selects a game and then a form is presented 
	// Get the icons for the game
	//
	
		
	echo '<div>';
			
	echo '<div class="team-icons">';				
		echo '<div class="us-icon"><img src="' . $s->us_img . '"></div>';
		echo '<div class="opp-icon"><img src="' . $s->opp_img . '"></div>';
	echo '</div>';	
	
	
	echo '<div class="team-input">';				
		echo '<input name="us-score">';
		echo '<input name="opp-score">';
	echo '</div>';
			
			
	echo '</div>';
?>