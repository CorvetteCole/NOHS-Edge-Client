<?php
/*
stdClass Object
(
    [users] =&gt; stdClass Object
        (
            [1] =&gt; stdClass Object
                (
                    [uid] =&gt; 6
                    [total_points] =&gt; 597
                    [total_points_formatted] =&gt; 597
                    [username] =&gt; brandon
                    [profile_pic] =&gt; http://media.superfanu.com/profiles/6.1397507758.jpg
                    [profile_pic_cropped] =&gt; http://media.superfanu.com/profiles/6.1397507758_c100.jpg
                    [current_position] =&gt; stdClass Object
                        (
                            [rank] =&gt; 2
                        )

                )

            [2] =&gt; stdClass Object
                (
                    [uid] =&gt; 4
                    [total_points] =&gt; 120
                    [total_points_formatted] =&gt; 120
                    [username] =&gt; tmruddell
                    [profile_pic] =&gt; http://media.superfanu.com/profiles/4.1397508044.jpg
                    [profile_pic_cropped] =&gt; http://media.superfanu.com/profiles/4.1397508044_c100.jpg
                    [current_position] =&gt; stdClass Object
                        (
                            [rank] =&gt; 3
                        )

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
	
	$users = json_decode($returnText);

		echo '<ul data-role="listview" id="event-info-users-list">';
		if($users->users) {
			foreach($users->users as $u) {
			
				echo '<li><img class="user-pic" src="'.$u->profile_pic_cropped.'" /><span class="user-points">'.$u->total_points_formatted.'</span><h3>'.$u->username.'</h3></li>';


			}
		} else {
			echo '<li><h3>Hmmmm. Nothing, yet.</h3></li>';
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