<?php

header('cache-control: no-cache');

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
	
	$poll = json_decode($returnText);

	echo '<table class="styled">';
	
		echo '<tr class="row"><td><p>'.$poll->question.'</p></td></tr>';
		
		if($poll->vote != null) {
			
			echo '<tr class="row"><td><p class="questionanswer">'.$poll->question_answer.'</p></td></tr>';
			
		}

	echo '</table>';
	
	if($poll->vote != null) {
		$addClass = 'row';
	} else {
		$addClass = 'link';
	}
	
	echo '<table class="styled" data-pid="'.$poll->pid.'">';
	
	foreach($poll->answers as $p) {
		
		if($poll->vote != null) {
			if($poll->vote == $p->answer) {
				$voteClass = ' vote-'.$p->type;
			} else {
				$voteClass = '';
				continue;
			}		
		} else {
			$voteClass = ' voteable';
		}
		echo '<tr class="'.$addClass.$voteClass.'"><td><h4>'.$p->answer.'</h4></td></tr>';
		
	}
	
	if($poll->vote != null) {
			
		echo '<table class="styled"><tr class="row"><td>';
		
			//print_r($poll);
			foreach($poll->results->answers as $k=>$v) {
				
				echo '<label>'.$k.':</label><div class="graph"><span class="graphbar" style="width: '.(($v / $poll->results->responses) * 100).'%"></span></div>';
				
			}
		
		echo '</td></tr></table>';
		
	}
	
	echo '</div>';
	
?>