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
	
	$profile = json_decode($returnText);
	
	function createDropDown($meta) {
		echo '<select name="meta['.$meta->label.']">';
			$ops = explode('|','|'. $meta->selector );
			foreach($ops as $o) {
				echo '<option value="'.addslashes($o).'"';
					if($o == $meta->tf) {echo ' selected="selected"';}
				echo '>'.$o.'</option>';
			}
		echo '</select>';
	}
	
?>

		<form class="block-styled" id="upload-profile-pic" method="post" action="http://media.superfanu.com/api/2.0/upload_profile_pic.php" enctype="multipart/form-data">
			<div class="profile-info" data-role="fieldcontain">
    			<img class="user-pic user-pic-self" src="<?php echo $profile->profile_pic_cropped; ?>" />
    			<p class="upload-user-pic">Change Profile Pic</p>
    			<progress id="progress-upload-user-pic"></progress>
    			<input type="file" name="media" id="upload-user-pic" accept="image/*" value="">
    			<input type="hidden" name="platform" class="input-platform" value="">
    			<input type="hidden" name="uuid" class="input-uuid "value="">
			</div>
		</form>
		<form class="block-styled" id="edit-profile-form">
			<div class="ui-field-contain">
    			<label>Username:</label>
    			<input type="text" name="username" value="<?php echo $profile->username; ?>"  />
			</div>
			<div class="ui-field-contain">
    			<label>Email:</label>
    			<input type="text" name="email" value="<?php echo $profile->email; ?>"  />
			</div>
			<div class="ui-field-contain">
    			<label>Name:</label>
    			<input type="text" name="name" value="<?php echo $profile->name; ?>"  />
			</div>
			<div class="ui-field-contain">
    			<label>Phone:</label>
    			<input type="text" name="phone" value="<?php echo $profile->phonepretty; ?>"  />
			</div>
			<div class="ui-field-contain">
    			<label>Address:</label>
    			<input type="text" name="address1" value="<?php echo $profile->address1; ?>"  />
			</div>
			<div class="ui-field-contain">
    			<label>Zip Code:</label>
    			<input type="text" name="zip" value="<?php echo $profile->zip; ?>"  />
			</div>
			<?php if($profile->meta) {
				foreach($profile->meta as $meta) { ?>
				<div class="ui-field-contain">
	    			<label><?php echo str_replace('_', ' ', $meta->label); ?>:</label>
		    		<?php if( $meta->selector <> '' ) {
						createDropDown($meta);
	    			} else { ?>
	    			<input type="text" name="meta[<?php echo $meta->label; ?>]" value="<?php echo $meta->tf; ?>"  />
	    			<?php } ?>
				</div>
			<?php
				}
			} ?>
		</form>
		
		<!-- <a href="#" id="update-profile-btn" class="ui-btn ui-corner-all major-btn">Update Profile</a> -->