//
var base_url = "https://kiljpq1n4a.execute-api.us-east-2.amazonaws.com/Alpha";
var createSchedule_url = base_url + "/createschedule";

function handleSubmit(e){
  document.getElementById("orgtools").style.display = 'none';
  createSchedule();
  return false;  
}

function processCreateScheduleResponse(xhrResult) {
	console.log("result: " + xhrResult);
	let js = JSON.parse(xhrResult);
	
	let response = js["createScheduleResponse"];
	
	alert(response);
	
}

function createSchedule(){
  // alert("Schedule Created");
  let startDay = document.getElementById("setstartday").value;
  let endDay = document.getElementById("setendday").value;
  let timeslot = document.getElementById("settimeslots").value;
  let dailyStartTime = document.getElementById("settimestart").value;
  let dailyEndTime = document.getElementById("settimeend").value;
  
  var data = {};
  data["startDate"] = String(startDay);
  data["endDate"] = String(endDay);
  data["startTime"] = String(dailyStartTime);
  data["endTime"] = String(dailyEndTime);
  data["slotDuration"] = String(timeslot);
  
  var js = JSON.stringify(data);
  console.log("JS: " + js);
  var xhr = new XMLHttpRequest();
  xhr.open("POST", createSchedule_url, true);
  
  xhr.send(js);
  
  xhr.onloadend = function () {
  	console.log(xhr);
  	console.log(xhr.request);
  	if(xhr.readyState == XMLHttpRequest.DONE) {
  		console.log("XHR: " + xhr.responseText);
  		processCreateScheduleResponse(xhr.responseText);
  	} else {
  		processCreateScheduleResponse("N/A");
  	}
  }
  
}
