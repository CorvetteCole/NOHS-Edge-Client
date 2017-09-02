<?php
/*

stdClass Object
(
    [user] =&gt; stdClass Object
        (
            [uid] =&gt; 6
            [username] =&gt; brandon
            [name] =&gt; Brandon Lawrence
            [profile_pic] =&gt; http://media.superfanu.com/profiles/6.1376355495.png
            [profile_pic_cropped] =&gt; http://media.superfanu.com/profiles/6.1376355495_c100.png
        )

    [place] =&gt; stdClass Object
        (
            [points] =&gt; 595
            [position] =&gt; 2
            [position_int] =&gt; 2
        )

    [awards] =&gt; stdClass Object
        (
            [1] =&gt; stdClass Object
                (
                    [id] =&gt; 
                    [uid] =&gt; 
                    [name] =&gt; Home is Where the Card is
                    [description] =&gt; Attend a home game
                    [receive_text] =&gt; Congrats! You've unlocked a free T-shirt! Come by the Atletic office anytime M-F between 10a-4p to pick it up! Remember to bring your student ID!
                    [fine_print] =&gt; Prize must be picked up by 6/3 at 4pm. Subject to Student ID. Prize may be forfeited if proper ID is not provided. Limited quantities of T-shirts, so we may substitute a different prize if T-shirts are no longer available.
                    [expires] =&gt; 1/1/13
                    [expires_unix] =&gt; 1357020000
                    [icon] =&gt; http://app.superfanu.com/icons/1.1396476523.4433715.jpeg
                    [tx] =&gt; 
                    [redeemable] =&gt; 1
                    [barcode] =&gt; 
                    [qr_hash] =&gt; 
                    [used] =&gt; 
                    [used_tx] =&gt; 4/11/14 1:25pm
                    [cid] =&gt; 0
                )

            [2] =&gt; stdClass Object
                (
                    [id] =&gt; 7
                    [uid] =&gt; 6
                    [name] =&gt; Not for the Faint of Skin
                    [description] =&gt; Attend 5 outdoor events in July
                    [receive_text] =&gt; Don't forget the sun screen!
                    [fine_print] =&gt; 
                    [expires] =&gt; 11/30/-1
                    [expires_unix] =&gt; 0
                    [icon] =&gt; http://app.superfanu.com/icons/1.1396480259.sausage-on-the-grill-1.jpg
                    [tx] =&gt; 7/13/11
                    [redeemable] =&gt; 0
                    [barcode] =&gt; 
                    [qr_hash] =&gt; 
                    [used] =&gt; 
                    [used_tx] =&gt; 0000-00-00 00:00:00
                    [cid] =&gt; 7
                )

            [3] =&gt; stdClass Object
                (
                    [id] =&gt; 23666
                    [uid] =&gt; 6
                    [name] =&gt; Retro
                    [description] =&gt; 500 points
                    [receive_text] =&gt; You had 500 points!
                    [fine_print] =&gt; Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam id ipsum quam. Maecenas cursus risus eleifend, ultrices magna eu, sagittis urna. Donec augue mi, elementum quis eros ut, facilisis consectetur urna. Nulla id ultrices felis. Sed nec dolor nec orci sagittis imperdiet nec in nibh. Quisque tincidunt sit amet sem a sodales. Mauris augue nibh, malesuada vel eros vitae, pharetra ultrices ligula. Suspendisse rutrum dolor augue, sed tempus mi feugiat vel.
                    [expires] =&gt; 11/30/-1
                    [expires_unix] =&gt; 0
                    [icon] =&gt; http://app.superfanu.com/icons/1.1396480192.Cassette-retro-950x709.jpg
                    [tx] =&gt; 10/21/13
                    [redeemable] =&gt; 1
                    [barcode] =&gt; 
                    [qr_hash] =&gt; buFBmqcfI4vSkKA03wtT
                    [used] =&gt; 0
                    [used_tx] =&gt; 0000-00-00 00:00:00
                    [cid] =&gt; i9e
                )

            [4] =&gt; stdClass Object
                (
                    [id] =&gt; 
                    [uid] =&gt; 
                    [name] =&gt; Sausage Fest
                    [description] =&gt; Tailgate before a UL football game 3 times
                    [receive_text] =&gt; Never has a sausage fest looked sooooo good.
                    [fine_print] =&gt; 
                    [expires] =&gt; 11/30/-1
                    [expires_unix] =&gt; 0
                    [icon] =&gt; http://app.superfanu.com/icons/1.1396480259.sausage-on-the-grill-1.jpg
                    [tx] =&gt; 
                    [redeemable] =&gt; 0
                    [barcode] =&gt; 
                    [qr_hash] =&gt; 
                    [used] =&gt; 
                    [used_tx] =&gt; 4/11/14 1:25pm
                    [cid] =&gt; 0
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
				echo '<div class="award '.(($a->tx<>'') ? 'award-unlocked' : 'award-locked').'" id="award-'.$a->id.'" data-aid="'.$a->id.'" data-cid="'.$a->cid.'" data-award-icon="'.$a->icon.'" data-name="'.addslashes(nl2br($a->name)).'" data-description="'.addslashes(nl2br($a->description)).'" data-receive-text="'.addslashes(nl2br(linkify($a->receive_text))).'" data-fine-print="'.addslashes(nl2br(linkify($a->fine_print))).'" data-expires="'.(($a->expires == '11/30/-1') ? 'Does not expire' : 'Expires on '.$a->expires).'" data-redeemable="'.$a->redeemable.'" data-used="'.$a->used.'" data-used-tx="'.date('n/j/y', strtotime($a->used_tx)).'" data-barcode="'.addslashes($a->barcode).'" data-qr="'.addslashes($qr).'"><div class="award-icon-container"><img class="award-icon" src="'.$a->icon.'" /><div class="gradient"></div></div>';

			echo '<div class="text">';
				if($a->tx=='') { echo '<img src="assets/img/award-lock-white.png">';}
			echo '<h3>'.nl2br($a->name).'</h3><p class="description">'.nl2br($a->description).'</p>';
				if($a->tx<>'') { echo '<img src="assets/img/award-carat-right-black.png">'; }
			echo '</div></div>';

		}
	} else {echo '<p class="text-align-center"><p>There are no awards yet.</p>';}

?>