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
		
	$json = json_decode($returnText);
	
	if($json->response == 'ok') {
		if($json->prizes) {
			//print_r($json->prizes);
			foreach($json->prizes as $p) {
				if($p->used == '0') {
					$class = 'redeemable';
					$text = 'Purchased '.$p->purchased_tx_formatted;
					$action = '<a href="#" class="redeem-item-btn link-color" data-pid="'.$p->pid.'" data-prid="'.$p->id.'" id="redeem-prize-item-'.$p->id.'">Pick Up</a>';
				} else {
					$class = 'redeemed';
					$text = 'Picked up '.$p->used_tx_formatted;
					$action = '';
				}
				echo '<div class="redeemable-item '.$class.'">';
					echo '<span class="text"><span class="title">'.$p->ps_title.'</span>'.$text.'</span>';
					echo '<span class="action">'.$action.'</span>';
				echo '</div>';
			}
		}
	}

	
?>