<?php
	
	
	$fb = $_POST['fbdata'];
	/*
		Object {id: "100002435954267", email: "superfan@karmaprogressive.com", first_name: "Chris", gender: "male", last_name: "Nowak"…}
		email: "superfan@karmaprogressive.com"
		first_name: "Chris"
		gender: "male"
		id: "100002435954267"
		last_name: "Nowak"
		link: "https://www.facebook.com/chris.nowak.140"
		locale: "en_US"
		name: "Chris Nowak"
		timezone: -5
		updated_time: "2013-08-14T18:29:12+0000"
		username: "chris.nowak.140"
		verified: false
		__proto__: Object
	*/
	
	//"email": e.data.email, "name": e.data.name, fbid: e.data.id, "username": e.data.username, "hash": sha1_encode('#lampshade(*55' + e.data.id)
	$_POST['email'] = $fb['email'];
	$_POST['name'] = $fb['name'];
	$_POST['fbid'] = $fb['id'];
	$_POST['username'] = $fb['username'];
	$_POST['hash'] = sha1('#lampshade(*55' . $fb['id']);
	
/*
	print_r($_POST);
	die();
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
		
	print_r($returnText);
?>