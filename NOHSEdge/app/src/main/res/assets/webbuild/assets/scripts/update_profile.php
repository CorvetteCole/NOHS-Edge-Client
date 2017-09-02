<?php


	//print_r($_POST);
	
	//print_r($_POST['formdata']);
	
	$formdata = array();
	parse_str($_POST['formdata'], $formdata);
	
	$formdata['platform'] = $_POST['platform'];
	$formdata['uuid'] = $_POST['uuid'];
	$formdata['login_key'] = $_POST['login_key'];
	foreach($formdata['meta'] as $k=>$v) {
		$formdata['meta-'.$k] = $v;
	}
	
	//print_r($formdata);


	$ch = curl_init( $_POST['url'] );     
	curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
	curl_setopt($ch, CURLOPT_HEADER, false);
	curl_setopt($ch, CURLOPT_ENCODING, "");
	curl_setopt($ch, CURLOPT_USERAGENT, "SuperFanU");
	curl_setopt($ch, CURLOPT_AUTOREFERER, 60);
	curl_setopt($ch, CURLOPT_CONNECTTIMEOUT, 60);
	curl_setopt($ch, CURLOPT_MAXREDIRS, 10);
	curl_setopt($ch, CURLOPT_POST, true);
	curl_setopt($ch, CURLOPT_POSTFIELDS, $formdata);
	
	$returnText	= curl_exec( $ch );
	curl_close( $ch );
	
	print_r($returnText);
	
?>