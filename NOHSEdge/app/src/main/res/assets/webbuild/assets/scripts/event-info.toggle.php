<?php

/**

stdClass Object
(
    [name] =&gt; Test Event
    [description] =&gt; Promos: Opening night, $1 Beer Happy Hour (5:30-7:00), Jay Bruce Something Promos: Opening night, $1 Beer Happy Hour (5:30-7:00), Jay Bruce Something 
    [notes] =&gt; 
    [opp_img] =&gt; http://app.superfanu.com/icons/event/1.1395598429.mud-hens.png
    [startstring] =&gt; Fri, Apr 11 5:00pm
    [calstarttime] =&gt; 1397250000
    [calendtime] =&gt; 1397253600
    [caltimezone] =&gt; EST
    [pointvalue] =&gt; 10
    [gameurl] =&gt; 
    [awards] =&gt; 
    [venues] =&gt; stdClass Object
        (
            [1] =&gt; stdClass Object
                (
                    [vid] =&gt; 220
                    [name] =&gt; Baxter Office
                    [address] =&gt; 962 Baxter Ave
                    [city] =&gt; Louisville
                    [state] =&gt; KY
                    [zip] =&gt; 40204
                    [notes] =&gt; This is where we work!
                    [img] =&gt; 
                    [latitude] =&gt; 38.240439447179185
                    [longitude] =&gt; -85.7248262969589
                )

        )

    [nocheckins] =&gt; Nobody has checked in yet
    [checkedin] =&gt; 
    [checkinpoints] =&gt; 
    [checkintime] =&gt; 
    [facebook] =&gt; stdClass Object
        (
            [link] =&gt; http://site.superfanu.com
            [name] =&gt; Test Event Promos: Opening night, $1 Beer Happy Hour (5:30-7:00), Jay Bruce Something Promos: Opening night, $1 Beer Happy Hour (5:30-7:00), Jay Bruce Something 
            [message] =&gt; I just checked in to Test Event and earned 10 points!
            [caption] =&gt; 5:00pm at 
            [picture] =&gt; http://app.superfanu.com/uploads/social-imgs/1.1378223082.5.1377050877.mich-logo.png
            [description] =&gt; SuperFanU is the bomb!
            [success] =&gt; true
        )

    [twitter] =&gt; stdClass Object
        (
            [name] =&gt; Test Event Promos: Opening night, $1 Beer Happy Hour (5:30-7:00), Jay Bruce Something Promos: Opening night, $1 Beer Happy Hour (5:30-7:00), Jay Bruce Something 
            [message] =&gt; I just checked in to Test Event and earned 10 points!
            [picture] =&gt; http://app.superfanu.com/uploads/social-imgs/1.1378223082.5.1377050877.mich-logo.png
            [at] =&gt; @username
            [hashtags] =&gt; #hashtag
            [success] =&gt; true
        )

    [event_tickets] =&gt; stdClass Object
        (
            [1] =&gt; stdClass Object
                (
                    [id] =&gt; 127
                    [exurl] =&gt; 
                    [label_name] =&gt; Buy Tickets
                    [name] =&gt; 
                    [description] =&gt; 
                    [fine_print] =&gt; 
                    [date_time] =&gt; 0000-00-00 00:00:00
                    [qty_available] =&gt; 0
                    [purchase_max] =&gt; 0
                    [purchase_step] =&gt; 0
                    [price] =&gt; 0
                    [tax_rate] =&gt; 0
                    [shipping] =&gt; 0
                )

        )

    [event_tickets_purchased] =&gt; 
    [links] =&gt; 
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
	
	$ev = json_decode($returnText);
	
	if($ev->checkedin == '') {
		
		if($ev->event_tickets) {
		
			foreach($ev->event_tickets as $tix) {
				echo '<a href="#" class="ui-btn ui-corner-all major-btn link-track ticket-btn" data-url="'.$tix->exurl.'" data-title="'.$tix->label_name.'" data-eid="'.$_POST['eventid'].'">'.$tix->label_name.'</a>';
			}
			
			echo '<a href="#" id="checkin-btn" data-eid="'.$_POST['eventid'].'" class="ui-btn ui-corner-all major-btn major-btn-half-width">Check In</a>';
			
			
			
		} else {
			echo '<a href="#" id="checkin-btn" data-eid="'.$_POST['eventid'].'" class="ui-btn ui-corner-all major-btn">Check In to this Event</a>';
		}
		
		
		
	} else {
		echo '<a href="#" class="ui-btn ui-corner-all other-btn points-received-color">Checked In!</a>';
	}

		echo '<div class="event-info">';
		
			echo '<div class="left-col">';
				if($ev->opp_img<>'') {
					echo '<img class="event-image" src="'.$ev->opp_img.'">';
				}
				echo '<div class="points-wrap text-align-center';
					if($ev->checkedin <> '') {echo ' points-received-color';}
				echo '"><strong>'.$ev->pointvalue.'</strong><span>PT'.($ev->pointvalue <> 1 ? 'S' : '').'</span></div></div>';
				
			echo '<div class="right-col"><h3>'.$ev->name.'</h3><span class="date">'.$ev->startstring.'</span><p>'.nl2br($ev->description).'</p></div>';
		
		
		echo '</div>';


		
		if($ev->notes) {		
				
			echo '<div class="event-info-notes"><p>'.nl2br($ev->notes).'</p></div>';

		}
		
		
		
/*
		if($ev->checkedin == '' && $ev->event_tickets) {
			foreach($ev->event_tickets as $tix) {
				if( $tix->exurl != '' ) {
					
					echo '<div class="event-info-link tickets ticket-text-color"><div class="link-track" data-url="'.$tix->exurl.'" data-title="Tickets" data-eid="'.$_POST['eventid'].'"><img src="assets/img/ticket-icon-v3.png" /><p>'.$tix->label_name.'</p></div></div>';
					
				}
			}
		}
*/
		
		
		

		if($ev->awards) {		
			foreach($ev->awards as $a) {
				$qr = $_POST['tkthttp'] . 'e/a/' . $a->cid . '/' . $a->qr_hash . '/' . sha1( $a->cid . $a->uid );
				echo '<div class="event-info-award event-info-link award-unlocked" id="award-'.$a->id.'" data-aid="'.$a->id.'" data-cid="'.$a->cid.'" data-award-icon="'.$a->icon.'" data-name="'.addslashes(nl2br($a->name)).'" data-description="'.addslashes(nl2br($a->description)).'" data-receive-text="'.addslashes(nl2br($a->receive_text)).'" data-fine-print="'.addslashes(nl2br($a->fine_print)).'" data-expires="'.(($a->expires == '11/30/-1') ? 'Does not expire' : 'Expires on '.$a->expires).'" data-redeemable="'.$a->redeemable.'" data-used="'.$a->used.'" data-used-tx="'.date('n/j/y', strtotime($a->used_tx)).'" data-barcode="'.addslashes($a->barcode).'" data-qr="'.addslashes($qr).'"><span class="award-unlocked-text">You unlocked an award!</span><h3>'.$a->name.'</h3><p>'.$a->receive_text.'</p></div>';

			}
		}
		
		//let's get social
		if($ev->checkintime) {
			//facebook row
			$fb = $ev->facebook;
			echo '<div class="event-info-link social-facebook" data-link="'.$fb->link.'" data-name="'.addslashes($fb->name).'" data-message="'.addslashes($fb->message).'" data-caption="'.addslashes($fb->caption).'" data-picture="'.addslashes($fb->picture).'" data-description="'.addslashes($fb->description).'" data-eid="'.$_POST['eventid'].'"><img src="assets/img/facebook-icon-v3.png" /><p>Share on Facebook</p></div>';
		}
		
		
		if($ev->links) {
			foreach($ev->links as $link) {
				
				if($link->img <> '') {
				
					echo '<div class="event-info-link link-img"><a href="#" class="link-track" data-url="'.$link->url.'" data-title="'.$link->text.'" data-eid="'.$_POST['eventid'].'"><img class="ad" src="'.$link->img.'" /></a></div>';
					
				} else {
				
					echo '<div class="event-info-link link-text"><div class="link-track" data-url="'.$link->url.'" data-title="'.$link->text.'" data-eid="'.$_POST['eventid'].'"><img src="assets/img/link-icon-v3.png" /><p>'.$link->text.'</p></div></div>';
					
				}
				
			}
		}
		
		if($ev->checkedin == '') {
			
			foreach($ev->venues as $vv) {
				$eventLocation = $vv->name.', ';
			}
			
			echo '<div class="event-info-link add-to-calendar android-only" data-name="'.urlencode($ev->name).'" data-description="'.urlencode($ev->description).'" data-notes="'.urlencode($ev->notes).'" data-location="'.urlencode(rtrim($eventLocation, ', ')).'" data-starttime="'.urlencode($ev->calstarttime).'" data-endtime="'.urlencode($ev->calendtime).'"><img src="assets/img/calendar-icon-v3.png" /><p>Add to Calendar</p></div>';
			
		}
		
		echo '<div class="event-info-link event-info-users" data-eid="'.$_POST['eventid'].'"><img src="assets/img/users-checked-in-v3.png" /><p>'.$ev->nocheckins.'</p></div>';


		if($ev->checkedin == '') {
			foreach($ev->venues as $venue) {
				if($venue->img) {$vimg = $venue->img;} else {$vimg = 'assets/img/map-icon-v3.png';}
				echo '<div class="event-info-link venue" id="venue-'.$venue->vid.'" data-name="'.urlencode($venue->name).'" data-address="'.urlencode($venue->address).'" data-city="'.urlencode($venue->city).'" data-state="'.urlencode($venue->state).'" data-zip="'.urlencode($venue->zip).'" data-lat="'.$venue->latitude.'" data-lng="'.$venue->longitude.'"><img src="'.$vimg.'" /><h4>'.$venue->name.'</h4><p>'.$venue->address.' '.$venue->city.', '.$venue->state.' '.$venue->zip.'</p></div>';
			}
		}


?>