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
	
/*
	if(true) {
		var_dump($json);
		die('finished echoing json');
	}
*/
	
	$tabs = '';
	if(count($json) > 1) {
		$tabs = '<form><fieldset data-role="controlgroup" data-type="horizontal">';
		$tab_count = 1;
		foreach($json as $j) {
			$place = 1;
			$html = '<ul class="leaderboard">';
			foreach($j->leaders as $k=>$l) {
				
				if($l->current_position) {
					$place = $l->current_position;
				}
				
				if($l->title) {
					$profile_picture = $l->profile_pic_cropped;
					$user = $l->username;
					$title = $l->title;
					$display = $l->display;
					$crownPosition = (33 + ((strlen((string) $place) - 1) * 9)) . 'px';
					
					if($l->current_position) {
						$html.='<li class="row-highlight captain-list-item"><div class="user-place"><span>'.$place.'</span></div>'."<img style='left: $crownPosition' class='user-pic-crown' src='assets/img/custom/captain-crown.png'>".'<img class="user-pic user-pic-self" src="'.$profile_picture.'" /><h3 class="captain-text">'.$user.'</h3><span class="user-points">'.$l->total_points_formatted.'</span>'."<br/><p class='captain-text'>$title of $display</p>";

					} else {
						$html.='<li class="captain-list-item"><div class="user-place"><span>'.$place.'</span></div>'."<img style='left: $crownPosition' class='user-pic-crown' src='assets/img/custom/captain-crown.png'>".'<img class="user-pic user-pic-self" src="'.$profile_picture.'" /><h3 class="captain-text">'.$user.'</h3><span class="user-points">'.$l->total_points_formatted.'</span>'."<br/><p class='captain-text'>$title of $display</p>";
					}
				} else {
					if($l->current_position) {
						//$position = $l->current_position;
						$html.='<li class="row-highlight"><div class="user-place"><span>'.$place.'</span></div><img class="user-pic user-pic-self" src="'.$l->profile_pic_cropped.'" /><h3>'.$l->username.'</h3><span class="user-points">'.$l->total_points_formatted.'</span></li>';
					} else {
						//$position = $k;
						$html.='<li><div class="user-place"><span>'.$place.'</span></div><img class="user-pic" src="'.$l->profile_pic_cropped.'" /><h3>'.$l->username.'</h3><span class="user-points">'.$l->total_points_formatted.'</span></li>';
					}
				}
				
				$place = $place + 1;
	
			}
			if($j->user->current_position > $place && $j->name == 'All') {
				$html.='<li class="row-highlight"><div class="user-place"><span>'.$j->user->current_position.'</span></div><img class="user-pic user-pic-self" src="'.$j->user->profile_pic_cropped.'" /><h3>'.$j->user->username.'</h3><span class="user-points">'.$l->total_points_formatted.'</span></li>';
			}
			
			$html.='</ul>';
			$tabs.='<input type="radio" name="leader-tab-choice" id="leader-tab-choice-'.$tab_count.'" data-html="'.rawurlencode($html).'"><label for="leader-tab-choice-'.$tab_count.'">'.$j->name.'</label>';
			$tab_count++;
		}
		$tabs.='</fieldset></form>';
	} else {
		$tabs = '<form><fieldset data-role="controlgroup" data-type="horizontal">';
		$tab_count = 1;
		foreach($json as $j) {
			$place = 1;
			$html = '<ul class="leaderboard">';
			foreach($j->leaders as $k=>$l) {
				
				if($l->current_position) {
					$place = $l->current_position;
				}
				
				if($l->current_position) {
					//$position = $l->current_position;
					$html.='<li class="row-highlight"><div class="user-place"><span>'.$place.'</span></div><img class="user-pic user-pic-self" src="'.$l->profile_pic_cropped.'" /><h3>'.$l->username.'</h3><span class="user-points">'.$l->total_points_formatted.'</span></li>';
				} else {
					//$position = $k;
					$html.='<li><div class="user-place"><span>'.$place.'</span></div><img class="user-pic" src="'.$l->profile_pic_cropped.'" /><h3>'.$l->username.'</h3><span class="user-points">'.$l->total_points_formatted.'</span></li>';
				}
				
				$place = $place + 1;
	
			}
			
			if($j->user->current_position > $place && $j->name == 'All') {
				$html.='<li class="row-highlight"><div class="user-place"><span>'.$j->user->current_position.'</span></div><img class="user-pic user-pic-self" src="'.$j->user->profile_pic_cropped.'" /><h3>'.$j->user->username.'</h3><span class="user-points">'.$l->total_points_formatted.'</span></li>';
			}

			$html.='</ul>';
			$tabs.='<input type="radio" name="leader-tab-choice" id="leader-tab-choice-'.$tab_count.'" data-html="'.rawurlencode($html).'"><label for="leader-tab-choice-'.$tab_count.'">'.$j->name.'</label>';
			$tab_count++;
		}
		$tabs.='</fieldset></form>';
	}
	
	echo $tabs;
	
	
	
	
/*
	if($leaders) {
		$place = 1;
		foreach($leaders as $k=>$l) {
			
			if($l->current_position) {
				$place = $l->current_position;
			}
			
			if($place < 99) {
				$placeclass = 'double-digit';
			} elseif($place < 999) {
				$placeclass = 'double-digit';
			} else {
				$place = floor( $place / 1000 ) . 'K';
				$placeclass = 'triple-digit';
			}

			if($l->current_position) {
				//$position = $l->current_position;
				echo '<li class="row-highlight"><div class="user-place place-'.$placeclass.'"><span>'.$place.'</span></div><img class="user-pic user-pic-self" src="'.$l->profile_pic_cropped.'" /><span class="user-points">'.$l->total_points.'</span><h3>'.$l->username.'</h3></li>';
			} else {
				//$position = $k;
				echo '<li><div class="user-place place-'.$placeclass.'"><span>'.$place.'</span></div><img class="user-pic" src="'.$l->profile_pic_cropped.'" /><span class="user-points">'.$l->total_points.'</span><h3>'.$l->username.'</h3></li>';
			}
			
			$place = $place + 1;

			//echo '<li><span class="user-points">'.$l->total_points.'</span><h3>'.$k.'. '.$l->username.'</h3></li>';
			//echo '<li><span class="user-points">'.$l->total_points.'</span><h3>'.$l->current_position.'. '.$l->username.'</h3></li>';
			//echo '<li><span class="user-points">'.$l->total_points.'</span><h3>'.$position.'. '.$l->username.'</h3></li>';

		}
	} else {echo '<li class="centered"><p>No users yet.</p></li>';}
	echo '</ul>';
*/
?>