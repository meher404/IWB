//This callback function is called when the content script has been 
//injected and returned its results
function onPageInfo(o)  { 

}

//Global reference to the status display SPAN
var statusDisplay = null;

//POST the data to the server using XMLHttpRequest
function addBookmark() {
	// Cancel the form submit
	event.preventDefault();
//alert("on load");
	// The URL to POST our data to
	var postUrl = "http://localhost:8080/IWBDPlugin/SentMessage?wrd="+document.getElementById('summary').value+"&ed="+document.getElementById('enterTextHere').value;

	// Set up an asynchronous AJAX POST request
	var xhr = new XMLHttpRequest();
	xhr.open('get', postUrl, true);

	// Prepare the data to be POSTed

	var post = encodeURIComponent(document.getElementById('enterTextHere').value);
	// var summary = encodeURIComponent(document.getElementById('summary').value);

	var params = post;



	// Set correct header for form data 
	xhr.setRequestHeader('Content-type', 'multipart/form-data');

	// Handle request state change events
	xhr.onreadystatechange = function() { 
		// If the request completed
		if (xhr.readyState == 4) {
			//statusDisplay.innerHTML = '';
			if (xhr.status == 200) {
				// If it was a success, close the popup after a short delay
				document.getElementById('enterTextHere').value="";
				a=document.getElementById("summary").value;
				b=xhr.responseText;
				c="\nkishan";
				//alert(b);
				//alert(a+b);
				statusDisplay.innerHTML =   a+b;
			} else {
				// Show what went wrong
				document.getElementById('enterTextHere').value="";
				statusDisplay.innerHTML = a+b+"something went wrong..\nplease check your net connection...";
			}
		}
	};

	// Send the request and set status
	
	xhr.send(params);
	//statusDisplay.innerHTML = 'Sending...';
}

//When the popup HTML has loaded
window.addEventListener('load', function(evt) {
	// Bind our addBookmark function to the form submit event
	document.getElementById('addbookmark').addEventListener('submit', addBookmark);
	// Cache a reference to the status display SPAN
	statusDisplay = document.getElementById('summary');
	// Call the getPageInfo function in the background page, injecting
	// content_script.js into the current HTML page and passing in our 
	// onPageInfo function as the callback
	
	
	//chrome.extension.getBackgroundPage().getPageInfo(onPageInfo);
	//above line is commented only because of the error on console
});


chrome.tabs.query({'active': true, 'windowId': chrome.windows.WINDOW_ID_CURRENT},	
		function(tabs){
	//alert(tabs[0].url);
	// console.log(tab[0].url);
	//console.log(tab[0].title);
	document.getElementById("get_url").innerHTML=tabs[0].url;
	//document.getElementById("get_url").innerHTML=tabs[0].title;
	setInterval(function(){getChat();},3000);	
}
);

				/*to view all tabs in the chrome, uncomment below lines of code and also uncomment lines of code in popup.html*/
//chrome.tabs.getAllInWindow(null, function(tabs) {
//	tabs.forEach(function(tab){
//		myFunction(tab.title);  
//	});
//});
//
//function myFunction(tablink) {
//	//console.log(tablink);
//	var oNewNode = document.createElement("LI");
//	urlList.appendChild(oNewNode);
//	oNewNode.innerText=tablink;  
//}
/* **********************************ends here*********************************** */


function getChat() {
	// Cancel the form submit
	// event.preventDefault();

	// The URL to POST our data to
	var postUrl = "http://localhost:8080/IWBDPlugin/TestMessage";//?wrd="+document.getElementById('summary').value+"&ed="+document.getElementById('enterTextHere').value;

	// Set up an asynchronous AJAX POST request
	var xhr = new XMLHttpRequest();
	xhr.open('get', postUrl, true);

	// Prepare the data to be POSTed

	var post = encodeURIComponent(document.getElementById('enterTextHere').value);
	// var summary = encodeURIComponent(document.getElementById('summary').value);

	var params = post;
	// Set correct header for form data 
	xhr.setRequestHeader('Content-type', 'multipart/form-data');

	// Handle request state change events
	xhr.onreadystatechange = function() { 
		// If the request completed
		if (xhr.readyState == 4) {
			//statusDisplay.innerHTML = '';
			if (xhr.status == 200) {
				a=document.getElementById("summary").value;
				b=xhr.responseText;

				if(xhr.responseText=="noNewMessages\n\n")
					{
						alert("from if : "+b);
					}
				else
					{
						alert("from else : "+b);
						statusDisplay.innerHTML =   a+b;
					}

			} else {
				// Show what went wrong
				//statusDisplay.innerHTML = document.getElementById("enterTextHere").value + xhr.statusText;
			}
		}
	};

	// Send the request and set status
	xhr.send(params);
	//statusDisplay.innerHTML = 'Sending...';
}


document.addEventListener('DOMContentLoaded', function () {
	document.getElementById('logout').addEventListener('click', change);
	});

function change()
{
	alert("lolz");
	 var popwin = window.open("https://mail.google.com/mail/u/0/?logout&hl=en");
	 setTimeout(function(){ popwin.close(); window.location.href='http://1-dot-iwb-auth-01.appspot.com';},5000);
	//window.location.href="New.html";
	//chrome.browserAction.setPopup({popup: "http://localhost:8080/IWBDPlugin/New.html"});
}

/* to create a new tab */
//chrome.tabs.create({'url':this.href, active: false }, function (tab) {
//	  setTimeout(function () {
//	    chrome.tabs.remove(tab.id);
//	  }, 5000);
//	});