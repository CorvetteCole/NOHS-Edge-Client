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
		
	//print_r($returnText);
	$venues = json_decode($returnText);

?><table class="styled venue-list" border="0">
<?php if($venues) {
	echo '<tr class="filter"><td><input type="text" id="venue-filter" placeholder="Filter" data-clear-btn="true"></td></tr>';
	foreach($venues as $v) {
		echo '<tr class="link" data-filter="'.$v->name.' '.$v->address.' '.$v->city.' '.$v->state.' '.$v->zip.'"><td><a href="#" class="link-track" data-url="https://maps.google.com/maps?q='.$v->name.'+'.$v->address.'+'.$v->city.'+'.$v->state.',+'.$v->zip.'" data-title="'.$v->name.'" data-eid="0"><h4>'.$v->name.'</h4><p>'.$v->address.'<br>'.$v->city.' '.$v->state.', '.$v->zip.'</p></td></tr>';
	}
} ?>
</table>