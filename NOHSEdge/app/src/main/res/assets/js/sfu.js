var nid = 305;
var appVersion = '6.0.0';
//api
var http = 'https://api.superfanu.com/6.0.0/gen/';
var api_prefix = '';
var tkthttp = 'https://api.superfanu.com/tkt/1.0/';
var imghttp = 'http://media.superfanu.com/api/5.0/upload_media.php';
var profile_pic_http = 'http://media.superfanu.com/api/5.0/upload_profile_pic.php';
var mobile_site_url = 'http://sites.superfanu.com/nohsstampede/6.0.0/';
//app info
var appname = 'Stampede';
var networkName = 'North Oldham High School';
var networkNameAbbr = 'North Oldham High School';
//rss
var rssLink = '';
//facebook
var fbAppId = '1177017332372374';
var fbPermissions = 'email';
//homescreen
var homescreenBGImg = ['img/custom/homescreen.background.568.png'];
var homescreenBGVideo = []; //'img/custom/images/custom/depaul-zone.mp4'
var homescreenModulesImg = 'img/custom/homescreen.modules.568.png';
var homescreenSponsorImg = 'img/custom/homescreen.sponsor.568.png';
var home_touch_target_action = null;
var ssoURL = '';
var ssoLogout = '';
var SPaction = '';
var ssoLoggedIn = false;
var localCookieStorage = {};
var platform, uuid, platformVersion, phoneModel, ostype, isJailbroken, firstLoad,
    login_key, user, loggedIn, rooturl, fbPost, fbPosting, fbReady, fbLast, isBuyingPrize,
    twPost, scoreEIDSelected, soundboardIsPlaying, soundboardSounds, taggedTicketEvents,
    taggedTicketEventsCount, canUseBeacons, beaconList, individualBeaconNotification;

$(document).ready( function(){
	//set the api_prefix immediately
	if( getVar('platform')=="Android" ) {
		api_prefix = mobile_site_url;
		console.log("getting local cookie storage mainactivity");
	} else {
		api_prefix = '';
	}

        if( getVar('uuid') != undefined ) {uuid = getVar('uuid'); console.log("UUID is" + uuid)}
    platform = 'Web: '+$.browser.name;
        if( getVar('platform') != undefined ) {console.log("abosut to set platfrom: " + platform); platform = getVar('platform'); console.log("abosut to get platfrom: " + platform);}
    uuid = getCookie('UUID');
    platformVersion = $.browser.version;
        if( getVar('platformVersion') != undefined ) {platformVersion = getVar('platformVersion');}
    phoneModel = 'Web: '+$.browser.name;
        if( getVar('phoneModel') != undefined ) {phoneModel = decodeURIComponent( getVar('phoneModel') ).replace(/\+/g,' ');}
    ostype = 'Unknown';
        if( getVar('architecture') != undefined ) {ostype = decodeURIComponent( getVar('architecture') ).replace(/\+/g,' ');}
    isJailbroken = 0;
        if( getVar('isJailbroken') != undefined && getVar('isJailbroken') == 'true') {isJailbroken = 1;}
    firstLoad = getCookie('firstLoad');
        if( getVar('firstLoad') != undefined && getVar('firstLoad') == 'true' && getCookie('firstLoad') != 'false') {firstLoad = true;} else {firstLoad = false;}
        console.log(`COOOOKIES: firstLoad: ${firstLoad}`);
        setCookie('firstLoad','false',365);
        if( getVar('forceTutorial') == 'true' ) {
            firstLoad = true;
        }
    login_key;
        if( getCookie('login_key') ) {
            login_key = getCookie('login_key');
        }
    user = {};

    rooturl = document.URL;
        rooturl = rooturl.split('/');
        rooturl.pop();
        rooturl = rooturl.join('/')+'/scripts/';
    //facebook
    fbPost = {};
    fbPosting = false;
    fbReady = false;
    fbLast = {};
        fbLast.action = null;
        fbLast.success = function() {};
        fbLast.failure = function() {};
    //prize store
    isBuyingPrize = false;
    //twitter
    twPost = {};
    //scores
    scoreEIDSelected = null;
    //soundboard
    soundboardIsPlaying = false;
    soundboardSounds = [];
    //tickets
    taggedTicketEvents = false;
    taggedTicketEventsCount = 0;
    //ibeacons
    canUseBeacons = false;
    beaconList = {};
	//individual beacon notification
	individualBeaconNotification = false;
	if( getVar('individualBeaconNotification') == 'true' ) {
		individualBeaconNotification = getVar('bid');
	}
	console.log("Done");
    console.log("done loading document sfu.js");
	$.mobile.defaultPageTransition = 'none';

	// Setup. Refresh Modules.
	$(document).on("moduleRefresh", function() {
		checkForNewHomescreen();
		refreshNotifications();
		refreshBeacons();
		setupCanUseBeaconToggle();
		refreshHistory();
		refreshFAQ();
		refreshEvents();
refreshRewards();
refreshProfile();
refreshFanCam();
refreshLeaderboard();
refreshScores();
refreshRosters();
refreshNOHSStudentID();

        console.log("Refreshing modules");
	});

    if( getVar('platform')=="Android" ) {
        refreshLocalCookieStorage();
        console.log("getting local cookie storage mainactivity");
    } else {
		//FB
		fbInit();
	
		//check login
		checkLogin();
    }

	//ON INDIVIDUAL PAGE LOAD
	$("div[data-role='page']").bind('pageshow',function(e, ui){
		if( $(this).attr('data-pagekey') != undefined ) {
			if( $.mobile.navigate.history.getNext() == undefined ) {
				//this check is necessary to
				//make we're not going backwards
				//because we don't want to double grab
				//the ads going both back and forth
				checkForAds($(this).attr('data-pagekey'), $(this).attr('id'));
			}
		}
	});
	
	//LOADING PAGE
	$('body').on('pageshow','#loading',function(){
		if(loggedIn == true) {
			$.mobile.changePage( '#homescreen' );
		}
	});
	
	//TUTORIAL
	$('body').on('pageshow','#tutorial',function(){
		$('#tutorial .tutorial-slides').slick({
			arrows: false,
			onInit: function() {
				console.log('slick init');
			},
			onReInit: function() {
				console.log('slick reinit');
			},
			infinite: false,
		}).slickGoTo(0);
	});
	
	$('.tutorial-next').click(function() {
		$('.tutorial-slides').slickNext();
	});
	
	$('.login-return').click(function() {
		if(ssoURL != false && ssoLogout != false && SPaction != false) {
			$.mobile.changePage('#single-sign-on-login');
		} else {
			$.mobile.changePage('#login');
		}

	});
	
	//LOGIN
	$('body').on('pageshow','#login',function(){
		if(loggedIn == true) {
			$.mobile.changePage( '#homescreen' );
			if( getVar('platform')=="Android" ) {
				window.location = "updateapid:/"+nid+"/"+user.uid;
			}
			return true;
		} else {
			//deleteCookie('UUID');
			$.post(api_prefix+'scripts/ajax.php', {url: http+'logout.php?nid='+nid,platform: platform, uuid: getCookie("UUID"), login_key: getCookie("login_key")});
			loggedIn = false;
			if( individualBeaconNotification != false) {
				showIndividualBeaconNotfication();
			}
		}
	});
	$('#fb-login-btn').click(function() {
		$('#fb-login-btn p').text('Logging in...');
		if( getVar('platform') == "Android" ) {
			window.location = 'fblogin://';
		} else {
			fbLast.action = 'login';
			FB.getLoginStatus(function(response) {
				if (response.status === 'connected') {
					loginViaFacebook();
				} else {
					console.log('not logged in, but gonna attempt it');
					fbLogin( function() {$('#fb-login-btn p').text('Login with Facebook');} , function() {$('#fb-login-btn p').text('Login with Facebook');});
				}
			});
		}
	});
	$('#single-sign-on-facebook-login-btn').click(function() {
		$('#single-sign-on-facebook-login-btn p').text('Logging in...');
		if( getVar('platform') == "Android" ) {
			window.location = 'fblogin://';
		} else {
			fbLast.action = 'login';
			FB.getLoginStatus(function(response) {
				if (response.status === 'connected') {
					loginViaFacebook();
				} else {
					console.log('not logged in, but gonna attempt it');
					fbLogin( function() {$('#single-sign-on-facebook-login-btn p').text('Login with Facebook');} , function() {$('#single-sign-on-facebook-login-btn p').text('Login with Facebook');});
				}
			});
		}
	});
	FB.Event.subscribe('auth.authResponseChange', function(response) {
		if (response.status === 'connected') {
			$('img.facebook-login-logout-btn').attr('src', 'img/fb-settings-logout-btn.png');
			if(fbLast.action == 'share') {
				var uid = response.authResponse.userID;
			    var accessToken = response.authResponse.accessToken;
			    postToFB();
			}
			if(fbLast.action == 'login') {
				loginViaFacebook();
			}
			if(fbLast.action == 'register') {
				loginViaFacebook();
			}
		} else {
			$('img.facebook-login-logout-btn').attr('src', 'img/fb-settings-login-btn.png');
		}
	});
	$('body').on('click', '#login-btn',  function() {
		var user = $('#login-username').val();
		var pass = $('#login-password').val();
		$('#login-btn').text('Logging in...');
		$.post(api_prefix+'scripts/ajax.php', {url: http+'new_session.php?num=64'}, function(data) {
			console.log(data);
			if(getVar('uuid')==undefined) {uuid = data;} else {uuid = getVar('uuid');}
			setCookie('UUID',uuid,365);
			$.post(api_prefix+'scripts/login.php', {url: http+'login.php?nid='+nid, user: user, pass: pass, platform: platform, uuid: getCookie("UUID"), version: platformVersion, app_version: appVersion, architecture: ostype, model: phoneModel, osname: platform, ostype: "32bit", is_jailbroken: isJailbroken}, function(data) {
				console.log(data);
				var json = jQuery.parseJSON(data);
				if(json.response=='ok') {
					//$.mobile.changePage( '#events' );
					user = json.user;
					updateLoginKey( json.loginkey );
					$.event.trigger({type: "moduleRefresh"});
					$.mobile.changePage( '#homescreen' );
					if( getVar('platform')=="Android" ) {
						window.location = "updateapid:/"+nid+"/"+user.uid;
					} 
					$('#login-username').val('');
					$('#login-password').val('');
					$('#login-btn').html('Login');
					loggedIn = true;
					
				} else {
					$('#login-password').val('');
					alert(json.error);
					$('#login-btn').html('Login');
				}
			});
		});
	});
	
	//FORGOT PASSWORD
	$('#forgot-password-submit-btn').click(function() {
		$('#forgot-password-submit-btn').text('Resetting...');
		$.post(api_prefix+'scripts/ajax.php', {url: http+'forgot_password.php?nid='+nid, platform: platform, uuid: getCookie("UUID"), login_key: getCookie("login_key"), email: $('#forgotPasswordTF').val()},  function(data) {
			var json = jQuery.parseJSON(data);
			if(json.response=='ok') {
				alert(json.msg);
				//$.mobile.changePage( '#login', { reverse: 'true'} );
				$.mobile.back();
				$('#forgotPasswordTF').val('');
				$('#forgot-password-submit-btn').text('Recover Password');
			} else {
				alert(json.msg);
				$('#forgot-password-submit-btn').text('Recover Password');
			}
				
		});
    });
	
	//REGISTER
	$('body').on('pageshow','#register',function(){
		if(loggedIn == true) {
			$.mobile.changePage( '#homescreen' );
			if( getVar('platform')=="Android" ) {
				window.location = "updateapid:/"+nid+"/"+user.uid;
			}
			return true;
		} else {
			//deleteCookie('UUID');
			$.post(api_prefix+'scripts/ajax.php', {url: http+'logout.php?nid='+nid,platform: platform, uuid: getCookie("UUID"), login_key: getCookie("login_key")});
			loggedIn = false;
		}
	});
	$('#fb-register-btn').click(function() {
		$('#fb-register-btn p').text('Registering...');
		if( getVar('platform') == "Android" ) {
			window.location = 'fbregister://';
		} else {
			fbLast.action = 'register';
			FB.getLoginStatus(function(response) {
				if (response.status === 'connected') {
					loginViaFacebook();
				} else {
					console.log('not logged in, but gonna attempt it');
					fbLogin( function() {$('#fb-register-btn p').text('Register with Facebook');} , function() {$('#fb-register-btn p').text('Register with Facebook');});
				}
			});
		}
	});
	$('body').on('click','#register-btn', function() {
		var email = $('#register-email').val();
		var user = $('#register-username').val();
		var pass = $('#register-password').val();
		$('#register-btn').text('Registering...');
		$.post(api_prefix+'scripts/ajax.php', {url: http+'new_session.php?num=64'}, function(data) {
			console.log(data);
			if(getVar('uuid')==undefined) {uuid = data} else {uuid = getVar('uuid')};
			setCookie('UUID',uuid,365);
			$.post(api_prefix+'scripts/login.php', {url: http+'register.php?nid='+nid, email:email, user: user, pass: pass, platform: platform, uuid: getCookie("UUID"), version: platformVersion, app_version: appVersion, architecture: ostype, model: phoneModel, osname: platform, ostype: "32bit", is_jailbroken: isJailbroken}, function(data) {
				try {
					var json = jQuery.parseJSON( data );
				} catch(e) {
					alert('We\'re having trouble connecting to the network. Please try again in a moment.');
					return false;
				} 
				if(json.response=='ok') {
					//$.mobile.changePage( '#events' );
					updateLoginKey( json.loginkey );
					$.mobile.changePage( '#homescreen' );
					if(confirm(json.alert.message)) {$.mobile.changePage( '#profile-edit' );}
					$.event.trigger({type: "moduleRefresh"});
					if( getVar('platform')=="Android" ) {
						window.location = "updateapid:/"+nid+"/"+user.uid;
					} 
					$('#register-email').val('');
					$('#register-username').val('');
					$('#register-password').val('');
					$('#register-btn').text('Register');
					loggedIn = true;
				} else {
					alert(json.alert.message);
					$('#register-btn').text('Register');
				}
			});
		});
	});
	
	function shibboleth(data) {
		ssoLoggedIn = true;
/*
		alert("trying");
		alert(data);
*/
		$.mobile.changePage( '#homescreen' );
		//$('#register-btn').trigger('click');
	}
	
	//HOMESCREEN
	$( "#homescreen" ).on( "pagecontainerload", function( event, ui ) {
		console.log('individualBeaconNotification is: '+individualBeaconNotification);
		if( individualBeaconNotification != false ) {
			showIndividualBeaconNotfication();
		}
	});
	$('body').on('click','#notification-view', function() {
	    $.mobile.changePage( '#notifications' );
	    $('#notification-view').hide();
	    $.post(api_prefix+'scripts/read-notifications.php', {url: http+'read_notifications.php?nid='+nid, platform: platform, uuid: getCookie("UUID"), login_key: getCookie("login_key")},  function(data) {
	    	console.log('read notification response: '+data);
	    });
    });
	$('body').on('click','.notification-fanpoll', function() {
		var pid = $(this).attr('data-pollId');
		$.post(api_prefix+'scripts/fanpoll.php', {url: http+'get_poll_from_id.php?nid='+nid, platform: platform, uuid: getCookie("UUID"), login_key: getCookie("login_key"), pid: pid},  function(data) {
			$('#fanpoll .ui-content').html(data).find('ul').listview().listview('refresh');
		});
	    $.mobile.changePage('#fanpoll');
    });
    $('body').on('click','#homescreen-touch-target', function() {
		//console.log('homescreen-touch-target is: '+home_touch_target_action);
	    if( home_touch_target_action !== null ) {
		    var act = home_touch_target_action.split("|");
		    if( act[0] == 'url' ) {

				//have the URL, just need to track it
				$.post(api_prefix+'scripts/ajax.php', {url: http+'link_out.php?nid='+nid, platform: platform, uuid: getCookie("UUID"), login_key: getCookie("login_key"), evurl: act[1], eid: 0}, function(data) {
					//console.log( data );
				});
				if(platform == 'Android') {
					window.location = 'modal:'+act[1];
				} else {
					window.open( act[1] , '', '');
				}

		    } else if( act[0] == 'lkey' ) {
			    
				if(platform == 'Android') {
					window.location = 'modal:'+http+'link_track.php?platform='+platform+'&uuid='+getCookie("UUID")+'&nid='+nid+'&lkey='+act[1];
				} else {
					window.open( http+'link_track.php?platform='+platform+'&uuid='+getCookie("UUID")+'&nid='+nid+'&lkey='+act[1] , 'Link Out', '');
				}
		    
		    } else if( act[0] == 'event' ) {
			    
			    getEventInfo( 'event-'+act[1] );
			    $.mobile.changePage( '#event-info' );
			    
			} else if( act[0] == 'module' ) {
				
				$.mobile.changePage( '#'+act[1] );
				
			} else if( act[0] == 'fire' ) {
			    
			    //TODO: not even sure what to do here
			    
		    }
	    }
    });
    
    //EVENTS
    $('body').on('click','h1 span.events-tab',function(e) {
		var _b = $(this);
		if( _b.hasClass('active') ) {
			//do nothing
		} else {
			$('h1 span.events-tab').removeClass('active');
			_b.addClass('active');
			$('.upcoming-event').hide();
			$('.upcoming-event.tag-'+_b.attr('data-event') ).show();
			$('.mini_calendar table td').removeClass('visible');
				$('.mini_calendar table td[data-tag="tag-'+_b.attr('data-event')+'"]').addClass('visible');
		}
	});
	$('body').on('click','#calendar-events',function(e) {
		$('#events-calendar').toggle();
	});
	$('body').on('click','#events-calendar .mini_calendar .event.visible',function(e) {
		var anchor = $(this).attr('data-anchor');
		$.mobile.silentScroll( $("#"+anchor).offset().top );
		$('#events-calendar').hide();
	});
	$('body').on('click','#events-calendar',function(e) {
		$('#events-calendar').hide();
	});
	// Moves the view to event info when clicking on an upcoming event
    $('body').on('click','li.upcoming-event',function(e) {
		getEventInfo( $(this).attr('id') );
		$.mobile.changePage( '#event-info' );
	});
	// Hearts and unhearts items when the heart-input checkbox is toggled
    $('body').on('click','div.heart-input',function(e) {
	    e.stopPropagation();
		var eventid = $(this).attr('data-eid');
		var hearted = $(this).attr('data-hearted');
		if(hearted == "true") {
			$(this).attr('data-hearted', "false");
		} else {
			$(this).attr('data-hearted', "true");
		}
		
		var source = $(this).attr('data-source');
		if(source == 'event-info') {
			var eventsDisplay = $('#event-' + eventid).find('.heart-input');
			var eventsHearted = $(eventsDisplay).attr('data-hearted');
			if(eventsHearted == "true") {
				$(eventsDisplay).attr('data-hearted', "false");
			} else {
				$(eventsDisplay).attr('data-hearted', "true");
			}
		}
		$.post(api_prefix+'scripts/ajax.php', {url: http+'heart.php?nid=' + nid, platform: platform, uuid: getCookie("UUID"), login_key: getCookie("login_key"), hkey: 'event', hvalue: eventid}, function(data) {
			var rMsg = $.parseJSON(data);
			console.log(rMsg);
		});
	});
	$('body').on('click', '.public-tag-list-item', function(e) {
		if(platform != 'Android') {
			var cal = $(this).attr('data-cal');
			var token = $(this).attr('data-token');
			var confirmationText = "Would you like to subscribe to events from the \"" + cal + "\" feed?";
			if( confirm(confirmationText) ) {
// 				window.location = 'addtocalendar:/'+cal.attr('data-name')+'/'+cal.attr('data-description')+'/'+cal.attr('data-notes')+'/'+cal.attr('data-location')+'/'+cal.attr('data-starttime')+'/'+cal.attr('data-endtime');
				window.location = 'webcal://api.superfanu.com/calendar/ical.php?type=ics&token=' + token;
// 				alert('webcal://api.superfanu.com/calendar/ical.php?type=ics&token=' + token);
			}
		} else {
			alert('Sorry, your device is not capable of downloading and installing calendar files directly.');
		}
	});
	$('body').on('click','#refresh-events',function(e) {
		refreshEvents();
	});
	$('body').on('click', '.checkin-btn', function(e) {
		var eventid = $(this).attr('data-eid');
		$(this).text('Checking In...');
		if(navigator.geolocation) {
			var count = 0;
			var watchGeo = navigator.geolocation.watchPosition(function(position) {
				if(position.coords.accuracy <= 500 || count==4) {
					navigator.geolocation.clearWatch(watchGeo);
					$.post(api_prefix+'scripts/ajax.php', {url: http+'check_in.php?nid='+nid, eventid: eventid, platform: platform, uuid: getCookie("UUID"), login_key: getCookie("login_key"), latitude: position.coords.latitude, longitude: position.coords.longitude, accuracy: position.coords.accuracy}, function(data) {
						var rMsg = $.parseJSON(data);
						console.log(rMsg);
						getEventInfo('event-'+eventid);
						$.event.trigger({type: "moduleRefresh"});
						alert(rMsg.alertMsg);
						count = 0;
					});
				} else {count = count + 1;}
			}, function(error) {
				if(error.code == 1) {
					alert("Geolocation is currently disabled. Please enable it in your browser's settings and try again.");
				} else {
					window.location = "checklocationpermissions:/";
				}
				$("#checkin-btn").text('Check In to this Event');// <== was changed for the bats
				navigator.geolocation.clearWatch(watchGeo);
			}, { frequency: 5000, enableHighAccuracy: true });
		} else {
			alert("Sorry, but geolocation services are not supported by your phone's browser.");
		}
	});
	$('body').on('click','.add-to-calendar', function() {
	    var cal = $(this).parent().parent().find('.ui-content .event-info');
	    if(platform == 'Android') {
			if( confirm("Would you like to add this event to your calendar?") ) {
				window.location = 'addtocalendar:/'+cal.attr('data-name')+'/'+cal.attr('data-description')+'/'+cal.attr('data-notes')+'/'+cal.attr('data-location')+'/'+cal.attr('data-starttime')+'/'+cal.attr('data-endtime');
			}
		} else {
			alert('Sorry, your device is not capable of downloading and installing calendar files directly.');
		}
    });
    $('body').on('click','.venue', function() {
	    var v = $(this);
	    if(platform == 'Android') {
			window.open( 'map:/'+v.attr('data-lat')+'/'+v.attr('data-lng')+'/'+v.attr('data-name') );
		} else {
			window.open( 'http://maps.google.com?q='+v.attr('data-lat')+','+v.attr('data-lng'), v.attr('data-name'), '');
		}
    });
    $('body').on('click','.social-twitter', function() {
		var tw = $(this);
		twPost = {
		    tweetString: tw.attr('data-message').replace(/\\(.)/mg, "$1"),
		    image: tw.attr('data-picture').replace(/\\(.)/mg, "$1"),
		    eid: tw.attr('data-eid').replace(/\\(.)/mg, "$1")
		};
	});
    $('body').on('click','.social-facebook', function() {
		var fb = $(this);
		fbLast.action = 'share';
		
		fbPost = {
		    //method: 'feed', //v2.0
		    method: 'feed', //v2.2
		    name: fb.attr('data-message').replace(/\\(.)/mg, "$1"),
		    link: fb.attr('data-link').replace(/\\(.)/mg, "$1"),
		    picture: fb.attr('data-picture'),
		    caption: fb.attr('data-name').replace(/\\(.)/mg, "$1") + ' - ' + fb.attr('data-caption').replace(/\\(.)/mg, "$1"),
		    description: fb.attr('data-description').replace(/\\(.)/mg, "$1"),
		    eventid: fb.attr('data-eid')
		};
		
		FB.getLoginStatus(function(response) {
			if (response.status === 'connected') {
			    var uid = response.authResponse.userID;
			    var accessToken = response.authResponse.accessToken;
			    postToFB();
			} else if (response.status === 'not_authorized') {
			    // the user is logged in to Facebook, 
			    // but has not authenticated the fb app
			    console.log('logged into facebook, but not authenticated');
				fbLogin(function() {}, function() {});
			} else {
				console.log('not logged in, but gonna attempt it');
				fbLogin(function() {}, function() {});
			}
		});
	});
	FB.Event.subscribe('statusChange', function(response) {
		facebookStatus(response.status);
	});
	$('body').on('click', '.event-info-users', function() {
		getEventInfoUsers( $(this).attr('data-eid') );
		$.mobile.changePage( '#event-info-users' );
	});
    
    //AWARDS
    $('body').on('click','h1 span.awards-tab',function(e) {
		var _b = $(this);
		if( _b.hasClass('active') ) {
			//do nothing
		} else {
			$('h1 span.awards-tab').removeClass('active');
			_b.addClass('active');
			$('#rewards').hide();
			$('#prize-store').hide();
			$('#'+_b.attr('data-show')).show();
		}
	});
    $('body').on('click','#refresh-rewards',function(e) {
		refreshRewards();
	});
    $('body').on('click', '#awards .user-points', function(e) {
	    $.mobile.changePage( '#history' );
    });
    $('body').on('click','.award-unlocked',function(e) {
		setAwardDetail( $(this) );
		$.mobile.changePage( '#award-detail' );
	});
	$('body').on('click', '#award-detail img.qr-icon', function(e) {
		$('#award-detail img.qr-icon').hide();
		$('#award-detail img.barcode').show();
	});
	
	$('body').on('click','.award-redeemable',function(e) {
		var cid = $(this).attr('data-cid');
		if( confirm("An administrator will tap Redeem for you when your award is ready. If you are not an administrator, tap Later.\nIssued: "+user.name+" (" + user.username+")") ) {
			redeemAward( cid );
		}
	});
	
	//PRIZE STORE
    $('body').on('click','#refresh-prizestore',function(e) {
		refreshPrizeStore();
	});
    $('body').on('click','.prize-store-prize',function(e) {
		setPrizeStoreDetail( $(this) );
		getPrizeStoreRedeemableItems( $(this).attr('data-pid') );
		$.mobile.changePage( '#prize-store-detail' );
	});
	$('body').on('click', '#buy-prize-btn', function(e) {
		var pid = $(this).attr('data-pid');
		if( pid != 0 ) {
			if(isBuyingPrize == false) {
				if( confirm("Are you sure? Purchasing this prize will deduct "+$(this).attr('data-points-needed')+" points from your account!") ) {
					buyPrize( pid, $(this) );
				}	
			}
		}
	});
	$('body').on('click', '#prize-store-detail .redeem-item-btn', function(e) {
		var pid = $(this).attr('data-pid');
		var prid = $(this).attr('data-prid');
		if( !$(this).hasClass('redeeming') ) {
			$(this).addClass('redeeming');
			if( confirm("Administrators use this to keep track of what you have already picked up. If you have not picked this item up, tap Later. Pick up now?") ) {
				prizeStoreRedeemItem( pid, prid, $(this) );
			} else {
				$(this).removeClass('redeeming');
			}
		}	
	});
	
	//LEADERBOARD
	$('body').on('click','#refresh-leaderboard',function(e) {
		refreshLeaderboard();
	});
	$('body').on('change','#leaderboard input:radio',function(e){
	    $('#leaderboard .ui-content').html( decodeURIComponent( $(this).attr('data-html') ) ).trigger('create');
	});
	
	//FANCAM
	$('body').on('click', '#add-fancam', function() {
		window.location = "checkreadstoragepermissions:/";
    });
    $('#upload-fancam-media').on('change', '#upload-media', function() {
	  console.log('submitting');
	  $('#media-progress').show();
	   
	  var formData = new FormData( $('#upload-media') );
	  var fd = new FormData();    
	  fd.append( 'media', $('#upload-media')[0].files[0]/* $('#upload-profile-pic')[0][0].files[0] */ );
	  fd.append( 'platform', platform );
	  fd.append( 'uuid', getCookie("UUID") );
	  	
	  	
	  	$.ajax({
	        url: imghttp+'?nid='+nid,  //Server script to process data
	        type: 'POST',
	        xhr: function() {  // Custom XMLHttpRequest
	            var myXhr = $.ajaxSettings.xhr();
	            if(myXhr.upload){ // Check if upload property exists
	                myXhr.upload.addEventListener('progress',uploadFanCamPicProgress, false); // For handling the progress of the upload
	            }
	            return myXhr;
	        },
	        //Ajax events
	        beforeSend: function() {},
	        success: function(data) {
	        	$('#media-progress').hide();
		        console.log('success');
		        console.log(data);
				refreshFanCam(nid);
		        
	        },
	        error: function(e) {
		        $('#media-progress').hide();
		        console.log('error');
		        console.log(e);
		        alert('There was an error uploading your pic. Please try again.');
	        },
	        // Form data
	        data: fd,
	        //dataType: 'jsonp',
	        //Options to tell jQuery not to process data or worry about content-type.
	        cache: false,
	        contentType: false,
	        processData: false
	    });
	   
	   
    });
    $('body').on('click','#fancam .photo-s',function(e) {
		var ind = $(this).attr( 'data-ind' );
		$('#fancam-single .ui-content .swipe-slick').slickGoTo( ind );
		$.mobile.changePage( '#fancam-single' );
		if( $(this).attr( 'data-type' ) == 'video' ) {
			//start playing the video!
			$('#bind-'+ind+' video').trigger('play');
		}
	});
	
	//VENUE LIST
	$('body').on('keyup','#venue-filter',function(e){
	    var k = $(this).val();
	    	var m = new RegExp(k, 'i');
	    	console.log('filtering: '+k);
	    if( k == '' ) {
		    $('.venue-list tr').show();
	    } else {
		    $('.venue-list tr').each(function() {
			    //console.log( $(this).attr('data-filter') );
				if( $(this).attr('data-filter') ) {
					if( $(this).attr('data-filter').match(m) ) {
						$(this).show();
					} else {
						$(this).hide();
					}
				}
		    });
	    }
	}).on('change','#venue-filter',function(e) {
		if( $(this).val() == '') {
			$('.venue-list tr').show();
		}
	});

	
	///DYNAMIC TABS
	$('body').on('change','#dynamic-text-tabs input:radio',function(e){
	    $('#dtt-content').html( decodeURIComponent( $(this).attr('data-html') ) ).trigger('create');
	});
	
	//NEWS
	$('body').on('click','#refresh-news',function(e) {
		refreshNews();
	});
	
	//FANPOLL
	$('body').on('click','tr.voteable', function() {
		$.post(api_prefix+'scripts/fanpoll-vote.php', {url: http+'poll_vote.php?nid='+nid, platform: platform, uuid: getCookie("UUID"), login_key: getCookie("login_key"), pid: $(this).parent().parent().attr('data-pid'), answer: $(this).find('h4').html()}, function(data) {
			//alert(data);
			console.log(data);
			if(data=='ok') {
				refreshFanPoll();
			} else {
				alert(data);
			}
		});
	});
	
	//PROFILE
	$('body').on('click','.logoutBtn', function() {
		if(ssoLoggedIn) {
			var w = window.open(ssoLogout, "popupWindow", "width=600, height=400, scrollbars=yes")
			$(w.document).ready(function(data) {
				$.mobile.changePage('#single-sign-on-login');
				loggedIn = false;
			});
		} else {
			loggedIn = false;
			console.log("Logged out; loggedIn is "+loggedIn);
			$.mobile.changePage('#login');
		}
	});
	$('body').on('click', 'img.facebook-login-logout-btn', function() {
		FB.getLoginStatus(function(response) {
			if (response.status === 'connected') {
				//logout
				FB.logout();
				$('img.facebook-login-logout-btn').attr('src', 'img/fb-settings-login-btn.png');
			} else {
				//login
				fbLast.action = null;
				fbLogin(function() {
					$('img.facebook-login-logout-btn').attr('src', 'img/fb-settings-logout-btn.png');
				}, function() {});
			}
		});
	});
	
	$('body').on('change', '#canUseBeaconToggle', function() {
		if(platform=='Android') {
			window.location='updateCanUseBeacon:/'+$('#canUseBeaconToggle').is(':checked');
			refreshBeacons();
		}
	});
	
	//PROFILE EDIT
	$('body').on('click', '.upload-user-pic', function() {
	    console.log('synthesizing click');
	    $('#upload-user-pic').click();
    });
	$('body').on('change','#upload-user-pic', function() {
		console.log('submitting');
		$('#profile-edit .profile-info p').hide();
		$('#profile-edit .profile-info progress').show();
		var formData = new FormData( $('#upload-profile-pic') );
		var fd = new FormData();    
		fd.append( 'media', $('#upload-user-pic')[0].files[0] );
		fd.append( 'platform', platform );
		fd.append( 'uuid', getCookie("UUID") );
	  	console.log( profile_pic_http+'?nid='+nid );
	  	$.ajax({
	        url: profile_pic_http+'?nid='+nid,
	        type: 'POST',
	        xhr: function() {
	            var myXhr = $.ajaxSettings.xhr();
	            if(myXhr.upload){
	                myXhr.upload.addEventListener('progress',uploadProfilePicProgress, false);
	            }
	            return myXhr;
	        },
	        beforeSend: function() {},
	        success: function(data) {
	        	$('#progress-upload-user-pic').hide();
	        	$('#profile-edit .profile-info p').show();
		        console.log('success');
		        console.log(data);
		        var json = jQuery.parseJSON(data);
		        if(json.response == 'ok') {
			        $('img.user-pic-self').attr('src', json.profile_pic_cropped );
		        } else {
			        alert( json.error );
		        }
		        
	        },
	        error: function(e) {
		        $('#progress-upload-user-pic').hide();
	        	$('#profile-edit .profile-info p').show();
		        console.log('error');
		        console.log(e);
		        alert('There was an error uploading your pic. Please try again.');
	        },
	        data: fd,
	        cache: false,
	        contentType: false,
	        processData: false
	    });
    });
    $('body').on('click','#update-profile-btn', function() {
		$('#update-profile-btn').text('Saving...');
		console.log('nid: '+nid+', platform: '+platform+', uuid: '+getCookie("UUID")+', login_key: '+login_key);
		var formElements = $('#edit-profile-form').serialize();
		$.post(api_prefix+'scripts/update_profile.php', {url: http+'update_profile.php?nid='+nid, platform: platform, uuid: getCookie("UUID"), login_key: getCookie("login_key"), formdata: formElements}, function(data) {
			console.log(data);
			try {
				var json = jQuery.parseJSON( data );
			} catch(e) {
				$('#update-profile-btn').text('Save');
				alert("We're having trouble connecting to the network. Please try again in a moment.");
				return false;
			} 
			if(json.response=='ok') {
				if(json.message!='' && json.message != null) {
					alert( json.message );
				}
				refreshProfile();
				//$.mobile.changePage( '#profile', { reverse: 'true'} );
				$.mobile.back();
				$('#update-profile-btn').text('Save');
			} else {
				$('#update-profile-btn').text('Save');
				alert(json.message);
			}
		});
	});
	
	//MYQR CODE
	$('body').on('pageshow','#myqr-code',function(){
		$('#myqr-code img.qr').attr('src', tkthttp + 'my-qr?encode=' + user.email);
	});
	
	//NOHS STUDENT ID
	$('body').on('pageshow','#nohs-student-id',function(){
		refreshNOHSStudentID();
	});
	
	$('body').on('click','#refresh-sports-pass',function(){
		refreshNOHSStudentID();
	});
	
	//HELP / FEEDBACK
	$('#send-feedback-btn').click(function(e) {
		if( $('#feeback-text').val() == '' ) {
			alert('You have to type something before you can send it');
			return false;
		}
		$.post(api_prefix+'scripts/ajax.php', {url: http+'feedback.php?nid='+nid, platform: platform, uuid: getCookie("UUID"), login_key: getCookie("login_key"), feedback_text: $('#feeback-text').val(), version: platformVersion, app_version: appVersion, ostype: ostype, phone_model: phoneModel, osname: platform, ostype: "32bit", is_jailbroken: isJailbroken},  function(data) {
			try {
				var json = jQuery.parseJSON( data );
			} catch(e) {
				alert("We're having trouble connecting to the network. Please try again in a moment.");
				return false;
			} 
			if(json.response=='ok') {
				if(json.message!='' && json.message != null) {
					alert( json.message );
				}
				$.mobile.back();
			} else {
				alert(json.message);
			}
		});
	});
	
	function refreshFAQ() {
        $.post(api_prefix+'scripts/faq.php', {url: http+'get_faq.php?nid='+nid, platform: platform, uuid: getCookie("UUID"), login_key: getCookie("login_key"), tkthttp: tkthttp},  function(data) {
            $('#help-faq .ui-content').html("<h4>Frequently Asked Questions</h4>" + data);
        });
	}

	var acc = document.getElementsByClassName("accordion");
	var i;
	
	for (i = 0; i < acc.length; i++) {
	  acc[i].onclick = function() {
	    this.classList.toggle("active");
	    var panel = this.nextElementSibling;
	    if (panel.style.maxHeight){
	      panel.style.maxHeight = null;
	    } else {
	      panel.style.maxHeight = panel.scrollHeight + "px";
	    } 
	  }
	}	
	//PROMO
	$('#promo-submit-btn').click(function() {
		$('#promo-submit-btn').text('Checking...');
		$.post(api_prefix+'scripts/ajax.php', {url: http+'promo-code.php?nid='+nid, platform: platform, uuid: getCookie("UUID"), login_key: getCookie("login_key"), invite: $('#promoTF').val()},  function(data) {
	    	var json = jQuery.parseJSON(data);
		        if(json.response == 'ok') {
					alert(json.msg);
					$('#promoTF').val('');
					$('#promo-submit-btn').text('Submit');
					//$.mobile.changePage( '#profile', { reverse: 'true'} );
					$.mobile.back();
				} else if(json.response = 'error') {
					alert(json.errormsg);
					$('#promo-submit-btn').text('Submit');
				}
		});
	});
	
	//INVITE
	$('#invite-submit-btn').click(function() {
	   	var users = [];
		var sub = $('#inviteTF').val();
		users.push(
			{
				name: 'Entered Manually',
				emails: {'manual': [ sub ]},
				phones: {'manual': [ sub ]}
			}
		); 
		alert('Your invitation is being sent');
		//$.mobile.changePage( '#profile', { reverse: 'true'} );
		$.mobile.back();
		$('#inviteTF').val('');
		console.log(users);
		$.post(api_prefix+'scripts/ajax.php', {url: http+'invite.php?nid='+nid, platform: platform, uuid: getCookie("UUID"), login_key: getCookie("login_key"), invite: JSON.stringify(users)},  function(data) {
			console.log(data);
		});
    });
	
	//NOTIFICATIONS
    $('body').on('click','#refresh-notifications',function(e) {
		refreshNotifications();
	});
	
	
    //OFFERS
    $('body').on('click','#refresh-offers',function(e) {
		refreshOffers();
	});
    $('body').on('click','li.offer',function(e) {
		setOfferDetail( $(this) );
		$.mobile.changePage( '#offer-detail' );
	});
	$('body').on('click','.offer-redeemable',function(e) {
		var oid = $(this).attr('data-oid');
		if( confirm("WARNING! You can only redeem this once!\nIssued: "+user.name+" (" + user.username+")") ) {
			redeemOffer( oid );
		}
	});
	
	//SOCIAL FEED
    $('body').on('click','#refresh-social-feed',function(e) {
		refreshSocialFeed();
	});
	
	//SOUNDBOARD
	$('#soundboard tr a').each(function(i) {
		soundboardSounds.push( {name: $( this ).attr('data-link')} );
	});
		if(soundboardSounds.length) {
			ion.sound({
			    sounds: soundboardSounds,
			    volume: 1.0,
			    path: "audio/soundboard/",
			    preload: true
			});
		}	
	
	$('body').on('click', '#soundboard a', function(e) {
		e.preventDefault();
		var audio = $(this);
		if(soundboardIsPlaying == false) {
			//start audio
			soundboardIsPlaying = audio.attr('data-link');
			ion.sound.play( audio.attr('data-link') );
			$('#soundboard img').attr('src','img/play-black@2x.png');
			audio.parent().parent().find('img').attr('src','img/stop-black@2x.png');
		} else {
			//stop old one first
			ion.sound.stop(soundboardIsPlaying);
			if(soundboardIsPlaying == audio.attr('data-link')) {
				//do nothing
				soundboardIsPlaying = false;
				$('#soundboard img').attr('src','img/play-black@2x.png');
			} else {
				//start the selected song
				soundboardIsPlaying = audio.attr('data-link');
				ion.sound.play( audio.attr('data-link') );
				$('#soundboard img').attr('src','img/play-black@2x.png');
				audio.parent().parent().find('img').attr('src','img/stop-black@2x.png');
			}
		}
	});
	
	//TICKETS
	$('body').on('pageshow','#student-tickets',function(){
		getMyTicketsAndChats();
	});
	$('body').on('pageshow','#student-tickets-buy',function(){
		getAvailableTickets();
	});
	
	$('#add-tickets').click(function() {
		if( $('#student-tickets-sell-eid').val() == null ) {
			alert("You have to select an event to list tickets for");
			return false;
		}
		$('#add-tickets').text('Adding....');
		console.log('eid: '+$('#student-tickets-sell-eid').val());
		$.post(api_prefix+'scripts/ajax.php', {url: http+'student_tickets_sell.php?nid='+nid, platform: platform, uuid: getCookie("UUID"), login_key: getCookie("login_key"), eid: $('#student-tickets-sell-eid').val(), event: 'placeholder', qty: $('#student-tickets-sell-qty').val(), price: $('#student-tickets-sell-price').val(), location: $('#student-tickets-sell-location').val(), description: $('#student-tickets-sell-description').val()}, function(data) {
			console.log(data);
			$('#add-tickets').text('Add Tickets');
			var json = jQuery.parseJSON(data);
			if(json.response == 'ok') {
				getMyTicketsAndChats();
				//$.mobile.changePage( '#student-tickets' );
				$.mobile.back();
				alert(json.msg);
				//close window
				
			} else {
				alert(json.errormsg);
				//don't close the window
			}
		});
	});
	
	$('#student-tickets-buy-eid').change(function() {
		getAvailableTickets();
	});
	
	$('body').on('click', '#single-sign-on-login', function(e) {
		if(platform == 'Android') {
			window.location = 'modal:'+ssoURL;
		} else {
			//alert("figuring this out for web still");
		}
	});
	
	$('body').on('click','#student-tickets-buy-tickets .chat-link a, #my-chats .chat-link a',function(e) {
		e.preventDefault();
		var chat = $(this);
		$('#student-tickets-chat .header img').attr('src',chat.attr('data-pic'));
		$('#student-tickets-chat .header span.username').text( chat.attr('data-username') );
		$('#student-tickets-chat .header span.details').html( chat.attr('data-dt') + ' - ' + chat.attr('data-eventname') + ' ' + chat.attr('data-eventdescription') + '<br />Qty: ' + chat.attr('data-qty') + '<br />$' + chat.attr('data-price') + '<br />' + chat.attr('data-location') + '<br />' + chat.attr('data-description') );
		getStudentTicketsChat( chat.attr('data-stid'), null, chat.attr('data-touid') );
		$.mobile.changePage( '#student-tickets-chat' );
	});
	
	$('body').on('click','#student-tickets-buy-message-send',function(e) {
		e.preventDefault();
		var msg = $('#student-tickets-buy-message').val();
		if(msg == '') {return;}
		var send = $(this);
		$.post(api_prefix+'scripts/ajax.php', {url: http+'student_tickets_chat_post.php?nid='+nid, platform: platform, uuid: getCookie("UUID"), login_key: getCookie("login_key"), stid: send.attr('data-stid'), to_uid: send.attr('data-touid'), message: msg}, function(data) {
			getStudentTicketsChat( send.attr('data-stid') , null, send.attr('data-touid') );
		});
	});
	
	$('body').on('click','#my-tickets .chats-link a',function(e) {
		e.preventDefault();
		var tix = $(this);
		$('#student-tickets-chats #delete-tickets').attr('data-stid',tix.attr('data-stid'));
		getTicketChats(tix.attr('data-stid'));
		$.mobile.changePage( '#student-tickets-chats' );
	});
	
	$('body').on('click','#ticket-chats .chat-link a',function(e) {
		e.preventDefault();
		var chat = $(this);
		$('#student-tickets-chat .header img').attr('src',chat.attr('data-pic'));
		$('#student-tickets-chat .header span.username').text( chat.attr('data-username') );
		$('#student-tickets-chat .header span.details').html( chat.attr('data-dt') + ' - ' + chat.attr('data-eventname') + ' ' + chat.attr('data-eventdescription') + '<br />Qty: ' + chat.attr('data-qty') + '<br />$' + chat.attr('data-price') + '<br />' + chat.attr('data-location') + '<br />' + chat.attr('data-description') );
		getStudentTicketsChat( chat.attr('data-stid'), chat.attr('data-threadid'), chat.attr('data-touid') );
		$.mobile.changePage( '#student-tickets-chat' );
	});
	
	$('body').on('click','#delete-tickets',function(e) {
		var stid = $(this).attr('data-stid');
		if(confirm('Are you sure you want to remove these tickets from the listings? There is no undo.')) {
			$.post(api_prefix+'scripts/ajax.php', {url: http+'student_tickets_remove.php?nid='+nid, platform: platform, uuid: getCookie("UUID"), login_key: getCookie("login_key"), stid: stid }, function(data) {
				getMyTicketsAndChats();
				$.mobile.changePage( '#student-tickets' );
			});
		}
	});

	//SCORES
	$('body').on('click','#refresh-scores',function(e) {
		refreshScores();
	});
	$('body').on('click','#add-score',function(e) {
		$('#scores li.scores-event-chooser').show();
	});
	$('body').on('change','#add-score-chooser',function(e) {
		scoreEIDSelected = $(this).val();
			console.log('scoreEID = '+scoreEIDSelected);
			
		$('#scores-add-us-img').attr('src', $('#scores-us-img-'+scoreEIDSelected).attr('src'));
		$('#scores-add-opp-img').attr('src', $('#scores-opp-img-'+scoreEIDSelected).attr('src'));
		$.mobile.changePage( '#scores-add' );
		$('#scores li.scores-event-chooser').hide();
		$('#add-score-chooser').val('');
	});
	
	$('#scores-add-btn').click(function() {
		$('#scores-add-btn').text('Submitting...');
		$.post(api_prefix+'scripts/ajax.php', {url: http+'add_scores.php?nid='+nid, platform: platform, uuid: getCookie("UUID"), login_key: getCookie("login_key"), eid: scoreEIDSelected, us_score: $('#scores-add-us-score').val(), opp_score: $('#scores-add-opp-score').val()}, function(data) {
			$('#update-profile-btn').text('Add Score');
			try {
				var json = jQuery.parseJSON(data);
			} catch(e) {
				alert('There was an error submitting your score. Please try again.');
				return;	
			}
			alert(json.message);
			if(json.response == 'ok') {
				//reset and return
				$('#scores-add-us-img').attr('src','');
				$('#scores-add-opp-img').attr('src','');
				$('#scores-add-us-score').val('');
				$('#scores-add-opp-score').val('');
				$.mobile.back();
			}
			
		});
	});
	
	
	
	
	//MISC
	$('body').on('click','.link-track', function() {
		var link = $(this);
		//console.log( 'track: ' + link.attr('data-lkey') + ' ' + link.attr('data-url') + ' ' + link.attr('data-title') + ' ' + link.attr('data-eid') );
		
		if( link.attr('data-url') != undefined ) {
			if( link.attr('data-url') != '' ) {
				//have the URL, just need to track it
				$.post(api_prefix+'scripts/ajax.php', {url: http+'link_out.php?nid='+nid, platform: platform, uuid: getCookie("UUID"), login_key: getCookie("login_key"), evurl: link.attr('data-url'), eid: link.attr('data-eid')}, function(data) {
					//console.log( data );
				});
				if(platform == 'Android') {
					window.location = 'modal:'+link.attr('data-url');
				} else {
					window.open( link.attr('data-url') , link.attr('data-title'), '');
				}
			}
		} else {
			if(platform == 'Android') {
				window.location = 'modal:'+http+'link_track.php?platform='+platform+'&uuid='+getCookie("UUID")+'&nid='+nid+'&lkey='+link.attr('data-lkey');
			} else {
				window.open( http+'link_track.php?platform='+platform+'&uuid='+getCookie("UUID")+'&nid='+nid+'&lkey='+link.attr('data-lkey') , 'Link Out', '');
			}
		}
	});
	$('body').on('click','.ad-interstitial .ad-header .close-btn', function() {
		$(this).parent().parent().hide();
	});
	
	$('span.appName').text(appname);
	$('span.networkName').text(networkName);
	$('span.app-version').text('version '+appVersion);
	if(platform == 'Android') {
		$('.android-only').show();
	}
	
});



function updateLoginKey( loginkey ) {
	login_key = loginkey;
	setCookie('login_key',loginkey,365);
	console.log("main: setting loginkey");
}

/* check login */

function checkLogin() {
	//console.log("showed loading, loggedIN is "+loggedIn);
	//console.log("loginkey is: "+login_key);
	if(loggedIn == true) {
		$.mobile.changePage( '#homescreen' );
		if( getVar('platform')=="Android" ) {
			window.location = "updateapid:/"+nid+"/"+user.uid;
		}
	} else if(loggedIn == false) {
		console.log('firstLoad: '+firstLoad);
		if(firstLoad == true) {
			firstLoad = false;
			$.mobile.changePage( '#tutorial' );
		} else {
			if(ssoURL != false && ssoLogout != false && SPaction != false) {
				$.mobile.changePage('#single-sign-on-login');
			} else {
				$.mobile.changePage( '#login' );
			}
		}
	} else {
        console.log("main: here is the cookies: " + JSON.stringify(localCookieStorage));
		console.log("main: here are the vars we passed: " + `platform: ${platform}, uuid: ${getCookie("UUID")}, login_key: ${getCookie("login_key")}, version: ${platformVersion}, app_version: ${appVersion}, architecture: ${ostype}, model: ${phoneModel}, osname: ${platform}, is_jailbroken: ${isJailbroken}`);
		$.post(api_prefix+'scripts/ajax.php', {url: http+'check_login.php?nid='+nid, platform: platform, uuid: getCookie("UUID"), login_key: getCookie("login_key"), version: platformVersion, app_version: appVersion, architecture: ostype, model: phoneModel, osname: platform, ostype: "32bit", is_jailbroken: isJailbroken}, function(data) {
			console.log("main: here is the data returned: " + data);
			var json = jQuery.parseJSON(data);
			console.log('check login response: '+json.response);
			console.log(data);
			if(json.response=='ok') {
				user = json.user;
				updateLoginKey( json.loginkey );
				$.event.trigger({type: "moduleRefresh"});
				if( getVar('platform')=="Android" ) {
					window.location = "updateapid:/"+nid+"/"+user.uid;
				}
                $.mobile.changePage( '#homescreen' );
				loggedIn = true;
				$('#fb-login-btn p').text('Login with Facebook');
				$('#fb-register-btn p').text('Register with Facebook');
				if(platform == "Android") {
					androidPullCookies();
				}
                $.event.trigger({type: "moduleRefresh"});
			} else {
				console.log('sfu firstLoad: '+firstLoad);
				if(firstLoad == true || firstLoad == null) {
					firstLoad = false;
					$.mobile.changePage( '#tutorial' );
				} else {
					if(ssoURL != false && ssoLogout != false && SPaction != false) {
			            $.mobile.changePage('#single-sign-on-login');
					} else {
						$.mobile.changePage( '#login' );
					}
					loggedIn = false;
				}
			}
		}).fail(function(data) {
			console.log(JSON.stringify(data));
		});
	}
}

//events
function refreshEvents() {
	$('#events .ui-content').html('<div class="loading"><img src="img/ajax-loader.gif" /><p>Loading Events...</p></div>');
	$.post(api_prefix+'scripts/events.php', {url: http+'get_events.php?nid='+nid+'&headers=month', platform: platform, uuid: getCookie("UUID"), login_key: getCookie("login_key")},  function(data) {
		$('#events .ui-content').html(data).find('ul').listview().listview('refresh');
		$('#events .nav-bar-color h1').html( $('#events .ui-content #event-tags').html() );
		var showOnlyTag = $('#show-only-tag').text();
			if(showOnlyTag) {
				$('.upcoming-event').hide();
				$('.upcoming-event.tag-'+showOnlyTag).show();
			}
		if( $('#has-calendar-icon').attr('data-show') == '1' ) {
			$('#calendar-events').show();
			$('.mini_calendar table td[data-tag="tag-'+$('#events .nav-bar-color h1 span.active').attr('data-event')+'"]').addClass('visible');
		} else {
			$('#calendar-events').hide();
		}
		$('#events .ui-content img.tint-image').each(function() {
			var label_color = $(this).attr('data-label-color');
			if( label_color != '' ) {
				var rgb = hexToRgb( label_color );
				$(this).load(function() {
					var tinted_img = tintImage(null, $(this).attr('src'), $(this).attr('class'), rgb.r, rgb.g, rgb.b);
					$(this).replaceWith( tinted_img );
				});
			}
		});
	});
}

function getEventInfo(eventid) {
	eventid = eventid.replace('event-','');
	$('#event-info .ui-content').html('<div class="loading"><p><img src="img/ajax-loader.gif" /><p>Loading Event Info...</p></div>');
	$.post(api_prefix+'scripts/event-info.php', {url: http+'event_info.php?nid='+nid, eventid: eventid, platform: platform, uuid: getCookie("UUID"), login_key: getCookie("login_key")},  function(data) {
		$('#event-info .ui-content').html(data);
		if(platform != 'Android') {
			$('.android-only').hide();
		}
		$('#event-info .ui-content img.tint-image').each(function() {
			var label_color = $(this).attr('data-label-color');
			if( label_color != '' ) {
				var rgb = hexToRgb( label_color );
				$(this).load(function() {
					var tinted_img = tintImage(null, $(this).attr('src'), $(this).attr('class'), rgb.r, rgb.g, rgb.b);
					$(this).replaceWith( tinted_img );
				});
			}
		});
	});
}

function getEventInfoUsers( eventid ) {
	$('#event-info-users .ui-content').html('<div class="loading"><p><img src="img/ajax-loader.gif" /><p>Loading Event Users...</p></div>');
	$.post(api_prefix+'scripts/event-info-users.php', {url: http+'get_event_checked_in_users.php?nid='+nid, eid: eventid, platform: platform, uuid: getCookie("UUID"), login_key: getCookie("login_key")},  function(data) {
		$('#event-info-users .ui-content').html(data).find('ul').listview().listview('refresh');
	});
}

//rewards
function refreshRewards() {
	$('#awards .ui-content').html('<div class="loading"><img src="img/ajax-loader.gif" /><p>Loading Rewards...</p></div>');
	$.post(api_prefix+'scripts/rewards.php', {url: http+'get_awards_and_prizes.php?nid='+nid, platform: platform, uuid: getCookie("UUID"), login_key: getCookie("login_key"), tkthttp: tkthttp},  function(data) {
		$('#awards .ui-content').html(data).find('ul').listview().listview('refresh');
		twemoji.parse(document.getElementById('awards'), {size: 36});
		var a = $('#rewards').html();
		var p = $('#prize-store').html();
		if(a === undefined) {a = '';}
		if(p === undefined) {p = '';}
		if(a.length > 0 && p.length > 0) {
			//they're both there
			$('#awards .nav-bar-color h1').html( '<div id="awards-tabs"><span class="awards-tab active" data-show="rewards">Rewards</span><span class="awards-tab" data-show="prize-store">Store</span></div>' );
			$('#rewards').show();
			$('#prize-store').hide();
		} else if(a.length > 0) {
			//just awards
			$('#awards .nav-bar-color h1').text('Rewards');
			$('#rewards').show();
			$('#awards #prize-store').hide();
		} else if(p.length > 0) {
			//it's only prizestore
			$('#awards .nav-bar-color h1').text('Prize Store');
			$('#prize-store').show();
			$('#rewards').hide();
		}
	});
}

function setAwardDetail( _o ) {
	//$('#award-detail .bgimg').attr('style', 'background-image:url("'+_o.attr('data-award-icon')+'");').show();
	$('#award-detail .bgimg img').attr('src', _o.attr('data-award-icon')).show();
	$('#award-detail .ui-content h3').text( _o.attr('data-name').replace(/\\(.)/mg, "$1") );
	if(_o.attr('data-used') == 1) {
		$('#award-detail .ui-content span.date').text('Used '+_o.attr('data-used-tx'));
	} else {
		$('#award-detail .ui-content span.date').text(_o.attr('data-expires'));
	}
	if( _o.attr('data-barcode')!='' ) {
		$('#award-detail img.qr-icon').hide();
		$('#award-detail img.barcode').attr('src', _o.attr('data-barcode')).show();
	} else {
		$('#award-detail img.qr-icon').show();
		$('#award-detail img.barcode').attr('src', _o.attr('data-qr')).hide();
	}
	if(_o.attr('data-redeemable') == 1) {
		if(_o.attr('data-used') == 1) {
			$('#award-detail img.qr-icon').hide();
			$('#redeem-award-btn').attr('data-aid', '0').attr('data-cid', '0').removeClass('award-redeemable').addClass('award-redeemed').text('Used!').show();
			$('#award-detail img.barcode').hide();
		} else {
			$('#redeem-award-btn').attr('data-aid', _o.attr('data-aid')).attr('data-cid', _o.attr('data-cid')).addClass('award-redeemable').removeClass('award-redeemed').text('Use Award').show();
		}
	} else {
		$('#award-detail img.barcode').hide();
		$('#redeem-award-btn').hide();
	}
	$('#award-detail .ui-content p.description').html(  _o.attr('data-receive-text').replace(/\\(.)/mg, "$1") );
	$('#award-detail .ui-content p.fine-print').html(_o.attr('data-fine-print').replace(/\\(.)/mg, "$1") );
}

function redeemAward( cid ) {
	var latitude = 0;
	var longitude = 0;
	var accuracy = 0;
	$.post(api_prefix+'scripts/ajax.php', {url: http+'redeem_prize.php?nid='+nid, platform: platform, uuid: getCookie("UUID"), login_key: getCookie("login_key"), latitude: latitude, longitude: longitude, accuracy: accuracy, cid: cid}, function(data) {
	
		var resp = $.parseJSON( data );
		if(resp.success) {
			alert("Success! Your award was redeemed!");
			$('#award-detail img.barcode').hide();

				var date = new Date();
				var minute = date.getMinutes();
				var am = 'am';
				var hour = date.getHours();
					if(hour > 12) {
						hour = hour - 12;
						am = 'pm';
					}
				var day = date.getDate();
				var month = date.getMonth();
					month = month + 1;
				var year = date.getFullYear();
				var datetime = month + '/' + day + '/' + year + ' at ' + hour + ':' + (minute<10?'0':'') + minute + am;
			
			$('#award-detail .ui-content span.date').text('Redeemed: '+datetime);
			$('#award-'+ $('#redeem-award-btn').attr('data-aid') ).attr('data-used', '1').attr('data-used-tx', datetime);
			$('#redeem-award-btn').removeClass('award-redeemable').addClass('award-redeemed').text('Redeemed')

		} else {
			alert('NOT REDEEMED! '+resp.error);
		}
	});
}

//prize store
function refreshPrizeStore() {
	$('#prize-store .ui-content').html('<div class="loading"><img src="img/ajax-loader.gif" /><p>Loading Prize Store...</p></div>');
	$.post(api_prefix+'scripts/prize-store.php', {url: http+'get_prize_store.php?nid='+nid, platform: platform, uuid: getCookie("UUID"), login_key: getCookie("login_key")},  function(data) {
		$('#prize-store .ui-content').html(data).find('ul').listview().listview('refresh');
		twemoji.parse(document.getElementById('prize-store'), {size: 36});
	});
}

function setPrizeStoreDetail( _o ) {
	//$('#prize-store-detail .bgimg').attr('style', 'background-image:url("'+_o.attr('data-award-icon')+'");').show();
	$('#prize-store-detail .bgimg img').attr('src', _o.attr('data-img')).show();
	$('#prize-store-detail .ui-content h3').text( _o.attr('data-title').replace(/\\(.)/mg, "$1") );
	$('#prize-store-detail .ui-content span.date').text( _o.attr('data-expires').replace(/\\(.)/mg, "$1") );
	$('#prize-store-detail .ui-content #buy-prize-btn').attr('data-pid', _o.attr('data-pid')).attr('data-points-needed', _o.attr('data-points-needed')).text( _o.attr('data-points-needed-formatted').replace(/\\(.)/mg, "$1") );
	$('#prize-store-detail .ui-content span.details').html( _o.attr('data-details') );
	$('#prize-store-detail .ui-content div.loading-text').show().html('<span>Loading purchased items...</span>');
	$('#prize-store-detail .ui-content div#redeemable-items').removeClass('is-redeemable').html('');
	if( _o.attr('data-is-redeemable') == '1' ) {
		$('#prize-store-detail .ui-content div#redeemable-items').addClass('is-redeemable');
	}
	
	if( _o.attr('data-award-img-inside-unlocked') != '' ) {
		$('#prize-store-detail img.img-inside-unlocked').attr('src', _o.attr('data-award-img-inside-unlocked'));
	}
	if( _o.attr('data-award-img-inside') != '' ) {
		$('#prize-store-detail img.img-inside').attr('src', _o.attr('data-award-img-inside'));
	}
	
	$('#prize-store-detail img.img-inside').hide();
	$('#prize-store-detail img.img-inside-unlocked').hide();
	
	if( parseInt( _o.attr('data-prize-count') ) == 0 ) {
		if( _o.attr('data-award-img-inside') != '' ) {
			$('#prize-store-detail img.img-inside').show();
		} else {
			$('#prize-store-detail img.img-inside').hide();
		}
	} else {
		if( _o.attr('data-award-img-inside-unlocked') != '' ) {
			$('#prize-store-detail img.img-inside-unlocked').show();
		} else if( _o.attr('data-award-img-inside') != '' ) {
			$('#prize-store-detail img.img-inside').show();
		} else {
			$('#prize-store-detail img.img-inside').hide();
		}
	}
	if( _o.attr('data-options') != 'null' ) {
		opts = JSON.parse( _o.attr('data-options') );
		var a = '<select id="prize-store-option">';
		for(var o in opts) {
			console.log( opts[o] );
			a+='<option value="'+opts[o].id+'">'+opts[o].option_name+'  - '+opts[o].available_int+' available</option>';
		}
		a+='</select>';
		$('#prize-store-detail #selector').html(a).trigger("create");
	} else {
		$('#prize-store-detail #selector').html('');
	}
	$('#prize-store-detail .ui-content p.description').html(  _o.attr('data-description-inside').replace(/\\(.)/mg, "$1") );
}

function getPrizeStoreRedeemableItems( pid ) {
	//return;
	$.post(api_prefix+'scripts/prize-store-redeemable-items.php', {url: http+'redeem_prize_store_purchased_items.php?nid='+nid, platform: platform, uuid: getCookie("UUID"), login_key: getCookie("login_key"), pid: pid},  function(data) {
		console.log('data = '+data);
		$('#prize-store-detail .ui-content div.loading-text').hide();
		$('#prize-store-detail .ui-content div#redeemable-items').html(data).find('ul').listview().listview('refresh');
	});
}

function buyPrize(pid, _obj) {
	isBuyingPrize = true;
	var original_text = _obj.text();
	_obj.text("Purchasing...");
	var prize_option = null;
	if($('#prize-store-option').val() !== undefined) {
		prize_option = $('#prize-store-option').val();
	}
	$.post(api_prefix+'scripts/ajax.php', {url: http+'redeem_prize_store.php?nid='+nid, platform: platform, uuid: getCookie("UUID"), login_key: getCookie("login_key"), pid: pid, prize_option: prize_option},  function(data) {
		var json = JSON.parse( data );
		if(json.response == 'ok') {
			getPrizeStoreRedeemableItems( pid );
			_obj.text( original_text );
			//update available
			$('#prize-store-detail .ui-content span.details span').text( json.available );
			//update coins
			$('#prize-store .ui-content .user-info span.user-points').text( json.coins );
			//update original prize card
			$('#prize-id-'+pid).attr('data-prize-count', 
				parseInt( $('#prize-id-'+pid).attr('data-prize-count') ) + 1
			);
			//update inside image
			if( $('#prize-store-detail img.img-inside-unlocked').attr('src') != '' ) {
				$('#prize-store-detail img.img-inside').hide();
				$('#prize-store-detail img.img-inside-unlocked').show();
			}
			isBuyingPrize = false;
			alert( json.title + "\n" + json.msg );
		} else {
			_obj.text( original_text );
			isBuyingPrize = false;
			alert('Something went wrong! Please try again.');
		}
	});
}

function prizeStoreRedeemItem(pid, prid, _obj) {
	var original_text = _obj.html();
	_obj.html('<img src="img/ajax-loader.gif" />');
	$.post(api_prefix+'scripts/ajax.php', {url: http+'redeem_prize_store_redeem_item.php?nid='+nid, platform: platform, uuid: getCookie("UUID"), login_key: getCookie("login_key"), prid: prid},  function(data) {
		var json = JSON.parse( data );
		if(json.response == 'ok') {
			alert( json.title + "\n" + json.msg );
			getPrizeStoreRedeemableItems( pid );
		} else {
			_obj.removeClass('redeeming').html( original_text );
			alert('Something went wrong! Please try again.');
		}
	});
}

function refreshHistory() {
	$('#history .ui-content').html('<div class="loading"><img src="img/ajax-loader.gif" /><p>Loading History...</p></div>');
	$.post(api_prefix+'scripts/history.php', {url: http+'get_point_history.php?nid='+nid, platform: platform, uuid: getCookie("UUID"), login_key: getCookie("login_key")},  function(data) {
		$('#history .ui-content').html(data).find('ul').listview().listview('refresh');
	});
}

//offers
function refreshOffers() {
	$('#offers .ui-content').html('<div class="loading"><img src="img/ajax-loader.gif" /><p>Loading Offers...</p></div>');
	$.post(api_prefix+'scripts/offers.php', {url: http+'get_offers.php?nid='+nid, platform: platform, uuid: getCookie("UUID"), login_key: getCookie("login_key")},  function(data) {
		$('#offers .ui-content').html(data).find('ul').listview().listview('refresh');
	});
}

function setOfferDetail( _o ) {
	if( _o.attr('data-bgimg')!='' ) {
		$('#offer-detail .bgimg').attr('style', 'background-image:url("'+_o.attr('data-bgimg')+'");').show();
	} else {
		$('#offer-detail .bgimg').hide();
	}
	$('#offer-detail .ui-content img.icon').attr('src', _o.attr('data-offer-icon'));
	$('#offer-detail .ui-content h3').text( _o.attr('data-name').replace(/\\(.)/mg, "$1") );
	$('#offer-detail .ui-content span.date').text('Expires '+_o.attr('data-expires')); //Expires on
	if( _o.attr('data-barcode')!='' ) {
		$('#offer-detail img.barcode').attr('src', _o.attr('data-barcode')).show();
	} else {
		$('#offer-detail img.barcode').hide();
	}
	console.log( _o.attr('data-used-tx') );
	if(_o.attr('data-used-tx') == '') {
		$('#redeem-offer-btn').attr('data-oid', _o.attr('data-oid')).addClass('offer-redeemable').removeClass('offer-redeemed').text('Redeem Offer').show();
	} else {
		$('#redeem-offer-btn').attr('data-oid', '0').removeClass('offer-redeemable').addClass('offer-redeemed').text('Redeemed').show();
		if(_o.attr('data-used-tx').indexOf('-') != -1) {
			var dt = _o.attr('data-used-tx').split(' ');
				dt = dt[0].split('-');
			$('#offer-detail .ui-content span.date').text('Redeemed on '+dt[1]+'/'+dt[2]+'/'+dt[0]);
		} else {
			$('#offer-detail .ui-content span.date').text('Redeemed on '+_o.attr('data-used-tx'));
		}
		
	}

	$('#offer-detail .ui-content p.description').html(  _o.attr('data-description').replace(/\\(.)/mg, "$1") );
	$('#offer-detail .ui-content p.fine-print').html(_o.attr('data-fine-print').replace(/\\(.)/mg, "$1") );
}

function redeemOffer(oid) {
	var latitude = 0;
	var longitude = 0;
	var accuracy = 0;
	$.post(api_prefix+'scripts/ajax.php', {url: http+'redeem_offer.php?nid='+nid, platform: platform, uuid: getCookie("UUID"), login_key: getCookie("login_key"), latitude: latitude, longitude: longitude, accuracy: accuracy, oid: oid}, function(data) {
		console.log(data);
		try {
			var resp = $.parseJSON( data );
		} catch(e) {
			console.log(e);
			alert('Sorry, there was a problem contacting the network. Try again in a moment.');
			return false;
		}
		if(resp.response == 'ok') {
			alert("Success! Your offer was redeemed!");
			//$('#award-detail img.barcode').hide();

				var date = new Date();
				var minute = date.getMinutes();
				var am = 'am';
				var hour = date.getHours();
					if(hour > 12) {
						hour = hour - 12;
						am = 'pm';
					}
				var day = date.getDate();
				var month = date.getMonth();
					month = month + 1;
				var year = date.getFullYear();
				var datetime = month + '/' + day + '/' + year + ' at ' + hour + ':' + (minute<10?'0':'') + minute + am;
			
			$('#offer-detail .ui-content span.date').text('Redeemed: '+datetime);
			$('#offer-'+ oid ).attr('data-used-tx', datetime);
			$('#redeem-offer-btn').removeClass('offer-redeemable').addClass('offer-redeemed').text('Redeemed')

		} else {
			alert('NOT REDEEMED! '+resp.error);
		}
	});
}

//social feed
function refreshSocialFeed() {
	$('#social-feed .ui-content').html('<div class="loading"><img src="img/ajax-loader.gif" /><p>Loading Social Feed...</p></div>');
	$.post(api_prefix+'scripts/social-feed.php', {url: http+'social_feed.php?nid='+nid, platform: platform, uuid: getCookie("UUID"), login_key: getCookie("login_key")},  function(data) {
		$('#social-feed .ui-content').html(data).find('ul').listview().listview('refresh');
		twemoji.parse(document.getElementById('social-feed'), {size: 16});
	});
}

//leaderboard
function refreshLeaderboard() {
	$('#leaderboard .ui-content').html('<div class="loading"><img src="img/ajax-loader.gif" /><p>Loading Leaderboard...</p></div>');
	$.post(api_prefix+'scripts/leaderboard.php', {url: http+'get_leaders.php?nid='+nid, platform: platform, uuid: getCookie("UUID"), login_key: getCookie("login_key")},  function(data) {
		$('#leaderboard #tab-form').html(data).trigger('create');
		if( $('#tab-form .ui-radio').size() == 1 ) {
			$('#tab-form').hide();
		} else {
			$('#tab-form .ui-radio').first().find('label').addClass('ui-btn-active');
			$('#tab-form').show();
		}
		$('#leaderboard .ui-content').html( decodeURIComponent( $('#tab-form .ui-radio').first().find('input').attr('data-html') ) ).trigger('create');
	});
}

//fancam
function refreshFanCam() {
	$('#fancam #fancam-pics').html('<div class="loading"><img src="img/ajax-loader.gif" /><p>Loading FanCam...</p></div>');
	$.post(api_prefix+'scripts/fancam.php', {url: http+'get_media.php?nid='+nid, platform: platform, uuid: getCookie("UUID"), login_key: getCookie("login_key")},  function(data) {
		$('#fancam #fancam-pics').html(data);
		$('#fancam-single .ui-content').html( $('#fancam #fancam-pics .swipe-slick').clone() );
			$('#fancam-single .ui-content .swipe-slick').slick({
				arrows: false
			});
	});
}

function synthesizeFanCamClick() {
    console.log('synthesizing click');
    $('#upload-media').click();
}


function uploadFanCamPicProgress(e){
    if(e.lengthComputable){
        $('progress#progress-upload-media').attr({value:e.loaded,max:e.total});
    }
}

//scores
function refreshScores() {
	$('#scores .ui-content').html('<div class="loading"><img src="img/ajax-loader.gif" /><p>Loading Scores...</p></div>');
	$.post(api_prefix+'scripts/scores.php', {url: http+'get_scores.php?nid='+nid, platform: platform, uuid: getCookie("UUID"), login_key: getCookie("login_key")},  function(data) {
		$('#scores .ui-content').html(data).find('ul').listview().listview('refresh');
	});
}

//profile
function refreshProfile() {
	$('#profile .ui-content').html('<div class="loading"><img src="img/ajax-loader.gif" /><p>Loading Account...</p></div>');
	$.post(api_prefix+'scripts/profile.php', {url: http+'get_profile.php?nid='+nid, platform: platform, uuid: getCookie("UUID"), login_key: getCookie("login_key")},  function(data) {
		$('#profile .ui-content').html(data);
		$('#profile .admin-link').attr('data-url', 'https://app.superfanu.com/auto-login?nid='+nid+'&uuid='+getCookie("UUID")+'&hash='+login_key);
		twemoji.parse(document.getElementById('profile'), {size: 36});
	});
	
	$('#profile-edit .ui-content').html('<div class="loading"><img src="img/ajax-loader.gif" /><p>Loading Profile...</p></div>');
	$.post(api_prefix+'scripts/profile-edit.php', {url: http+'get_profile.php?nid='+nid, platform: platform, uuid: getCookie("UUID"), login_key: getCookie("login_key")},  function(data) {
		$('#profile-edit .ui-content').html(data).trigger('create');
	});
}

function uploadProfilePicProgress(e){
    if(e.lengthComputable){
        $('progress#progress-upload-user-pic').attr({value:e.loaded,max:e.total});
    }
}

function facebookStatus(status) {
	if (status === 'connected') {
		$('img.facebook-login-logout-btn').attr('src', 'img/fb-settings-logout-btn.png');
	} else {
		$('img.facebook-login-logout-btn').attr('src', 'img/fb-settings-login-btn.png');
	}
}

//notifications
function refreshNotifications() {
	$('#notifications .ui-content').html('<div class="loading"><img src="img/ajax-loader.gif" /><p>Loading Notifications...</p></div>');
	$.post(api_prefix+'scripts/notifications.php', {url: http+'get_notifications.php?nid='+nid, platform: platform, uuid: getCookie("UUID"), login_key: getCookie("login_key")},  function(data) {
		$('#notifications .ui-content').html(data).find('ul').listview().listview('refresh');
	});
	checkNotifications(nid);
}

function checkNotifications() {
	$.post(api_prefix+'scripts/check-notifications.php', {url: http+'get_notifications.php?nid='+nid, platform: platform, uuid: getCookie("UUID"), login_key: getCookie("login_key")},  function(data) {
		var not = jQuery.parseJSON( data );
		if(not.count > 0) {
			$('#notification-count').html( not.count );
			$('#notification-view p').html( 'New Notification' );
			$('#notification-view').show();
		}
	});
}

//fanpoll
function refreshFanPoll() {
	var currentTime = new Date();
	var n = currentTime.getTime();
	$('#fanpoll .ui-content').html('<div class="loading"><img src="img/ajax-loader.gif" /><p>Loading Poll...</p></div>');
	$.post(api_prefix+'scripts/fanpoll.php', {url: http+'get_poll.php?nid='+nid+'&n='+n, platform: platform, uuid: getCookie("UUID"), login_key: getCookie("login_key")},  function(data) {
		$('#fanpoll .ui-content').html(data).find('ul').listview().listview('refresh');
	});
}

//news
function refreshNews() {
	if(rssLink!='') {
		$('#news .ui-content').html('<div class="loading"><img src="img/ajax-loader.gif" /><p>Loading News...</p></div>');
		$.post(api_prefix+'scripts/news-pics.php', {url: rssLink, platform: platform, uuid: getCookie("UUID"), login_key: getCookie("login_key")},  function(data) {
			$('#news .ui-content').html(data).find('ul').listview().listview('refresh');
		});
	}
}

//rosters
function refreshRosters() {
	$('#rosters .ui-content').html('<div class="loading"><img src="img/ajax-loader.gif" /><p>Loading Rosters...</p></div>');
	$.post(api_prefix+'scripts/rosters.php', {url: http+'rosters.php?nid='+nid, platform: platform, uuid: getCookie("UUID"), login_key: getCookie("login_key")},  function(data) {
		$('#rosters .ui-content').html(data).trigger('create');
	});
}

//venue list
function refreshVenueList() {
	$('#venue-list .ui-content').html('<div class="loading"><img src="img/ajax-loader.gif" /><p>Loading Venues...</p></div>');
	$.post('scripts/venue-list.php', {url: http+'get_venues.php?nid='+nid, platform: platform, uuid: uuid, login_key: login_key},  function(data) {
		$('#venue-list .ui-content').html(data).trigger('create');
	});
}

//dynamic text
function getDynamicText() {
	$('#dynamic-text .ui-content').html('<div class="loading"><img src="img/ajax-loader.gif" /><p>Loading...</p></div>');
	$.post(api_prefix+'scripts/ajax.php', {url: http+'get_dynamic_text.php?nid='+nid, platform: platform, uuid: getCookie("UUID"), login_key: getCookie("login_key")},  function(data) {
		//console.log(data);
		try {
			var json = JSON.parse( data );
		} catch(e) {
			
		}
		if(json.text !== null) {
			$('#dynamic-text .ui-content').html( json.text[1].text );
		}
		
		//$('#rosters .ui-content').html(data).trigger('create');
	});
}

function getDynamicTextTabs(option) {
	$('#dynamic-text-tabs .ui-content #dtt-content').html('<div class="loading"><img src="img/ajax-loader.gif" /><p>Loading...</p></div>');
	$.post(api_prefix+'scripts/ajax.php', {url: http+'get_dynamic_text_tabs.php?nid='+nid, platform: platform, uuid: getCookie("UUID"), login_key: getCookie("login_key"), option: option},  function(data) {
		try {
			var json = JSON.parse( data );
		} catch(e) {
			
		}
		var btn_count = 0;
		var btn_str = '<form><fieldset data-role="controlgroup" data-type="horizontal">';
		if(json.text) {		
			for (var t in json.text) {
				if (json.text.hasOwnProperty(t)) {
					if(btn_count == 0) {
						btn_str+= '<input type="radio" name="dtt-radio-choice" id="dtt-radio-choice-'+btn_count+'" value="on" checked="checked" data-html="'+encodeURIComponent(json.text[t].text.replace(/<a href="(.*?)">(.*?)<\/a>/g,'<a href="#" class="link-track" data-url="\$1" data-title="" data-eid="0">\$2</a>'))+'"><label for="dtt-radio-choice-'+btn_count+'">'+json.text[t].button_name+'</label>';
						//console.log(json.text[t].text.replace(/a/g,'<a href="#" class="link-track" data-url="\$1" data-title="" data-eid="0">\$2</a>'));
						$('#dtt-content').html(json.text[t].text.replace(/<a href="(.*?)">(.*?)<\/a>/g,'<a href="#" class="link-track" data-url="\$1" data-title="" data-eid="0">\$2</a>'));
					} else {
						btn_str+= '<input type="radio" name="dtt-radio-choice" id="dtt-radio-choice-'+btn_count+'" data-html="'+encodeURIComponent(json.text[t].text.replace(/<a href="(.*?)">(.*?)<\/a>/g,'<a href="#" class="link-track" data-url="\$1" data-title="" data-eid="0">\$2</a>'))+'"><label for="dtt-radio-choice-'+btn_count+'">'+json.text[t].button_name+'</label>';
					}
					btn_count = btn_count + 1;
				}
			}
			$('#dynamic-text-tabs #dtt-form').html(btn_str+'</fieldset></form>').trigger('create');
		}
	});
}


function getDynamicTextTabsOptions() {
	$('#dynamic-text-tabs-options .ui-content #dtt-content').html('<div class="loading"><img src="img/ajax-loader.gif" /><p>Loading...</p></div>');
	$.post(api_prefix+'scripts/ajax.php', {url: http+'get_dynamic_text_tabs_options.php?nid='+nid, platform: platform, uuid: getCookie("UUID"), login_key: getCookie("login_key")},  function(data) {
		console.log(data);
		try {
			var json = JSON.parse( data );
		} catch(e) {
			
		}
		var str = '<table class="styled" border="0">';
		if(json) {
			for(_i=0;_i<json.length;_i++) {
				str+='<tr class="link"><td><h4><a href="#" onClick="getDynamicTextTabs('+json[_i].option+');$.mobile.changePage(\'#dynamic-text-tabs\');">'+json[_i].name+'</a></h4></td></tr>';
			}
		}
		str+='</table>';
		//<table class="styled" border="0">
		//echo '<tr class="link"><td><h4><a href="#" class="link-track" data-url="'.$r->url.'" data-title="'.$r->name.'" data-eid="0">'.$r->name.'</a></h4></td></tr>';
		//</table>
		$('#dynamic-text-tabs-options .ui-content').html(str).trigger('create');
	});
}


//beacons
function setupCanUseBeaconToggle() {
	setTimeout(function() {
		if(platform == 'Android') {
			window.location = 'getbeacontog://';
		} else {
			//console.log('you are not android!');
		}
	}, 8000);
}

function setCanUseBeaconToggle(bool, msg) {
	if(bool == 'true') {
		$('#canUseBeaconToggle').prop('checked', true).checkboxradio('refresh');
		canUseBeacons = true;
	} else {
		$('#canUseBeaconToggle').prop('checked', false).checkboxradio('refresh');
		canUseBeacons = false;
	}
	if(msg && msg!='') {
		alert(msg);
	}
}

function refreshBeacons() {
	if(platform=='Android') {
		$.post(api_prefix+'scripts/ajax.php', {url: http+'beacons.php?nid='+nid, platform: platform, uuid: getCookie("UUID"), login_key: getCookie("login_key")},  function(data) {
			try {
				var json = jQuery.parseJSON( data );
			} catch(e) {
				console.log(e);
				return false;
			}
			beaconList = json;
			for (var beacon in json) {
				if( json.hasOwnProperty( beacon ) ) {
					//console.log(json[beacon]);
					//window.location = 'addbeacon:/'+json[beacon].buuid+'/'+json[beacon].bmajor+'/'+json[beacon].bminor+'/'+json[beacon].bname+'/'+encodeURIComponent(json[beacon].bmessage)+'/'+encodeURIComponent(json[beacon].bmessage_full)+'/'+encodeURIComponent(json[beacon].bmessage_full_img)+'/'+json[beacon].message_start+'/'+json[beacon].message_end+'/'+json[beacon].message_repeat_delay+'/'+json[beacon].message_show_total_times+'/';
					window.location = 'addbeacon:/'+json[beacon].buuid+'/'+json[beacon].bmajor+'/'+json[beacon].bminor+'/'+json[beacon].bname+'/'+encodeURIComponent(json[beacon].bmessage)+'/'+json[beacon].message_start+'/'+json[beacon].message_end+'/'+json[beacon].message_repeat_delay+'/'+json[beacon].message_show_total_times+'/';
				} 
			}
			//window.location = 'beacons://';
			setCookie("beacons",JSON.stringify(json),365);
		});
	}
}

function showIndividualBeaconNotfication( bid ) {
	if( bid != false) {
		try {
			var beacons = jQuery.parseJSON( getCookie("beacons") );
		} catch(e) {
			console.log(e);
			return false;
		}
		for (var beacon in beacons) {
			if( beacons.hasOwnProperty( beacon ) ) {
				if( bid == beacons[beacon].bname ) {
					if( beacons[beacon].bmessage_full_img != '' ) {
						$('#individual-beacon-notification .ui-content img').attr('src', beacons[beacon].bmessage_full_img ).show();
					} else {
						$('#individual-beacon-notification .ui-content img').hide();
					}
					if( beacons[beacon].bmessage_full != '' ) {
						$('#individual-beacon-notification .ui-content p').html( linkify( beacons[beacon].bmessage_full ) );
					} else {
						$('#individual-beacon-notification .ui-content p').html( linkify( beacons[beacon].bmessage ) );
					}
					$.mobile.changePage( '#individual-beacon-notification' );
				}
			} 
		}
	}
}

//push

function showIndividualPushNotfication( mid ) {
	if(mid == undefined) {
		mid = 'latest';
	}
	$.post(api_prefix+'scripts/ajax.php', {url: http+'get_notification.php?nid='+nid, platform: platform, uuid: getCookie("UUID"), mid: mid}, function(data) {
		try {
			var json = jQuery.parseJSON( data );
		} catch(e) {
			alert("We weren't able to get your notification. Please go to your account and tap on Notifications.");
			return false;
		}
		for (var not in json) {
			if( json.hasOwnProperty( not ) ) {
				//mark that we read this
				$.post(api_prefix+'scripts/ajax.php', {url: http+'read_notification.php?nid='+nid, platform: platform, uuid: getCookie("UUID"), id: json[not].id}, function(data) {
					//nothing
				});
				if(json[not].full_message_image != '') {
					if( json[not].full_message_image_alignment == 'top' ) {
						$('#individual-push-notification img.img-top').attr('src', json[not].full_message_image).show();
						$('#individual-push-notification img.img-bottom').hide();
					} else if( json[not].full_message_image_alignment == 'bottom' ) {
						$('#individual-push-notification img.img-top').hide();
						$('#individual-push-notification img.img-bottom').attr('src', json[not].full_message_image).show();
					} 
				} else {
					$('#individual-push-notification img.img-top').hide();
					$('#individual-push-notification img.img-bottom').hide();
				}
				if(json[not].full_message != '') {
					$('#individual-push-notification p').html( linkify( json[not].full_message ) );
				} else {
					$('#individual-push-notification p').html( linkify( json[not].message ) );
				}
				$('#individual-push-notification span.date').text('Sent '+json[not].tx);
			}
		}
	});
	refreshNotifications();
}

//ticket community
function getMyTicketsAndChats() {
	$.post(api_prefix+'scripts/ajax.php', {url: http+'student_tickets_mine.php?nid='+nid, platform: platform, uuid: getCookie("UUID"), login_key: getCookie("login_key")}, function(data) {
		var json = jQuery.parseJSON( data );
		var myTickets = '';
		if(json.mylistcount > 0) {
			myTickets = '<h5>Listed Tickets</h5><table class="styled" border="0">';
			for(i = 1;i<=json.mylistcount;i++) {
				myTickets += '<tr class="link chats-link"><td><a href="#" data-stid="'+json.mylist[i].id+'">';
				myTickets += '<span>'+json.mylist[i].dt+' - '+json.mylist[i].eventname+'</span>';
				myTickets += '<span>Qty: '+json.mylist[i].qty+'</span>';
				myTickets += '<span>$'+json.mylist[i].price+'</span>';
				myTickets += '<span>'+json.mylist[i].location+'</span>';
				myTickets += '</td></tr>';
			}
			myTickets += '</table>';
		} else {
			myTickets = '<h5>No Tickets</h5>';
		}
		$('#my-tickets').html(myTickets);
		
		var myChats = '';
		if(json.mychatcount > 0) {
			myChat = '<h5>My Chats</h5><table class="styled" border="0">';
			for(i = 1;i<=json.mychatcount;i++) {
				myChat += '<tr class="link chat-link"><td><a href="#" data-stid="'+json.mychat[i].stid+'" data-touid="'+json.mychat[i].uid+'" data-pic="'+json.mychat[i].profile_pic_cropped+'" data-username="'+json.mychat[i].username+'" data-dt="'+json.mychat[i].dt+'" data-eventname="'+json.mychat[i].eventname+'" data-eventdescription="'+json.mychat[i].eventdescription+'" data-qty="'+json.mychat[i].qty+'" data-price="'+json.mychat[i].price+'" data-location="'+json.mychat[i].location+'" data-description="'+json.mychat[i].description+'">';
				myChat += '<img src="'+json.mychat[i].profile_pic_cropped+'">';
				myChat += '<span class="username">'+json.mychat[i].username+'</span>';
				//myChat += '<span>$'+json.mychat[i].price+'</span>';
				myChat += '<span>'+json.mychat[i].dt+' - '+json.mychat[i].eventname+'</span>';
				myChat += '</td></tr>';
			}
			myChat += '</table>';
		} else {
			myChat = '<h5>No Chats</h5>';
		}
		$('#my-chats').html(myChat);
	});
	getTaggedTicketEvents();
}

function getTaggedTicketEvents() {
	$.post(api_prefix+'scripts/ajax.php', {url: http+'student_tickets_events.php?nid='+nid, platform: platform, uuid: getCookie("UUID"), login_key: getCookie("login_key")}, function(data) {
		var json = jQuery.parseJSON( data );
		if(json.count > 0) {
			taggedTicketEvents = json.events;
			taggedTicketEventsCount = json.count;
		} else {
			taggedTicketEvents = false;
			taggedTicketEventsCount = 0;
		}
		updateStudentTicketsSellSelect();
		updateStudentTicketsBuySelect();
	});
}

function updateStudentTicketsSellSelect() {
	var select;
	if(taggedTicketEvents != false) {
		select = '<option value="" disabled selected></option>';
		for(_i=1;_i<=taggedTicketEventsCount;_i++) {
			select+='<option value="'+taggedTicketEvents[_i].eid+'">'+taggedTicketEvents[_i].dt+' - '+taggedTicketEvents[_i].eventname+'</option>';
		}
	} else {
		select = '<option value="" disabled selected>No available events</option>';
		//console.log('there were no taggedTicketEvents');
	}
	$('#student-tickets-sell select').html(select);
	$('#student-tickets-sell select').selectmenu();
    $('#student-tickets-sell select').selectmenu('refresh', true);
}

function updateStudentTicketsBuySelect() {
	var select;
	if(taggedTicketEvents != false) {
		select = '<option value="" selected>All</option>';
		for(_i=1;_i<=taggedTicketEventsCount;_i++) {
			select+='<option value="'+taggedTicketEvents[_i].eid+'">'+taggedTicketEvents[_i].dt+' - '+taggedTicketEvents[_i].eventname+'</option>';
		}
	} else {
		select = '<option value="" selected>No available events</option>';
		//console.log('there were no taggedTicketEvents');
	}
	$('#student-tickets-buy select').html(select);
	$('#student-tickets-buy select').selectmenu();
    $('#student-tickets-buy select').selectmenu('refresh', true);
    getAvailableTickets();
}

function getAvailableTickets() {
	$('#student-tickets-buy-tickets').html('');
	$.post(api_prefix+'scripts/ajax.php', {url: http+'student_tickets_buy.php?nid='+nid, platform: platform, uuid: getCookie("UUID"), login_key: getCookie("login_key"), eid: $('#student-tickets-buy-eid').val()}, function(data) {
		var json = jQuery.parseJSON( data );
		var tickets = '';
		if(json.response == 'ok') {
			if(json.count > 0) {
				tickets = '<table class="styled" border="0">';
				for(i = 1;i<=json.count;i++) {
					tickets += '<tr class="link chat-link"><td><a href="#" data-stid="'+json.tickets[i].id+'" data-touid="'+json.tickets[i].uid+'" data-pic="'+json.tickets[i].profile_pic_cropped+'" data-username="'+json.tickets[i].username+'" data-dt="'+json.tickets[i].dt+'" data-eventname="'+json.tickets[i].eventname+'" data-eventdescription="'+json.tickets[i].eventdescription+'" data-qty="'+json.tickets[i].qty+'" data-price="'+json.tickets[i].price+'" data-location="'+json.tickets[i].location+'" data-description="'+json.tickets[i].description+'">';
					tickets += '<img src="'+json.tickets[i].profile_pic_cropped+'">';
					tickets += '<span class="username">'+json.tickets[i].username+'</span>';
					tickets += '<span>'+json.tickets[i].dt+' - '+json.tickets[i].eventname+' '+json.tickets[i].eventdescription+'</span>';
					tickets += '<span>Qty: '+json.tickets[i].qty+'</span>';
					tickets += '<span>$'+json.tickets[i].price+'</span>';
					tickets += '<span>'+json.tickets[i].location+'</span>';
					tickets += '<span>'+json.tickets[i].description+'</span>';
					tickets += '</td></tr>';
				}
				tickets += '</table>';
			}
			$('#student-tickets-buy-tickets').html(tickets);
		} else {
			alert(json.msg);
		}
	});
}

function getStudentTicketsChat(stid, threadid, touid) {
	$('#student-tickets-chat-chat').html('');
	$.post(api_prefix+'scripts/ajax.php', {url: http+'student_tickets_chat.php?nid='+nid, platform: platform, uuid: getCookie("UUID"), login_key: getCookie("login_key"), stid: stid, threadid: threadid}, function(data) {
		var json = jQuery.parseJSON( data );
		if(json.response == 'ok') {
			$('#student-tickets-chat-chat').html('');
			var chat = '';
			for(_i = 1; _i<=json.count; _i ++) {
				console.log(user.uid);
				if(json.chat[_i].to_uid == user.uid ) {
					
					chat+= '<div class="chat-them">';
					
				} else {
				
					chat+= '<div class="chat-me">';
					
				}
				
				chat+= '<img src="'+json.chat[_i].from_pic+'">';
				chat+= '<span class="username">'+json.chat[_i].from_user+'</span>';
				chat+= '<div class="chat">'+json.chat[_i].message+'</div><div style="clear: both"></div>';
				chat+= '</div>';
				
			}
			
			chat+= '<div class="chat-bar"><input id="student-tickets-buy-message"><a href="#" id="student-tickets-buy-message-send" data-stid="'+stid+'" data-touid="'+touid+'">Send</a></div>';
			
			$('#student-tickets-chat-chat').html(chat);
			
		} else {
			alert(json.msg);
		}
	});
}

function getTicketChats(stid) {
	$.post(api_prefix+'scripts/ajax.php', {url: http+'student_tickets_chats.php?nid='+nid, platform: platform, uuid: getCookie("UUID"), login_key: getCookie("login_key"), stid: stid}, function(data) {
		var json = jQuery.parseJSON( data );
		var chats = '';
		if(json.count > 0) {
			chat = '<h5>Chats for these Tickets</h5><table class="styled chats" border="0">';
			for(i = 1;i<=json.count;i++) {
				chat += '<tr class="link chat-link"><td><a href="#" data-stid="'+json.chats[i].stid+'" data-touid="'+json.chats[i].uid+'" data-pic="'+json.chats[i].profile_pic_cropped+'" data-username="'+json.chats[i].username+'" data-dt="'+json.chats[i].dt+'" data-eventname="'+json.chats[i].eventname+'" data-eventdescription="'+json.chats[i].eventdescription+'" data-qty="'+json.chats[i].qty+'" data-price="'+json.chats[i].price+'" data-location="'+json.chats[i].location+'" data-description="'+json.chats[i].description+'">';
				chat += '<img src="'+json.chats[i].profile_pic_cropped+'">';
				chat += '<span class="username">'+json.chats[i].username+'</span>';
				//myChat += '<span>$'+json.mychat[i].price+'</span>';
				chat += '<span class="message">'+json.chats[i].message+'</span>';
				chat += '<span class="date">'+json.chats[i].msg_tx+'</span>';
				chat += '</td></tr>';
			}
			chat += '</table>';
		} else {
			chat = '<h5>No chats yet</h5>';
		}
		$('#ticket-chats').html(chat);
	});
}

function refreshNOHSStudentID() {
    $('#nohs-student-id .ui-content').html('<div class="loading"><img src="img/ajax-loader.gif" /><p>Loading Student ID...</p></div>');
	$.post(api_prefix+'scripts/nohs_student_id.php', {url: http+'nohs_sports_pass.php?nid='+nid, platform: platform, uuid: uuid, login_key: login_key},  function(data) {
		$('#nohs-student-id .ui-content').html(data);
	});
}


//shared
function checkForNewHomescreen() {
	if( $('#homescreen-bg-video source').attr('src') == '' && homescreenBGVideo.length > 0 ) {
		//console.log('video found!');
		$('#homescreen-bg-video source').attr('src', homescreenBGVideo[0]);
		$('#homescreen-bg-video').get(0).load();
		//show the video
		$('#homescreen-bg-video').show();
		$('#homescreen-bg-video').get(0).play();
	} else {
		//console.log('no video found :(');
	}
	$.post(api_prefix+'scripts/ajax.php', {url: http+'check_for_homescreen_updates.php?nid='+nid, platform: platform, uuid: getCookie("UUID"), login_key: getCookie("login_key")},  function(data) {
		try {
			var json = jQuery.parseJSON( data );
		} catch(e) {
			$('#homescreen-bg-img').css('background-image','url('+homescreenBGImg+')');
			return false;
		}
		if(json != '') {
			if(json.homescreen) {
				//TODO: check for video
				//is it a video?
				if( json.homescreen.slice(-4) == '.mp4' || json.homescreen.slice(-4) == '.mov' ) {
					//set the homescreen to the default
					$('#homescreen-bg-img').css('background-image','url('+homescreenBGImg+')');
					//set the source of the video
					var video = $('#homescreen-bg-video')[0];
					video.src = json.homescreen;
					video.load();
					video.play();
					//show the video
					$('#homescreen-bg-video').show();
				} else {
					//it's an image or a gif, so just show the homescreen img
					$('#homescreen-bg-img').css('background-image','url('+json.homescreen+')');
					//and hide the video background
					$('#homescreen-bg-video').hide();
				}
				
			} else {
				//reset
				$('#homescreen').css('background-image','url('+homescreenBGImg+')');
				//is there a video?
				if( homescreenBGVideo.length > 0 ) {
					$('#homescreen-bg-video source').attr('src', homescreenBGVideo[0]);
					$('#homescreen-bg-video').load();
					//show the video
					$('#homescreen-bg-video').show();
				} else {
					$('#homescreen-bg-video').hide();
				}
			}
			
			if(json.modules) {
				$('#homescreen-modules-img').css('background-image','url('+json.modules+')');
			} else {
				//reset to default
				$('#homescreen-modules-img').css('background-image','url('+homescreenModulesImg+')');
			}
			
			if(json.sponsor) {
				$('#homescreen-sponsor-img').css('background-image','url('+json.sponsor+')');
			} else {
				//reset to default
				$('#homescreen').css('background-image','url('+homescreenSponsorImg+')');
			}
			
			if( json.home_touch ) {
				home_touch_target_action = json.home_touch;
			} else {
				home_touch_target_action = null;
			}

			
		} else {
			$('#homescreen').css('background-image','url('+homescreenBGImg+')');
		}
	});
}

function checkForAds(pageKey, pageHash) {
	if(pageHash.indexOf('#') == -1) {
		pageHash = '#' + pageHash;
	}
	$.post(api_prefix+'scripts/ajax.php', {url: http+'get_page_ad.php?nid='+nid, platform: platform, uuid: getCookie("UUID"), login_key: getCookie("login_key"), pageKey: pageKey},  function(data) {
		try {
			var json = jQuery.parseJSON( data );
		} catch(e) {
			return false;
		}
		if(json == '') {
			$(pageHash+' .ad-footer').hide();
		} else {
			if(json.ad_type == 'interstitial') {
				var adheight = $(window).height() - 120;
				$(pageHash+' .ad-interstitial').show().find('.ad-body').css('height',adheight+'px').find('img').attr('src',json.media).attr('data-url', json.url).attr('data-eid','0');

			} else if(json.ad_type == 'banner') {
				$(pageHash+' .ad-footer img').attr('src',json.media).attr('data-url', json.url).attr('data-eid','0').parent().show();
			}
		}
	});
}


/* fb */

function fbInit() {
	fbReady = false;
	FB.init({
		appId  : fbAppId,
		status : true, // check login status
		//cookie : true, // enable cookies to allow the server to access the session
		xfbml  : true, // parse XFBML
		channelUrl : rooturl+'fb-channel.php', // channel.html file
		//oauth  : true // enable OAuth 2.0
	});
	fbReady = true;
	FB.getLoginStatus(function(response) {
		facebookStatus(response.status);
	});
}

function fbLogin(success, failure) {
	if (typeof(success) != "function") {success = function() {};}
	if (typeof(failure) != "function") {failure = function() {};}
	fbLast.success = success;
	fbLast.failure = failure;
	if(platform == 'Android') {
		var winloc = "https://m.facebook.com/dialog/oauth?client_id=" + fbAppId + "&response_type=code&redirect_uri=" + rooturl + "fb-closewindow.php&scope=" + fbPermissions;
		window.location = 'modal:'+winloc;
	} else {
		FB.login(function(response) {
			console.log('FB.login response');
			console.log(response);
			if (response.authResponse) {
	   			//everything good
	   			fbLast.success();
	   			fbLast.success = function() {};
	   		} else {
		   		//alert('User cancelled login or did not fully authorize.');
		   		fbLast.failure();
		   		fbLast.failure = function() {};
		   	}
		},{scope: fbPermissions});
	}
}

function fbLoginResponse() {
	FB.getLoginStatus(function(response) {
		console.log('response status: '+response.status);
		if (response.status === 'connected') {
			if(fbLast.action == 'share') {
				var uid = response.authResponse.userID;
			    var accessToken = response.authResponse.accessToken;
			    postToFB();
			}
			if(fbLast.action == 'login') {
				loginViaFacebook();
			}
			if(fbLast.action == 'register') {
				loginViaFacebook();
			}
			fbLast.success();
			fbLast.success = function() {};
		} else {
			fbLast.failure();
			fbLast.failure = function() {};
		}
	}, true);
}

function androidLoginViaFacebook(uuid, response, platformVersion, phoneModel, isJailbroken) {
	$.post(api_prefix+'scripts/fb-login.php', {url: http+'fb_login.php?nid='+nid, platform: 'Android', uuid: getCookie("UUID"), fbdata: response, version: platformVersion, app_version: appVersion, architecture: '32bit', model: phoneModel, osname: platform, ostype: "32bit", is_jailbroken: isJailbroken}, function(data) {
		try {
		    console.log(data);
 			var rr = jQuery.parseJSON( data );
            console.log("Trying to login to Facebook from Droid");
		} catch(e) {
			alert('We are not able to log you in at this time. Please try again in a moment.');
			console.log(e);
			$('#fb-login-btn p').text('Login with Facebook');
			$('#fb-register-btn p').text('Register with Facebook');
			return;
		}
		console.log("response: " + rr.response);
		if(rr.response=='ok') {
			loggedIn = null;
			updateLoginKey( rr.loginkey );
			checkLogin();
			if(rr.type == 'register') {
				Ti.App.fireEvent('askProfileQuestion', {username: jj.username, email: jj.email});
			}
			//fbLast.action = null;
		} else {
			$('#fb-login-btn p').text('Login with Facebook');
			$('#fb-register-btn p').text('Register with Facebook');
			alert('There was an error connecting with facebook. Try again in a moment');
		}
		$('#fb-login-btn p').text('Login with Facebook');
		$('#fb-register-btn p').text('Register with Facebook');

	});
}

function loginViaFacebook() {
	$.post(api_prefix+'scripts/ajax.php', {url: http+'new_session.php?num=64'}, function(data) {
		console.log(data);
		if(getVar('uuid')==undefined) {
			console.log('no uuid');
			console.log('cookie: '+getCookie('UUID'));
			if(getCookie('UUID')!=null) {
				uuid = getCookie('UUID');
			} else {
				uuid = data;
			}
		} else {uuid = getVar('uuid')};
		setCookie('UUID',uuid,365);
		FB.api('/me?fields=id,name,first_name,last_name,email,age_range,link,gender,locale,timezone,updated_time,verified', function(response) {
			if( typeof(response) == 'object' ) {
				response = JSON.stringify( response );
				console.log('response was an object, now its a string');
			}
			$.post(api_prefix+'scripts/fb-login.php', {url: http+'fb_login.php?nid='+nid, platform: platform, uuid: getCookie("UUID"), fbdata: response, version: platformVersion, app_version: appVersion, architecture: ostype, model: phoneModel, osname: platform, ostype: "32bit", is_jailbroken: isJailbroken}, function(data) {
				try {
					var rr = jQuery.parseJSON( data );
				} catch(e) {
					alert('We are not able to log you in at this time. Please try again in a moment.');
					$('#fb-login-btn p').text('Login with Facebook');
					$('#fb-register-btn p').text('Register with Facebook');
					return;
				}
				if(rr.response=='ok') {
					loggedIn = null;
					updateLoginKey( rr.loginkey );
					checkLogin();
					if(rr.type == 'register') {
						Ti.App.fireEvent('askProfileQuestion', {username: jj.username, email: jj.email});
					}
					//fbLast.action = null;
				} else {
					$('#fb-login-btn p').text('Login with Facebook');
					$('#fb-register-btn p').text('Register with Facebook');
					alert('There was an error connecting with facebook. Try again in a moment');
				}
			});
		});
	});
}

function postToFB() {
	if(fbPosting == true) {console.log("it's true");return;}
	fbPosting = true;
	console.log(window.history);
	fbInit();
	setTimeout(function(){
		FB.getLoginStatus(function(response) {
			console.log(response);
			if (response.status === 'connected') {
				//let's post this monster
				var postdata = fbPost;
				
				if(platform == 'Android') {
					var url = 'https://www.facebook.com/dialog/feed?';
						url+='app_id='+fbAppId;
						url+='&display=page';
						url+='&app_id='+fbAppId;
						url+='&name='+fbPost.name;
						url+='&link='+fbPost.link;
						url+='&picture='+fbPost.picture;
						url+='&caption='+fbPost.caption;
						url+='&description='+fbPost.description;
						url+='&redirect_uri='+rooturl + 'fb-close-sharewindow.php';
					
					window.location = 'modal:'+url;
				} else {
					FB.ui(postdata,
						function(response) {
						    if (response && response.post_id) {
						    	$.post(api_prefix+'scripts/ajax.php', {url: http+'fb_post.php?nid='+nid, platform: platform, uuid: getCookie("UUID"), login_key: getCookie("login_key"), eventid: fbPost.eventid}, function(data) {
						    		//shhhhh
						    	});
						    	fbLast.action = null;
								alert('This event was posted to Facebook!');
								fbPosting = false;
						    } else {
								//alert('Sorry, there was an error posting to Facebook');
								fbPosting = false;
								console.log(window.history);
						    }
						}
					);
				}

				
			} else if (response.status === 'not_authorized') {
				//what to do here?
				console.log('logged into facebook, but not authenticated');
				fbPosting = false;
			} else {
				// the user still isn't logged in to Facebook.
				console.log('not logged in');
				fbPosting = false;
			}
		});
	}, 1000); //1 second later
}

function closedShareWindow(posted) {
	//they closed the share window,
	//either as a fail or success
	if(posted == "true") {
		$.post(api_prefix+'scripts/ajax.php', {url: http+'fb_post.php?nid='+nid, platform: platform, uuid: getCookie("UUID"), login_key: getCookie("login_key"), eventid: fbPost.eventid}, function(data) {
    		//shhhhh
    	});
    	fbLast.action = null;
		alert('This event was posted to Facebook!');
	}
	fbPosting = false;
}

/* twitter */
window.twttr=(function(d,s,id){var js,fjs=d.getElementsByTagName(s)[0],t=window.twttr||{};if(d.getElementById(id))return;js=d.createElement(s);js.id=id;js.src="https://platform.twitter.com/widgets.js";fjs.parentNode.insertBefore(js,fjs);t._e=[];t.ready=function(f){t._e.push(f);};return t;}(document,"script","twitter-wjs"));

	//this is for web
	twttr.ready(
		function (twttr) {
			twttr.events.bind('tweet',function (event) {
				$.post(api_prefix+'scripts/ajax.php', {url: http+'twitter_post.php?nid='+nid, platform: platform, uuid: getCookie("UUID"), login_key: getCookie("login_key"), eid: twPost.eid}, function(data) {
	    			//shhhhh
	    			if(data == 'ok') {
		    			alert('Your message was posted to Twitter!');
	    			} else {
		    			alert('There was an error posting to Twitter');
	    			}
	    		});
			});
			twttr.events.bind('retweet',function (event) {
				$.post(api_prefix+'scripts/ajax.php', {url: http+'social_interaction.php?nid='+nid, platform: platform, uuid: getCookie("UUID"), login_key: getCookie("login_key"), source: 'social_feed', social_network: 'twitter', social_uuid: '', action: 'retweet'}, function(data) {
					//i doubt it makes it here anyway...
	    		});
			});
			twttr.events.bind('favorite',function (event) {
				$.post(api_prefix+'scripts/ajax.php', {url: http+'social_interaction.php?nid='+nid, platform: platform, uuid: getCookie("UUID"), login_key: getCookie("login_key"), source: 'social_feed', social_network: 'twitter', social_uuid: '', action: 'retweet'}, function(data) {
	    			//i doubt it makes it here anyway...
	    		});
			});
		}
	);

	//this is for android
	function closedTwitterShareWindow(posted, action, url) {
		//they closed the share window,
		//either as a fail or success
		if(posted == "true") {
			if(action == 'tweet') {
				if( getVarFromURL('in_reply_to', url) != undefined) {
					$.post(api_prefix+'scripts/ajax.php', {url: http+'social_interaction.php?nid='+nid, platform: platform, uuid: getCookie("UUID"), login_key: getCookie("login_key"), source: 'social_feed', social_network: 'twitter', social_uuid: getVarFromURL('in_reply_to', url), action: 'reply'}, function(data) {
			    		if(data == 'ok') {
			    			alert('Your tweet was sent successfully!');
			    			//we should just update the image
							//$('#social-feed li.sf-'+social_uuid+' .actions img.twitter-retweet').attr('src', 'img/social/twitter/retweet_on.png');
						} else {
			    			alert('There was an error tweeting that');
						}
			    	});
				} else {
					$.post(api_prefix+'scripts/ajax.php', {url: http+'twitter_post.php?nid='+nid, platform: platform, uuid: getCookie("UUID"), login_key: getCookie("login_key"), eid: twPost.eid, return_url: url}, function(data) {
			    		if(data == 'ok') {
			    			alert('Your message was posted to Twitter!');
						} else {
			    			alert('There was an error posting to Twitter');
						}
			    	});
				}
			}
			if(action == 'retweet') {
				//https://twitter.com/intent/retweet/complete?tweet_id=thetweetid
				var social_uuid = getVarFromURL('tweet_id',url);
				$.post(api_prefix+'scripts/ajax.php', {url: http+'social_interaction.php?nid='+nid, platform: platform, uuid: getCookie("UUID"), login_key: getCookie("login_key"), source: 'social_feed', social_network: 'twitter', social_uuid: social_uuid, action: action}, function(data) {
		    		if(data == 'ok') {
		    			//alert('You retweeted that tweet!');
		    			//we should just update the image
						$('#social-feed li.sf-'+social_uuid+' .actions img.twitter-retweet').attr('src', 'img/social/twitter/retweet_on.png');
					} else {
		    			alert('There was an error retweeting that');
					}
		    	});
			}
			if(action == 'favorite') {
				//https://twitter.com/intent/favorite/complete?tweet_id=thetweetid&already_favorited=true|false
				var social_uuid = getVarFromURL('tweet_id',url);
				if( getVarFromURL('already_favorited', url) == 'false') {
					$.post(api_prefix+'scripts/ajax.php', {url: http+'social_interaction.php?nid='+nid, platform: platform, uuid: getCookie("UUID"), login_key: getCookie("login_key"), source: 'social_feed', social_network: 'twitter', social_uuid: social_uuid, action: action}, function(data) {
			    		if(data == 'ok') {
			    			//alert('Your favorited this message');
			    			//we should just update the image
							$('#social-feed li.sf-'+social_uuid+' .actions img.twitter-favorite').attr('src', 'img/social/twitter/favorite_on.png');
						} else {
			    			alert('There was an error favoriting this message');
						}
			    	});
				} else {
					alert('You have already favorited this tweet');
					$('#social-feed li.sf-'+social_uuid+' .actions img.twitter-favorite').attr('src', 'img/social/twitter/favorite_on.png');
				}
			}
		}
	}

/* android */
function onAndroidResume() {
	console.log('Android resumed:');
	if(loggedIn == true) {
		console.log('logged in and refreshing...');
		$.event.trigger({type: "moduleRefresh"});
	} else {
		console.log('nevermind, not logged in');
	}
}

/* cookies */
function setCookie(name,value,days) {
    if(platform == "Android") {
        localCookieStorage[name] = value;
    } else {
        if (days) {
            var date = new Date();
            date.setTime(date.getTime()+(days*24*60*60*1000));
            var expires = "; expires="+date.toGMTString();
        }
        else var expires = "";
        document.cookie = name+"="+value+expires+"; path=/";
    }
}

function getCookie(name) {
    if(platform == "Android") {
        console.log(`COOOOKIES: ${name}, ${localCookieStorage[name]}`);
        console.log("GETTING COOKIES");
        return localCookieStorage[name];
    } else {
        var nameEQ = name + "=";
        var ca = document.cookie.split(';');
        for(var i=0;i < ca.length;i++) {
            var c = ca[i];
            while (c.charAt(0)==' ') c = c.substring(1,c.length);
            if (c.indexOf(nameEQ) == 0) return c.substring(nameEQ.length,c.length);
        }
        return null;
    }
}

function refreshLocalCookieStorage() {
    if(getVar('platform') == "Android") {
        console.log("refreshLocalCookieStorage platform mainactivity: " + getVar('platform'));
        //window.location = "setcookie:/chris/awesome";
        window.location = "getallcookies:/";
    }
}

function androidGetCookieCallback(data) {
    data = JSON.parse(data);
    console.log("COOOOKIES: " + data);
    for(var key in data) {
        if(data.hasOwnProperty(key)) {
            console.log(`COOOOKIES => key: ${key}, property: ${data[key]}`);
            localCookieStorage[key] = data[key];
        }
    }

	//FB
	fbInit();

	//check login
	checkLogin();
}

function androidPullCookies() {
    cookies = JSON.stringify(localCookieStorage);
    console.log("finished gethere: " + cookies);
    window.location = "sendallcookies:/" + cookies;
    console.log("finished getthere");
    $.event.trigger({type: "moduleRefresh"});
}

function deleteCookie(name) {
    setCookie(name,"",-1);
}

function getVar(name){
   if(name=(new RegExp('[?&]'+encodeURIComponent(name)+'=([^&]*)')).exec(location.search))
      return decodeURIComponent(name[1]);
}

function getVarFromURL(name,url){
   if(name=(new RegExp('[?&]'+encodeURIComponent(name)+'=([^&]*)')).exec(url))
      return decodeURIComponent(name[1]);
}

function linkify(inputText) {
    var replacedText, replacePattern1, replacePattern2, replacePattern3;

    //URLs starting with http://, https://, or ftp://
    replacePattern1 = /(\b(https?|ftp):\/\/[-A-Z0-9+&@#\/%?=~_|!:,.;]*[-A-Z0-9+&@#\/%=~_|])/gim;
    replacedText = inputText.replace(replacePattern1, '<a href="$1" target="_blank">$1</a>');

    //URLs starting with "www." (without // before it, or it'd re-link the ones done above).
    replacePattern2 = /(^|[^\/])(www\.[\S]+(\b|$))/gim;
    replacedText = replacedText.replace(replacePattern2, '$1<a href="http://$2" target="_blank">$2</a>');

    //Change email addresses to mailto:: links.
    replacePattern3 = /(([a-zA-Z0-9\-\_\.])+@[a-zA-Z\_]+?(\.[a-zA-Z]{2,6})+)/gim;
    replacedText = replacedText.replace(replacePattern3, '<a href="mailto:$1">$1</a>');

    return replacedText;
}

function hexToRgb(hex) {
    var result = /^#?([a-f\d]{2})([a-f\d]{2})([a-f\d]{2})$/i.exec(hex);
    return result ? {
        r: parseInt(result[1], 16),
        g: parseInt(result[2], 16),
        b: parseInt(result[3], 16)
    } : null;
}

function tintImage(image, image_src, image_class, r, g, b) {
    
    if(image == null) {
	    image = new Image;
		image.src = image_src;
    }
    var newImg = $('<canvas>')[0];
    newImg.width = image.width;
    newImg.height = image.height;
    newImg.className = image_class;
    var newCtx = newImg.getContext('2d');
    newCtx.drawImage(image, 0, 0);
    
    var imageData = newCtx.getImageData(0, 0, image.width, image.height);
    data = imageData.data;
    for (var i = 0; i < data.length; i += 4) {
        data[i+0] = r;
        data[i+1] = g;
        data[i+2] = b;
    }
    newCtx.putImageData(imageData, 0, 0);
    
    return newImg;
}

console.log("done loading. sfu.js");