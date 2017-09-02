<?php

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
	
	$notifications = json_decode($returnText);

	$noCount = 0;

	if($notifications) {
		foreach($notifications as $n) {
			if($n->readtx == '0000-00-00 00:00:00') {
				$noCount = $noCount + 1;
				$lastNotification = $n->message;
			}
		}
	}
	
	//echo json_encode(array('count'=>$noCount, 'lastMessage'=>$lastNotification));
	echo json_encode(array('count'=>$noCount));
	
	die();