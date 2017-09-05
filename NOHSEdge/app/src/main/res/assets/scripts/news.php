<?php

	//$_POST['url'] = 'http://goargos.com/rss.asp';
	$ch = curl_init( $_POST['url'] );     
	curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
	curl_setopt($ch, CURLOPT_HEADER, false);
	curl_setopt($ch, CURLOPT_ENCODING, "");
	curl_setopt($ch, CURLOPT_USERAGENT, "SuperFanU");
	curl_setopt($ch, CURLOPT_AUTOREFERER, 60);
	curl_setopt($ch, CURLOPT_CONNECTTIMEOUT, 60);
	curl_setopt($ch, CURLOPT_MAXREDIRS, 10);
/*
	curl_setopt($ch, CURLOPT_SSL_VERIFYHOST , 0);
	curl_setopt($ch, CURLOPT_SSL_VERIFYPEER , false);
	curl_setopt($ch, CURLOPT_POST, true);
	curl_setopt($ch, CURLOPT_POSTFIELDS, $_POST);
*/
	
	$returnText	= curl_exec( $ch );
	curl_close( $ch );
	
	
	//print_r($returnText);
	
	$xml = simplexml_load_string($returnText);
	$json = json_encode($xml);
	$array = json_decode($json,TRUE);
	
	//print_r($array);
	
	echo '<ul class="news">';
	
	foreach($array['channel']['item'] as $item) {
		//print_r($item);
		echo '<li class="link-track" data-url="'.$item['link'].'" data-title="Link" data-eid="0"><h3>'.$item['title'].'</h3>';
		echo '<p>'.strip_tags($item['description']).'</p></li>';
	}
	
	echo '</ul>';
	
?>