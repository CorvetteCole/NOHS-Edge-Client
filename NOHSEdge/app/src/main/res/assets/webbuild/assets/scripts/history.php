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
		//echo print_r($json->history);
		$history = $json->history;
	echo '<ul data-role="listview" class="history">';
	if($history) {
		foreach($history as $h) {
			echo '<li><div class="right-col"><p><strong>'.$h->points.'</strong>Point'.((abs($h->points) != 1) ? 's' : '').'</p></div><div class="left-col"><p><strong>'.$h->name.'</strong>'.$h->tx.'</p></div></li>';

		}
	} else {echo '<li class="centered"><p>No history yet.</p></li>';}
	echo '</ul>';
?>