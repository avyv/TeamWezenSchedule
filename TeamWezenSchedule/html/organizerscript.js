var base_url = "https://kiljpq1n4a.execute-api.us-east-2.amazonaws.com/Alpha";
var createSchedule_url = base_url + "/createschedule";


function openPrompt(e){
  document.getElementById('orgtools').style.display='block';
  return false;
}
function handleSubmitSched(e){
  if(checkValidResponse()){
    document.getElementById("orgtools").style.display = 'none';
    createSchedule();
  }else{
    alert("Please Fill Out All Fields");
  }
  return false;
}
function checkValidResponse(){
  let sid = (document.getElementById("setschedid").value != "");
  let sd = (document.getElementById("setstartday").value != "");
  let ed = (document.getElementById("setendday").value != "");
  let ts = (document.getElementById("settimeslots").value != "");
  let dst = (document.getElementById("settimestart").value != "");
  let det = (document.getElementById("settimeend").value != "");

  return (sd && ed && ts && dst && det && sid);
}



function createSchedule(){
  let sid = document.getElementById("setschedid").value
  let startDay = document.getElementById("setstartday").value;
  let endDay = document.getElementById("setendday").value;
  let timeslot = document.getElementById("settimeslots").value;
  let dailyStartTime = document.getElementById("settimestart").value;
  let dailyEndTime = document.getElementById("settimeend").value;
  let data = {};
  data["startDate"] = String(startDay);
  data["endDate"] = String(endDay);
  data["startTime"] = String(dailyStartTime);
  data["endTime"] = String(dailyEndTime);
  data["slotDuration"] = String(timeslot);
  data["id"] = String(sid);
  sendOrgData(data,createSchedule_url,processCreateScheduleResponse);
}
function processCreateScheduleResponse(xhrResult) {
	console.log("result: " + xhrResult);
	let js = JSON.parse(xhrResult);
	let response = js["createScheduleResponse"];
  alert(response);
}

function sendOrgData(data,url,processcallback){
  let js = JSON.stringify(data);
  console.log("JS:" + js);
  let xhr = new XMLHttpRequest();
  xhr.open("POST", url, true);
  xhr.send(js);
  xhr.onloadend = function () {
    console.log(xhr);
    console.log(xhr.request);
    if(xhr.readyState == XMLHttpRequest.DONE) {
      console.log("XHR:" + xhr.responseText);
      processcallback(xhr.responseText);
    } else {
      processcallback(xhr.responseText);
    }
  };
}
