//
var base_url = "https://kiljpq1n4a.execute-api.us-east-2.amazonaws.com/Alpha";
var createSchedule_url = base_url + "/createschedule";

function openPrompt(e){
  document.getElementById('orgtools').style.display='block';
  return false;
}
function handleSubmit(e){
  if(checkValidResponse()){
    document.getElementById("orgtools").style.display = 'none';
    createSchedule();
  }else{
    alert("Please Fill Out All Fields");
  }
  return false;
}
function checkValidResponse(){
  let sd = (document.getElementById("setstartday").value != "");
  let ed = (document.getElementById("setendday").value != "");
  let ts = (document.getElementById("settimeslots").value != "");
  let dst = (document.getElementById("settimestart").value != "");
  let det = (document.getElementById("settimeend").value != "");

  return (sd && ed && ts && dst && det);
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
