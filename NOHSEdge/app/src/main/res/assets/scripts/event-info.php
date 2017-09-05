<?php

/**


stdClass Object
(
    [name] => Kayla's Sick Party
    [description] => vs Amanda's Infection

#Kayla'ssick
#ThanksAmanda
    [notes] => These are some notes! http://google.com
    [opp_img] => http://app.superfanu.com/icons/event/33.1436465824.uk-logo-old-USE-IN-STORY-575x359.jpg
    [isitnow] => true
    [startstring] => Check in now!
    [calstarttime] => 1436466600
    [calendtime] => 1436911200
    [caltimezone] => EST
    [pointvalue] => 1
    [gameurl] => 
    [awards] => stdClass Object
        (
            [1] => stdClass Object
                (
                    [id] => 267886
                    [uid] => 1
                    [name] => FREE Taco Bell Steak Burrito
                    [description] => Get 1 Point
                    [receive_text] => Congrats! You've just unlocked your FREE Taco Bell Steak Burrito (Limit 1 per person. Available at only Louisville area Taco Bell locations.)
                    [fine_print] => Limit 1 per person. 
                    [expires] => 7/10/15
                    [expires_unix] => 1436504400
                    [icon] => http://app.superfanu.com/icons/33.1436466143.taco-bell-logo-640-280.png
                    [tx] => 7/13/15
                    [redeemable] => 1
                    [barcode] => 
                    [qr_hash] => Ooi12khJFS7V5zVg2ykX
                    [used] => 0
                    [used_tx] => 0000-00-00 00:00:00
                    [cid] => 5qpa
                )

        )

    [venues] => stdClass Object
        (
            [1] => stdClass Object
                (
                    [vid] => 888
                    [name] => Green Building
                    [address] => 732 East Market Streets
                    [city] => Louisville
                    [state] => KY
                    [zip] => 40202
                    [notes] => 
                    [img] => 
                    [latitude] => 38.25308626758144
                    [longitude] => -85.73868362698363
                )

            [2] => stdClass Object
                (
                    [vid] => 2020
                    [name] => House
                    [address] => 524 Wood Lake Drive
                    [city] => La Grange
                    [state] => KY
                    [zip] => 40031
                    [notes] => 
                    [img] => 
                    [latitude] => 38.39883599336287
                    [longitude] => -85.39161622524261
                )

        )

    [nocheckins] =>  people have checked in
    [nocheckins_int] => 4
    [checkedin] => 1381002
    [checkinpoints] => 1
    [checkintime] => 7/13/2015 at 1:19pm
    [facebook] => stdClass Object
        (
            [link] => 
            [name] => Kayla's Sick Party vs Amanda's Infection

#Kayla'ssick
#ThanksAmanda
            [message] => I just checked in to Kayla's Sick Party and earned 1 points!
            [caption] => 2:30pm at Green Building
            [picture] => http://app.superfanu.com/uploads/social-imgs/33.1389630391.long-logo.png
            [description] => 
            [success] => true
        )

    [twitter] => stdClass Object
        (
            [tweetString] => SuperFanU - I just checked in to Kayla's Sick Party and earned 1 points! @superfanu #hashtag
            [picture] => http://app.superfanu.com/uploads/social-imgs/33.1389630391.long-logo.png
            [at] => 
            [hashtags] => 
            [success] => true
        )

    [event_tickets] => stdClass Object
        (
            [1] => stdClass Object
                (
                    [id] => 598
                    [exurl] => http://www.ukathletics.com/tickets/tickets-m-footbl.html
                    [label_name] => Buy Tickets
                    [label_color] => #0000ff
                    [name] => 
                    [description] => 
                    [fine_print] => 
                    [date_time] => 0000-00-00 00:00:00
                    [qty_available] => 0
                    [purchase_max] => 0
                    [purchase_step] => 0
                    [price] => 0
                    [tax_rate] => 0
                    [shipping] => 0
                )

        )

    [event_tickets_purchased] => 
    [links] => stdClass Object
        (
            [1] => stdClass Object
                (
                    [text] => Taco Bell
                    [img] => http://app.superfanu.com/uploads/event-link-imgs/33.1436465824.tb_shockermaniacs_largebanner.jpg
                    [url] => http://www.tacobell.com/
                )

            [2] => stdClass Object
                (
                    [text] => Pepsi
                    [img] => http://app.superfanu.com/uploads/event-link-imgs/33.4893.1394125077.Pepsi-Ad.jpg
                    [url] => http://jimmyjohns.com
                )

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
	
	include_once('inc.php');
	
	$ev = json_decode($returnText);
	
	//print_r($ev);
	
	$venue = 'Multiple Locations';
	if( count( $ev->venues ) == 1 ) {
		foreach( $ev->venues as $v) {
			$venue = $v->name;
		}
	}
	
	if($ev->checkedin == '') {
		
		echo '<a href="#" id="checkin-btn" data-eid="'.$_POST['eventid'].'" class="ui-btn ui-corner-all major-btn checkin-btn">Check In to this Event</a>';
		
	} else {
		echo '<a href="#" class="ui-btn ui-corner-all other-btn points-received-color">You are Checked In!</a>';
	}
		$heartValue = " data-hearted='false'";
		if($ev->hearted !== FALSE) {
			$heartValue = " data-hearted='true'";
		}
		echo '<div class="event-info" data-name="'.urlencode($ev->name).'" data-description="'.urlencode($ev->description).'" data-notes="'.urlencode($ev->notes).'" data-location="'.$venue.'" data-starttime="'.$ev->calstarttime.'" data-endtime="'.$ev->calendtime.'"><div class="heart-input" data-source="event-info" data-eid="'.$ev->eid.'"'.$heartValue.'/>';
		
			if($ev->opp_img<>'') {
				echo '<div class="left-col">';
				echo '<img class="event-image" src="'.$ev->opp_img.'"><div class="clear"></div>';
				echo '</div>';
			
			// /left-col
				
				echo '<div class="right-col">';
				
			} else {
				
				echo '<div class="full-col">';
				
			}
			
			echo '<h3>'.nl2br($ev->name).'</h3><p>'.nl2br(linkify($ev->description)).'</p>';
			
			if($ev->notes) {		
				
				echo '<p class="event-info-notes">'.nl2br(linkify($ev->notes)).'</p>';
	
			}
			
			if( $ev->nocheckins_int != 0 ) {
				
				echo '<p class="event-info-users" data-eid="'.$_POST['eventid'].'"><strong>'.$ev->nocheckins_int.'</strong> '.$ev->nocheckins.'</p>';
				
			}
			
			if($ev->publicTags) {
				foreach($ev->publicTags as $publicTag) {
					$user = $publicTag->captain->username;
					$title = $publicTag->captain_title;
					$profile_picture = $publicTag->captain->profile_pic_cropped;
					$display = $publicTag->display;
					if($user && $title && $profile_picture && $display) {
					    echo "<div class='captain-item'><img class='user-pic-crown' src='img/custom/captain-crown.png'><img class='user-pic' src='$profile_picture'/><h2>$user</h2><h3>$title of $display</h3></div>";
					}
				}
			}
			
			
			echo '</div>';
			
			// /right-col
		
		
			echo '<div class="bottom-bar"><div class="points'.(($ev->checkedin != '') ? ' points-received-color" style="border-top: 1px solid #63b664;"' : '"').'><strong>'.$ev->pointvalue.'</strong><span>Point'.(($ev->pointvalue != 1) ? 's' : '').'</span></div><div class="time">';
				
				if($ev->checkedin == '') {
					if($ev->isitnow == 'true') {
						echo '<p class="now"><strong class="checkin-btn" data-eid="'.$_POST['eventid'].'">'.$ev->startstring.'</strong></p>';
					} else {
						echo '<p class="starting">Check in starting<strong>'.$ev->startstring.'</strong></p>';
					}
				} else {
					echo '<p class="starting">You Checked in on<strong>'.$ev->checkintime.'</strong></p>';
				}
				
				echo '</div>';
				
				// /time
			
			echo '</div>';
			
			// /bottom-bar

		echo '</div>';
		
		// /event-info
		
		
		
		if($ev->event_tickets) {
			foreach($ev->event_tickets as $tix) {
				if( $tix->exurl != '' ) {
					
					echo '<div class="event-info-link tickets ticket-text-color"'.(($tix->label_color!='') ? ' style="border: 1px solid '.colorBrightness($tix->label_color,.95).'; background-color: '.colorBrightness($tix->label_color, .05).';"' : '').'><div class="link-track" data-url="'.$tix->exurl.'" data-title="Tickets" data-eid="'.$_POST['eventid'].'"><img class="carat-right tint-image" data-label-color="'.$tix->label_color.'" src="img/bg-arrow.png"><img src="img/ticket-icon-v3.png" class="tint-image" data-label-color="'.$tix->label_color.'" /><p'.(($tix->label_color!='') ? ' style="color: '.$tix->label_color.'"' : '').'>'.$tix->label_name.'</p></div></div>';
					
				}
			}
		}
		
		
		

		if($ev->awards) {		
			echo '<span class="text-divider">You unlocked an award!</span>';
			foreach($ev->awards as $a) {
				$qr = $_POST['tkthttp'] . 'e/a/' . $a->cid . '/' . $a->qr_hash . '/' . sha1( $a->cid . $a->uid );
				echo '<div class="event-info-award event-info-link award-unlocked" id="award-'.$a->id.'" data-aid="'.$a->id.'" data-cid="'.$a->cid.'" data-award-icon="'.$a->icon.'" data-name="'.addslashes(nl2br($a->name)).'" data-description="'.addslashes(nl2br($a->description)).'" data-receive-text="'.addslashes(nl2br($a->receive_text)).'" data-fine-print="'.addslashes(nl2br($a->fine_print)).'" data-expires="'.(($a->expires == '11/30/-1') ? 'Does not expire' : 'Expires on '.$a->expires).'" data-redeemable="'.$a->redeemable.'" data-used="'.$a->used.'" data-used-tx="'.date('n/j/y', strtotime($a->used_tx)).'" data-barcode="'.addslashes($a->barcode).'" data-qr="'.addslashes($qr).'"><img src="'.$a->icon.'"><div class="award-info"><h3>'.$a->name.'</h3><p>'.$a->receive_text.'</p></div></div>';
				
				

			}
		}
		
		//let's get social
		if($ev->checkintime) {
			echo '<span class="text-divider">Share your Check In</span>';
			echo '<div class="social">';
			
			//twitter row
			$tw = $ev->twitter;
			echo '<div class="event-info-link social-twitter" data-message="'.addslashes($tw->tweetString).'" data-picture="'.addslashes($tw->picture).'" data-eid="'.$_POST['eventid'].'"><img src="img/twitter-icon.png" /><p><a href="https://twitter.com/intent/tweet?text='.urlencode($tw->tweetString).'&url='.urlencode($tw->picture).'">Share on Twitter</a></p></div>';
			//facebook row
			$fb = $ev->facebook;
			echo '<div class="event-info-link social-facebook" data-link="'.$fb->link.'" data-name="'.addslashes($fb->name).'" data-message="'.addslashes($fb->message).'" data-caption="'.addslashes($fb->caption).'" data-picture="'.addslashes($fb->picture).'" data-description="'.addslashes($fb->description).'" data-eid="'.$_POST['eventid'].'"><img src="img/facebook-icon.png" /><p>Share on Facebook</p></div>';
			
			echo '</div>';
			
		}
		
		
		if($ev->links) {
			
			echo '<div class="link-imgs">';
			
			foreach($ev->links as $link) {
				
				if($link->img <> '') {
				
					echo '<div class="event-info-link link-img"><a href="#" class="link-track" data-url="'.$link->url.'" data-title="'.$link->text.'" data-eid="'.$_POST['eventid'].'"><img class="ad" src="'.$link->img.'" /></a></div>';
					
				} else {
				
					echo '<div class="event-info-link link-text"><div class="link-track" data-url="'.$link->url.'" data-title="'.$link->text.'" data-eid="'.$_POST['eventid'].'"><img src="img/link-icon-v3.png" /><p>'.$link->text.'</p></div></div>';
					
				}
				
			}
			
			echo '</div>';
			
		}
		
/*
		if($ev->checkedin == '') {
			
			foreach($ev->venues as $vv) {
				$eventLocation = $vv->name.', ';
			}
			
			echo '<div class="event-info-link add-to-calendar android-only" data-name="'.urlencode($ev->name).'" data-description="'.urlencode($ev->description).'" data-notes="'.urlencode($ev->notes).'" data-location="'.urlencode(rtrim($eventLocation, ', ')).'" data-starttime="'.urlencode($ev->calstarttime).'" data-endtime="'.urlencode($ev->calendtime).'"><img src="img/calendar-icon-v3.png" /><p>Add to Calendar</p></div>';
			
		}
*/


		if($ev->checkedin == '') {
			echo '<span class="text-divider">Check in at these locations:</span>';
			echo '<div class="venues">';
			foreach($ev->venues as $venue) {
				if($venue->img) {$vimg = $venue->img;} else {$vimg = 'img/map-icon-v3.png';}
				echo '<div class="event-info-link venue" id="venue-'.$venue->vid.'" data-name="'.urlencode($venue->name).'" data-address="'.urlencode($venue->address).'" data-city="'.urlencode($venue->city).'" data-state="'.urlencode($venue->state).'" data-zip="'.urlencode($venue->zip).'" data-lat="'.$venue->latitude.'" data-lng="'.$venue->longitude.'"><img src="'.$vimg.'" /><h4>'.$venue->name.'</h4><p>'.$venue->address.'<br />'.$venue->city.', '.$venue->state.' '.$venue->zip.'</p></div>';
			}
			echo '</div>';
		}
		
		buildFanPollDisplay($ev->poll);
		buildPublicTagDisplay($ev->publicTags);
		
		function buildPublicTagDisplay($publicTags) {
			echo "<span class='text-divider'>Add these related calendars:</span>";
			echo "<div class='public-tag-container'>";
			echo "<ul class='public-tag-list'>";
			foreach($publicTags as $publicTag) {
				$display = $publicTag->display;
				$token = $publicTag->token;
				echo "<li class='public-tag-list-item' data-token='$token' data-cal='$display'><div><span>";
				echo $display;
				echo "</span></div></li>";
			}
			echo "</ul>";
			echo "</div>";
		}
		
		function buildFanPollDisplay($fanPollId) {
			if($fanPollId) {
				echo "<span class='text-divider'>Try your hand at this related Fan Poll:</span>";
				echo "<div class='event-info-fanpoll-container'>";
				echo "<span class='public-tag-list'>Click here?</span>";
				echo "</div>";
			}
		}
?>