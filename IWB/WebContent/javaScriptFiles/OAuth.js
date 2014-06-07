
/**
 * Global variables to hold the profile and email data.
 */
var profile, email, name, profile;

/*
 * Triggered when the user accepts the sign in, cancels, or closes the
 * authorization dialog.
 */
function loginFinishedCallback(authResult) {
	if (authResult) {
		if (authResult['error'] == undefined){
			gapi.client.load('plus','v1', loadProfile);  // Trigger request to get the email address.
		} else {
			console.log('An error occurred');
		}
	} else {
		console.log('Empty authResult');  // Something went wrong
	}
}

/**
 * Uses the JavaScript API to request the user's profile, which includes
 * their basic information. When the plus.profile.emails.read scope is
 * requested, the response will also include the user's primary email address
 * and any other email addresses that the user made public.
 */
function loadProfile(){
	var request = gapi.client.plus.people.get( {'userId' : 'me'} );
	request.execute(loadProfileCallback);
}

/**
 * Callback for the asynchronous request to the people.get method. The profile
 * and email are set to global variables. Triggers the user's basic profile
 * to display when called.
 */
function loadProfileCallback(obj) {
	profile = obj;

	// Filter the emails object to find the user's primary account, which might
	// not always be the first in the array. The filter() method supports IE9+.
	email = obj['emails'].filter(function(v) {
		return v.type === 'account'; // Filter out the primary email
	})[0].value; // get the email from the filtered results, should always be defined.
	displayProfile(profile);


}

/**
 * Display the user's basic profile information from the profile object.
 */
function displayProfile(profile){
	var error=false;
	try
	{
		var dpName=profile['displayName'];
		var uId=profile['id'];
		var img=profile['image']['url'];
		var dob=profile['birthday'];
		var placeLived=profile['placesLived'][0]['value'];
	}
	catch (e)
	{
		console.log("in catch");
		error=true;
	}
	finally
	{
		console.log("in finally");
		if(error==true)
			window.location.href ="http://www.iwbonline.in/loginObjected.html";
		else
			window.location.href = "http://www.iwbonline.in/LoginServlet?uname="+dpName+"&email="+email+"&uId="+uId+"&img="+img+"&gender="+profile['gender']+"&placeLived="+placeLived+"&dob="+dob;
	}
}

/*if idle more than 3 minutes close */
function closeWhenIdle() {
	idleTime = 0;
	$(document).ready(function () {
		//Increment the idle time counter every minute.
		var idleInterval = setInterval(timerIncrement, 60000); // 1 minute
		console.log("hieer");
		//Zero the idle timer on mouse movement.
		$(this).mousemove(function (e) {
			idleTime = 0;
			console.log(idleTime);
		});
		$(this).keypress(function (e) {
			idleTime = 0;
			console.log(idleTime);
		});
	});

	function timerIncrement() {
		console.log("reached timerIncrement");
		idleTime = idleTime + 1;
		if (idleTime > 3) { // 3 minutes
			window.close();
		}
	}
}