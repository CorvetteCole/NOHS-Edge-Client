<?php
/*

stdClass Object
(
    [user] =&gt; stdClass Object
        (
            [uid] =&gt; 1
            [username] =&gt; cwnowak
            [name] =&gt; Chris Nowak
            [profile_pic] =&gt; http://media.superfanu.com/profiles/1.1423258339.png
            [profile_pic_cropped] =&gt; http://media.superfanu.com/profiles/1.1423258339_c100.png
        )

    [place] =&gt; stdClass Object
        (
            [points] =&gt; 0
            [points_int] =&gt; 
            [position] =&gt; 2
            [position_int] =&gt; 2
        )

    [awards] =&gt; stdClass Object
        (
            [1] =&gt; stdClass Object
                (
                    [id] =&gt; 1
                    [points_needed] =&gt; 50
                    [title] =&gt; Jersey signed by Geoff Kempster
                    [description_outside] =&gt; Purchase a chance to win a game worn jersey used vs. Maryland
                    [description_inside] =&gt; 
                    [img] =&gt; http://media.superfanu.com/ads/superfanu/kempster-2.png
                    [img_inside] =&gt; 
                    [img_inside_unlocked] =&gt; 
                    [available_int] =&gt; 8
                    [limit_per] =&gt; 5
                    [expires_tx] =&gt; 11/30/-1
                    [expires_tx_unix] =&gt; 0
                    [prize_count] =&gt; 1
                    [is_redeemable] =&gt; 1
                    [cid] =&gt; 1
                    [points_needed_formatted] =&gt; 50
                )

            [2] =&gt; stdClass Object
                (
                    [id] =&gt; 3
                    [points_needed] =&gt; 80
                    [title] =&gt; Coupon for a Freezy Baja Blast
                    [description_outside] =&gt; Cool off with a coupon for a FREE Chiller from Taco Bell!
                    [description_inside] =&gt; Cool off with a coupon for a FREE Chiller from Taco Bell!

When I got my first full-time job, I remember being overwhelmed by a lot of financial questions.

One of the pressing issues was finding an apartment. Specifically, I wanted to know how much rent I could afford to know which neighborhoods and areas were within my budget.
                    [img] =&gt; http://media.superfanu.com/ads/superfanu/tbell.png
                    [img_inside] =&gt; http://4.bp.blogspot.com/-S275FC-P4vU/U083UAPh4pI/AAAAAAAANEc/UticIHMLCtg/s1600/taco+bell+mtn+dew+black+cherry+kickstart+freeze.jpg
                    [img_inside_unlocked] =&gt; http://gtin.info/wp-content/uploads/2013/12/gti_upc.png
                    [available_int] =&gt; 9
                    [limit_per] =&gt; 5
                    [expires_tx] =&gt; 6/1/15
                    [expires_tx_unix] =&gt; 1433134800
                    [prize_count] =&gt; 1
                    [is_redeemable] =&gt; 1
                    [cid] =&gt; 3
                    [points_needed_formatted] =&gt; 80
                )

        )

)



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
	
	$json = json_decode($returnText);
	//print_r($json);

	include_once('inc.php');
	
	if($json->place->points == -1) {
		echo '<p class="text-align-center"><p>Please logout and login again.</p>';
		die();
	}
	
	if($json->place->position_int < 99) {
		$place = $json->place->position;
		$placeclass = 'double-digit';
	} elseif($json->place->position_int < 999) {
		$place = $json->place->position;
		$placeclass = 'double-digit';
	} else {
		$place = floor( $json->place->position_int / 1000 ) . 'K';
		$placeclass = 'triple-digit';
	}
	echo '<div class="user-info"><div class="user-place place-'.$placeclass.'"><span>'.$place.'</span></div><img class="user-pic user-pic-self" src="'.$json->user->profile_pic_cropped.'" /><div class="user-points-wrap"><span class="user-points">'.$json->place->points.'</span><span class="points-text">Points</span></div><h3 class="username">'.$json->user->username.'</h3></div>';
	
	
	if($json->awards) {
		foreach($json->awards as $a) {
		
/*
					row.awardCID = awards[award].cid;
				  	row.awardUID = jdata.user.uid;
				  	row.awardTitle = awards[award].name;
				  	row.awardDescription = awards[award].description;
				  	row.awardReceiveText = awards[award].receive_text;
				  	row.awardFinePrint = awards[award].fine_print;
				  	row.awardExpires = awards[award].expires;
				  	row.awardExpiresUnix = awards[award].expires_unix;
				  	row.awardRedeemable = awards[award].redeemable;
				  	row.awardUsed = awards[award].used;
				  	row.awardUsedTX = awards[award].used_tx;
				  	row.awardBarcode = awards[award].barcode;
				  	row.awardQRHash = awards[award].qr_hash;
*/

				$qr = $_POST['tkthttp'] . 'e/a/' . $a->cid . '/' . $a->qr_hash . '/' . sha1( $a->cid . $a->uid );
				$pn = 'Buy for ';
				if($a->points_needed == 0) {
					$pn = 'FREE';
				} elseif($a->points_needed == 1) {
					$pn.=$a->points_needed_formatted.' point';
				} else {
					$pn.=$a->points_needed_formatted.' points';
				}
				$avail = '<span>'.$a->available_int.'</span> available, ';
				if($a->limit_per == 0) {
					$avail.='no limit per person';
				} else {
					$avail.='limit '.$a->limit_per.' per person';
				}
				echo '<div class="award prize-store-prize" id="prize-id-'.$a->id.'" data-pid="'.$a->id.'" data-cid="'.$a->cid.'" data-img="'.$a->img.'" data-award-img-inside="'.$a->img_inside.'" data-award-img-inside-unlocked="'.$a->img_inside_unlocked.'" data-points-needed="'.$a->points_needed.'" data-points-needed-formatted="'.$pn.'" data-title="'.addslashes(nl2br($a->title)).'" data-description-inside="'.addslashes(nl2br($a->description_inside)).'" data-available="'.$a->available_int.'" data-limit-per="'.$a->limit_per.'" data-details="'.$avail.'" data-expires="'.(($a->expires_tx == '11/30/-1') ? 'Does not expire' : 'Expires on '.$a->expires_tx).'" data-is-redeemable="'.$a->is_redeemable.'" data-prize-count="'.$a->prize_count.'"><div class="award-icon-container"><img class="award-icon" src="'.$a->img.'" /><div class="gradient"><span>'.$pn.'</span></div></div>';

			echo '<div class="text">';
			echo '<h3>'.nl2br($a->title).'</h3><p class="description">'.nl2br($a->description_outside).'</p>';
			echo '<img class="carat" src="img/award-carat-right-black.png">';
			echo '</div></div>';

		}
	} else {echo '<p class="text-align-center"><p>There are no prizes yet.</p>';}

?>