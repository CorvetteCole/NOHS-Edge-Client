<?php

	header('Access-Control-Allow-Origin: *', false);


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
	$rosters = json_decode($returnText);
	
/*
	<table class="styled" border="0">
			<tr class="link"><td><h4><a href="#" class="link-track" data-url="http://www.shupirates.com/sports/w-golf/mtt/seha-w-golf-mtt.html" data-title="Women's Golf" data-eid="0">Women's Golf</a></h4></td></tr>
			<tr class="link"><td><h4><a href="#" class="link-track" data-url="http://www.shupirates.com/sports/w-soccer/mtt/seha-w-soccer-mtt.html" data-title="Volleyball" data-eid="0">Women's Soccer</a></h4></td></tr>
			<tr class="link"><td><h4><a href="#" class="link-track" data-url="http://www.shupirates.com/sports/m-soccer/mtt/seha-m-soccer-mtt.html" data-title="Volleyball" data-eid="0">Men's Soccer</a></h4></td></tr>
			<tr class="link"><td><h4><a href="#" class="link-track" data-url="http://www.shupirates.com/sports/w-volley/mtt/seha-w-volley-mtt.html" data-title="Volleyball" data-eid="0">Volleyball</a></h4></td></tr>
		</table>
*/
?><table class="styled" border="0">
<?php if($rosters) {
	foreach($rosters as $r) {
		echo '<tr class="link"><td><h4><a href="#" class="link-track" data-url="'.$r->url.'" data-title="'.$r->name.'" data-eid="0">'.$r->name.'</a></h4></td></tr>';
	}
} ?>
</table>