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
	
	$pics = json_decode($returnText);
	
	//print_r($pics);

		//echo '<form id="upload-media" method="post" action="http://media.superfanu.com/api/2.0/upload_media.php" enctype="multipart/form-data"><progress id="progress-upload-media"></progress><input type="file" name="media" id="upload-media" accept="image/*" value=""></form>';
		
		if($pics) {
			$ind = 0;
			foreach($pics as $p) {
				
				//print_r($p);

				if($p->type == 'video') {
					echo '<a href="#" class="photo-s" data-ind="'.$ind.'" data-type="video">';
					echo '<video src="'.$p->media.'" class="landscape">Ah! No video!</video>';
					echo '<img src="assets/img/video-play-btn.png" class="video-play-btn"/>';
					echo '</a>';
				} elseif($p->type == 'image') {
					//echo '<img class="photo photo-'.$p->orientation.'" src="'.$p->media.'" />';
					echo '<a href="#" class="photo-s" style="background-image: url('.$p->media.')" data-ind="'.$ind.'" data-type="image"></a>';
				}

				$ind++;
			}
			
			for($i = 0; $i <= 5; $i++) {
				echo '<a href="" class="photo-blank"></a>';
			}
			
			echo '<div class="swipe-slick">';
			$b_ind = 0;
			foreach($pics as $p) {
				if($p->type == 'video') {
					echo '<div class="photo-l video-l" id="bind-'.$b_ind.'">
						<video controls src="'.$p->media.'">Ah! No video!</video>
						<span class="slider-bottom">
							<img class="pic" src="'.$p->profile_pic_cropped.'">
							<h3>'.$p->username.'</h3>
							<p>'.$p->text.'</p>
						</span>
					</div>';
				} elseif($p->type == 'image') {
					echo '<div class="photo-l" id="bind-'.$b_ind.'">
						<img src="'.$p->media.'" />
						<span class="slider-bottom">
							<img class="pic" src="'.$p->profile_pic_cropped.'">
							<h3>'.$p->username.'</h3>
							<p>'.$p->text.'</p>
						</span>
					</div>';
				}
				$b_ind++;

			}
			echo '</div>';
		}

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