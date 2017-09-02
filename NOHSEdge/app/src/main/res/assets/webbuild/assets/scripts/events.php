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

	$json = json_decode($returnText);
		$events = $json->events;
		$tags = $json->tags;
		// Retrieves a list of hearted events for this user and network
		$hearts = $json->hearts;
		$hasCalendarIcon = $json->hasCalendarIcon;

		echo '<ul data-role="listview" id="upcoming-events-list" class="ui-arrow-min">';
		if($events) {
			$ev_count = 0;
			foreach($events as $ev) {
				// Checks to see if the event has been hearted by the user and sets a string value, which
				// will be appended inline to the element, to set the toggle to hearted or unhearted.
				$heartValue = " data-hearted='false'";
				if(array_search($ev->eid, $hearts) !== FALSE) {
					$heartValue = " data-hearted='true'";
				}

				if($ev->header) {echo '<li data-role="list-divider">'.$ev->header.'</li>';}
					
				echo '<li id="event-'.$ev->eid.'" class="upcoming-event '.(($ev->tag <> '') ? 'tag-'.str_replace(' ','-',$ev->tag) : '').'"><a>';
				//<div class="points"><strong>'.$ev->pointvalue.'</strong>Point'.($ev->pointvalue > 1 ? 's' : '').'</div><h3>'.$ev->name.'</h3><p class="description">'.nl2br($ev->description).'</p><p class="starttime"><img class="clock" src="img/clock.png" />'.$ev->starttime.' <img class="map-marker" src="img/map-marker.png" />'.$ev->location.'</p>';
				
				if($ev_count == 0 && $ev->us_img<>'' && $ev->opp_img<>'') {
					echo '<div class="spotlight text-align-center"><img class="opp-img" src="'.$ev->opp_img.'"><span class="vs">VS.</span><img class="us-img" src="'.$ev->us_img.'"></div><div class="event-info"><div class="heart-input" data-source="events" data-eid="'.$ev->eid.'"'.$heartValue.'/><div class="full-col"><h3>'.$ev->name.'</h3><p>'.$ev->description.'</p><span class="location">@ '.$ev->location.'</span>';
					if($ev->f_temp) {
						//has weather
						echo '<span class="weather"><img src="assets/img/weather/'.$ev->icon.'">'.$ev->temp_formatted.' - '.$ev->weather_description.'</span>';
					}
					if($ev->event_tickets) {
						echo '<span class="tickets-available"'.(($ev->event_tickets_label_color!='') ? ' style="color: '.$ev->event_tickets_label_color.'"' : '').'><img class="ticket tint-image" '.(($ev->event_tickets_label_color!='') ? ' data-label-color="'.$ev->event_tickets_label_color.'"' : '').' src="assets/img/ticket-icon-tiny.png" />'.$ev->event_tickets_label_name.'</span>';
					}
					echo '</div></div>';
				} else {
					
					echo '<div class="event-info"><div class="heart-input" data-source="events" data-eid="'.$ev->eid.'"'.$heartValue.'/><div class="left-col">';
						if($ev->opp_img<>'') {
							echo '<img class="event-image" src="'.$ev->opp_img.'">';
						}
					echo '</div><div class="right-col"><h3>'.$ev->name.'</h3><p>'.nl2br($ev->description).'</p><span class="location">@ '.$ev->location.'</span>';
					
					if($ev->f_temp) {
						//has weather
						echo '<span class="weather"><img src="assets/img/weather/'.$ev->icon.'">'.$ev->temp_formatted.' - '.$ev->weather_description.'</span>';
					}
					if($ev->event_tickets) {
						echo '<span class="tickets-available"'.(($ev->event_tickets_label_color!='') ? ' style="color: '.$ev->event_tickets_label_color.'"' : '').'><img class="ticket tint-image" '.(($ev->event_tickets_label_color!='') ? ' data-label-color="'.$ev->event_tickets_label_color.'"' : '').' src="assets/img/ticket-icon-tiny.png" />'.$ev->event_tickets_label_name.'</span>';
					}
					echo '</div></div>';
					
				}
				
				echo '<div class="bottom-bar"><div class="points"><strong>'.$ev->pointvalue.'</strong><span>Point'.(($ev->pointvalue != 1) ? 's' : '').'</span></div><div class="time">';
					if($ev->isitnow == 'true') {
						echo '<p class="now"><strong>'.$ev->startstring.'</strong></p>';
					} else {
						echo '<p class="starting">Check in starting<strong>'.$ev->startstring.'</strong></p>';
					}
				echo '</time></div>';
					
				
				
				echo '</a></li>';
				$ev_count++;
			}
		} else {
			echo '<li><h3>Sorry, there are no upcoming events.</h3></li>';
		}
		echo '</ul>';
		if($events) {
			$cal = array();
			foreach($events as $e) {
				$tag = '';
				$class = 'event';
				if($e->tag<>'') {
					//$class .= ' tag-'.str_replace(' ', '-', $e->tag);
					$tag = 'tag-'.str_replace(' ', '-', $e->tag);
				}
				//$cal[ $e->month ][ $e->day ] = array(NULL, $tag);
				$cal[ $e->month ][ $e->day ] = array('event-'.$e->eid, $class, $tag);
			}
			if($cal) {
				include('../classes/calendar.class.php');
				echo '<div id="events-calendar"><div class="slider">';
				foreach($cal as $k=>$m) {
					echo generate_calendar(substr($k, 0, 4), substr('0'.substr($k,4,2),-2), $m, 1);
				}
				echo '</div></div>';
			}
		}
		if($tags) {
			foreach($tags as $k=>$v) {
				$tagArr[$k] = $v;
			}
		}
		if(count($tagArr) > 0) {
			echo '<div id="event-tags">';
				for($i=1;$i<=count($tagArr);$i++) {
					echo '<span class="events-tab'.(($i==1) ? ' active' : '').'" data-event="'.str_replace(' ','-',$tagArr[$i]->tag).'">'.$tagArr[$i]->display.'</span>';
				}
			echo '</div>';
			echo '<span id="show-only-tag">'.str_replace(' ','-',$tagArr[1]->tag).'</span>';
		} else {
			echo '<div id="event-tags">Events</div>';
		}
		echo '<div id="has-calendar-icon" data-show="'.$hasCalendarIcon.'"></div>';
		die();
		
		
			
			
?>