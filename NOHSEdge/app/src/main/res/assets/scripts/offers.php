<?php

/**
stdClass Object
(
    [1] =&gt; stdClass Object
        (
            [oid] =&gt; 1
            [icon] =&gt; http://app.superfanu.com/uploads/offers/36.1396984497.512px-Dominos_pizza_logo.svg.png
            [bgimg] =&gt; http://app.superfanu.com/uploads/offers/36.1396984497.dominos-pizza.jpg
            [name] =&gt; Free 1 topping Pizza
            [description] =&gt; For freeeeee! This need to be slightly longer so that we can scroll the background image, so that's all that I'm doing here.
            [fine_print] =&gt; Carryout only. elit felis, placerat ac enim sit amet, facilisis vestibulum leo. Proin quis varius tellus. In sit amet turpis nec enim vulputate feugiat. Sed tortor risus, interdum vel porttitor porta, porta in augue. Aliquam vel est suscipit lacus interdum bibendum quis eu odio. 

Suspendisse eros purus, tempus vitae quam nec, lobortis tempus arcu. Quisque orci orci, fermentum volutpat suscipit sit amet, consequat ut lacus. Fusce ullamcorper dolor in tellus porta rutrum. Sed condimentum rutrum tortor. Vestibulum tincidunt lacus ut justo condimentum accumsan. Phasellus accumsan lorem velit, sit amet condimentum augue ornare et. Vivamus eget leo suscipit, porta velit et, egestas lorem.
            [barcode] =&gt; http://app.superfanu.com/uploads/offers/36.1396984678.sample-1d-barcode1.jpg
            [expires] =&gt; 5/10/14
            [used_tx] =&gt; 
        )

)

**/

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
	
	$offers = json_decode($returnText);
	//print_r($offers);die();
	
	include_once('inc.php');
	
	echo '<ul data-role="listview" id="offers-list" class="ui-arrow-min">';
	if($offers) {

		if($offers->error) {
			
			echo '<li class="text-align-center"><p class="oflowauto">'.$offers->error.'</p></li>';
			
		} else {
		
			foreach($offers as $o) {
				
				echo '<li class="offer" id="offer-'.$o->oid.'" data-oid="'.$o->oid.'" data-name="'.addslashes($o->name).'" data-description="'.addslashes(nl2br(linkify($o->description))).'" data-fine-print="'.addslashes(nl2br(linkify($o->fine_print))).'" data-offer-icon="'.$o->icon.'" data-bgimg="'.$o->bgimg.'" data-barcode="'.$o->barcode.'" data-expires="'.$o->expires.'" data-used-tx="'.$o->used_tx.'"><a href="#"><img src="'.$o->icon.'" />';
				echo '<h3>'.$o->name.'</h3><p class="description">'.$o->description.'</p><span class="date">Expires '.$o->expires.'</date>';
				echo '</li>';
	
			}
			
		}


	}
	echo '</ul>';
?>