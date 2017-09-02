<?php

/**

stdClass Object
(
    [header] =&gt; April 2014
    [location] =&gt; Baxter Office
    [eid] =&gt; 426
    [date] =&gt; 4/11/2014
    [name] =&gt; Test Event
    [description] =&gt; 
    [startdate] =&gt; April 11
    [starttime] =&gt; 5:00pm
    [pointvalue] =&gt; 10
    [opp_img] =&gt; http://app.superfanu.com/icons/event/1.1395598429.mud-hens.png
    [us_img] =&gt; http://media.superfanu.com/banner/bats.png
    [tag] =&gt; 
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
	
	$events = json_decode($returnText);

		echo '<ul data-role="listview" id="upcoming-events-list" class="ui-arrow-min">';
		if($events) {
			$ev_count = 0;
			foreach($events as $ev) {
				//print_r($ev);
				if($ev->header) {echo '<li data-role="list-divider">'.$ev->header.'</li>';}
					
				echo '<li id="event-'.$ev->eid.'" class="upcoming-event '.str_replace(' ','-',$ev->tag).'"><a href="#">';
				//<div class="points"><strong>'.$ev->pointvalue.'</strong>Point'.($ev->pointvalue > 1 ? 's' : '').'</div><h3>'.$ev->name.'</h3><p class="description">'.nl2br($ev->description).'</p><p class="starttime"><img class="clock" src="img/clock.png" />'.$ev->starttime.' <img class="map-marker" src="img/map-marker.png" />'.$ev->location.'</p>';
				
				if($ev_count == 0 && $ev->us_img<>'' && $ev->opp_img<>'') {
					echo '<h3 class="text-align-center">Next Event</h3><span class="date text-align-center">'.$ev->startdate.' at '.$ev->starttime.'</span><div class="spotlight text-align-center"><img class="opp-img" src="'.$ev->opp_img.'"><span class="vs">VS</span><img class="us-img" src="'.$ev->us_img.'"></div><div class="event-info"><div class="left-col"><div class="points-wrap text-align-center"><strong>'.$ev->pointvalue.'</strong><span>PT'.($ev->pointvalue <> 1 ? 'S' : '').'</span></div></div><div class="right-col"><h3>'.$ev->name.'</h3><p>'.$ev->description.'</p><span class="location">@ '.$ev->location.'</span>';
					if($ev->event_tickets) {
						echo '<span class="tickets-available"><img class="ticket" src="assets/img/ticket-icon-tiny.png" />Tickets Available</span>';
					}
					echo '</div></div>';
				} else {
					
					echo '<div class="event-info"><div class="left-col">';
						if($ev->opp_img<>'') {
							echo '<img class="event-image" src="'.$ev->opp_img.'">';
						}
					echo '<div class="points-wrap text-align-center"><strong>'.$ev->pointvalue.'</strong><span>PT'.($ev->pointvalue <> 1 ? 'S' : '').'</span></div></div><div class="right-col"><h3>'.$ev->name.'</h3><span class="date">'.$ev->startdate.' at '.$ev->starttime.'</span><p>'.$ev->description.'</p><span class="location">@ '.$ev->location.'</span>';
					if($ev->event_tickets) {
						echo '<span class="tickets-available"><img class="ticket" src="assets/img/ticket-icon-tiny.png" />Tickets Available</span>';
					}
					echo '</div></div>';
					
				}
					
				
				
				echo '</a></li>';
				$ev_count++;
			}
		} else {
			echo '<li><h3>Sorry, there are no upcoming events.</h3></li>';
		}
		echo '</ul>';
		die();
		
/*
		if(events) {
			for (var i=0;i<events.length;i++) {
			
				if(events[i].header) {push += '<li data-role="list-divider">'+events[i].header+'</li>';}
				
				push += '<li id="event-'+events[i].eid+'" class="upcoming-event"><a href=""><h3>'+events[i].name+'</h3><p>'+events[i].description+'</p><p>'+events[i].starttime + ' @ ' + events[i].location+'</p></a></li>';
				
			}
			
		
		} else {
		
			push+= '<li><h3>Sorry, there are no upcoming events.</h3></li>';
				
		}
		
		push += '</ul>';
*/
			
			
?>