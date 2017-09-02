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
	
	$notifications = json_decode($returnText);

	echo '<ul data-role="listview">';
	if($notifications) {
		foreach($notifications as $n) {
			if(preg_match("/\[\[poll ([a-zA-Z0-9]+)\]\]/", $n->full_message, $matches) == 1) {
				$pollId = $matches[1];
				$n->full_message = preg_replace("/\[\[poll ([a-zA-Z0-9]+)\]\]/", '', $n->full_message);
			} else {
				unset($pollId);
			}
			
			echo '<li>';
			echo '<p class="message">';
				if($n->full_message_image != '' && $n->full_message_image_alignment == 'top') {
					echo '<img src="'.$n->full_message_image.'" class="img-top" />';
				}
				if($n->full_message != '') {
					if(strpos($n->full_message, '[[video') !== false) {
						preg_match('/\[\[video (.*)\]\]/i', $n->full_message, $matches);
						if($matches) {
							preg_match('/https\:\/\/www\.youtube\.com\/watch\?v=(.*)/i', $matches[1], $video);
							if($video) {
								//it's youtube
								$v = '<iframe width="100%" height="215" src="'.str_replace('watch?v=','embed/',$video[0]).'?showinfo=0&showsearch=0&modestbranding=1" frameborder="0" allowfullscreen></iframe>';
								echo $v;
							} else {
								//some other
								$v = '<video controls style="width: 100%; height: 215px;"><source src="'.$matches[1].'">Your device does not support video.</video>';
								echo $v;
							}
						}
						$n->full_message = str_replace($matches[0], '', $n->full_message);
					}
					echo ereg_replace("[[:alpha:]]+://[^<>[:space:]]+[[:alnum:]/]","<a href=\"#\" class=\"link-track\" data-url=\"\\0\" data-title=\"\\0\" data-eid=\"0\" >\\0</a>", $n->full_message);
				} else {
					echo ereg_replace("[[:alpha:]]+://[^<>[:space:]]+[[:alnum:]/]","<a href=\"#\" class=\"link-track\" data-url=\"\\0\" data-title=\"\\0\" data-eid=\"0\" >\\0</a>", $n->message);
				}
				if($n->full_message_image != '' && $n->full_message_image_alignment == 'bottom') {
					echo '<img src="'.$n->full_message_image.'" class="img-bottom" />';
				}
			echo '</p>';
			if($pollId) {
				echo "<span class='notification-fanpoll' data-pollId='$pollId'>View Poll Here</span>";
			}
			echo '<p class="date">Sent '.$n->tx.'</span>';
			echo '</li>';
		}
	} else {
		echo '<li><h4>You have no notifications, yet.</p></h4>';
	}
	echo '</ul>';