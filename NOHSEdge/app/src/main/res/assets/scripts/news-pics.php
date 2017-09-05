<?php

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
	
	echo '<div class="news-pics">';
	
	foreach($array['channel']['item'] as $item) {
		//print_r($item['enclosure']['@attributes']['url']);
		//echo '<li class="link-track" data-url="'.$item['link'].'" data-title="Link" data-eid="0"><h3>'.$item['title'].'</h3>';
		//echo '<p>'.strip_tags($item['description']).'</p></li>';
		echo '<div class="news-item link-track" data-url="'.$item['link'].'" data-title="News" data-eid="0" style="background: #000 url('.((!$item['enclosure']['@attributes']['url']) ? 'img/custom/tutorial-home.png' : $item['enclosure']['@attributes']['url'] ) .') center center no-repeat; background-size: cover;"><div class="shadow"></div><p>'.$item['title'].' →</p>';
		
		echo '</div>';
	}
	
	echo '</div>';
	
?>