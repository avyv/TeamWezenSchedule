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
  return (sd && ed && sid);
}

function updateEndTime(){
  let start = document.getElementById("settimestart").value;
  let end = document.getElementById("settimeend");
  end.innerHTML = "";
  let st = start.split(":");
  let hrs = parseInt(st[0]) + 1;
  let mins = parseInt(st[1]);
  while(hrs<=24){
    label = hrs + ":" + 0 + mins;
    let option = document.createElement("OPTION");
    option.innerHTML=label;
    option.value = label;
    end.appendChild(option);
    hrs++;
  }
}

function createSchedule(){
  let sid = document.getElementById("setschedid").value
  let startDay = document.getElementById("setstartday").value;
  let endDay = document.getElementById("setendday").value;
  let dailyStartTime = document.getElementById("settimestart").value;
  let dailyEndTime = document.getElementById("settimeend").value;
  let slotDuration = document.getElementById("settimeslots").value;
  let data = {};
  data["id"] = String(sid);
  data["startDate"] = String(startDay);
  data["endDate"] = String(endDay);
  data["startTime"] = String(dailyStartTime);
  data["endTime"] = String(dailyEndTime);
  data["slotDuration"] = String(slotDuration);
  sendOrgData(data,createSchedule_url);
}
function processCreateScheduleResponse(xhrResult) {
	console.log("result: " + xhrResult);
	let js = JSON.parse(xhrResult);
	let response = js["createScheduleResponse"];
  alert(response);
}





/*********************** SUBMIT DATA TO JAVA *********************************/
function sendOrgData(data,url){
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
      processSchedule(xhr.responseText);
    } else {
      processSchedule(xhr.responseText);
    }
  };
}

function processSchedule(xhrResult){
	console.log("result:" + xhrResult);
	let js = JSON.parse(xhrResult);
  let responseSchedule = js["responseSchedule"];
  alert("received Schedule");
  //add if schedule couldnt be opened, display "could not find schedule"
  // generateCalendar();
}
