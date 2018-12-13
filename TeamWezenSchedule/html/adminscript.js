var base_url = "https://kiljpq1n4a.execute-api.us-east-2.amazonaws.com/Alpha";
var schedulelist = ["im a schedule","another schedule"];
// window.onload = getList;


function displaySchedules(){
  let list = document.getElementById("scheduleList");
  list.innerHTML = "";//empty the current list
  if(schedulelist.length == 0){
    let nonefound = document.createElement("P");
    nonefound.innerText = "No Schedules Match That Criteria";
    list.appendChild(nonefound);
  }
  for(let i=0; i<schedulelist.length;i++){
    let curr_sched = document.createElement("li");
    curr_sched.className = "schedules";
    curr_sched.listval = schedulelist[i];
    curr_sched.innerHTML = curr_sched.listval + "  ";
    list.appendChild(curr_sched);
  }
}

function getList(){
  var range = document.getElementById("viewMenu").value;
  var userInput = document.getElementById("sort").value;

  if(range == "all"){
    range = "day";
    userInput = "0";
  }
  if(userInput == ""){
    alert("Please Enter a number");
    return;
  }
  //request for list
  let data = {};
  data["requestRange"] = String(range);
  data["requestNumDaysOrHours"] = String(userInput);
  let getlist_url = base_url + "/adminget";
  sendData(data,getlist_url);
}


function handleDeleteSchedule(sched){
    // let range = document.getElementById("viewMenu").value;
    let userInput = document.getElementById("sort").value;
    if(userInput == ""){
      alert("Please Enter a number");
      return;
    }

    //request for list
    let data = {};
    data["requestNumDays"] = String(userInput);
    let deletelist_url = base_url + "/admindelete";
    sendData(data,deletelist_url);
}

function toggleSort(){
    let selection = document.getElementById("viewMenu");
    if(selection.value == "hr"){
      document.getElementById("listdelete").style.display = "none";
      document.getElementById("sortForm").style.visibility = "visible";
      document.getElementById("sortlabel").innerHTML = " Hours";
    }else if(selection.value == "day"){
      document.getElementById("listdelete").style.display = "inline";
      document.getElementById("sortForm").style.visibility = "visible";
      document.getElementById("sortlabel").innerHTML = " Days";
    }else{
      document.getElementById("listdelete").style.display = "none";
      document.getElementById("sortForm").style.visibility = "hidden";
    }
}



function sendData(data,url){
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
      processList(xhr.responseText);
    } else {
      processList(xhr.responseText);
    }
  };
}

function processList(xhrResult){
	console.log("result:" + xhrResult);
	let js = JSON.parse(xhrResult);

  //add if schedule couldnt be opened, display "could not find schedule"
  if((js["response"]=="Successfully got list")||(js["response"]=="Successfully deleted schedules")){
    schedulelist = js["responseList"];
    displaySchedules();
  }else{
    alert(js["response"]);
  }
}
