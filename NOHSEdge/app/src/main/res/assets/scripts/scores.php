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
	
	$json = json_decode($returnText);
		$events = $json->events;
		$scores = $json->scores;
	echo '<ul data-role="listview" id="scores-ul">';
	if($events) {
		$select.='<li class="scores-event-chooser">';
		//print_r($events);
		$select.='<select name="add-score-chooser" id="add-score-chooser">';
		$select.='<option value="">Choose an event to add a score</option>';
			foreach($events as $e) {
				$select.='<option value="'.$e->eid.'">'.$e->name.' - '.$e->description.'</option>';
				$score_div.='<img class="score-img" id="scores-us-img-'.$e->eid.'" src="'.$e->us_img.'"><img class="score-img" id="scores-opp-img-'.$e->eid.'" src="'.$e->opp_img.'">';
			}
		$select.='</select>';
		$select.='</li>';
		
		echo $select;
		echo $score_div;
	}
	if($scores) {
		
		foreach($scores as $k=>$s) {
			echo "<li>";
				echo '<h2>' . $s->name . '</h2>';
				echo '<h3>' . $s->starttime . '</h3>';
				
				
				echo '<div class="us-score' . (($s->us_score > $s->opp_score)?' winner':'') . '">';
				echo '	<img src="' . $s->us_img . '"> <span>' . $s->us_score . '</span>';
				echo '</div>';
				
				echo '<div class="opp-score' . (($s->opp_score > $s->us_score)?' winner':'') . '">';
				echo '	<span>' . $s->opp_score . '</span> <img src="' . $s->opp_img . '">' ;
				echo '</div>';
				
				echo '<p class="note">' . $s->note . '</p>';
			echo "</li>";
		}
	} else {echo '<li class="centered"><p>No scores yet.</p></li>';}
	echo '</ul>';
?>