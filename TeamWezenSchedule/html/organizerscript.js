//
var base_url = "https://kiljpq1n4a.execute-api.us-east-2.amazonaws.com/Alpha";
var createSchedule_url = base_url + "/createSchedule";

function handleSubmit(){
  document.getElementById("orgtools").style.display = 'none';
  createSchedule();
  return false;  
}

function processCreateScheduleResponse(result) {
	console.log("result: " + xhrRequest);
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
  
  let data = {};
  data["startDate"] = startDay;
  data["endDate"] = endDay;
  data["startTime"] = dailyStartTime;
  data["endTime"] = dailyEndTime;
  data["slotDuration"] = timeslot;
  
  let js = JSON.stringify(data);
  console.log("JS: " + js);
  let xhr = new XMLHttpRequest();
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
