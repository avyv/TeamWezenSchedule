var base_url = "https://kiljpq1n4a.execute-api.us-east-2.amazonaws.com/Alpha";

var dummyTimeslots = [{startTime:"01:00:00",secretCode:0001,id:"third1",meeting:{name:"Mtng1"},date:"2018-12-03",isOpen:false},
                    {startTime:"01:00:00",meeting:{name:"Mtng2"},secretCode:0002,id:"fourth1",date:"2018-12-04",isOpen:true},
                    {startTime:"01:00:00",secretCode:0003,id:"fifth1",meeting:{name:"Mtng3"},date:"2018-12-05",isOpen:false},
                    {startTime:"01:00:00",meeting:{name:"Mtng4"},secretCode:0004,id:"sixth1",date:"2018-12-06",isOpen:true},
                    {startTime:"01:00:00",meeting:{name:"Mtng5"},secretCode:0005,id:"seventh1",date:"2018-12-07",isOpen:true},
                    {startTime:"01:00:00",secretCode:0006,id:"eighth1",meeting:{name:"Mtng6"},date:"2018-12-08",isOpen:false},
                    {startTime:"01:00:00",meeting:{name:"Mtng7"},secretCode:0007,id:"nineth1",date:"2018-12-09",isOpen:true},

                    {startTime:"02:00:00",secretCode:0008,id:"third2",meeting:{name:" "},date:"2018-12-03",isOpen:true},
                    {startTime:"02:00:00",meeting:{name:" "},secretCode:0009,id:"fourth2",date:"2018-12-04",isOpen:true},
                    {startTime:"02:00:00",secretCode:0010,id:"fifth2",meeting:{name:" "},date:"2018-12-05",isOpen:false},
                    {startTime:"02:00:00",meeting:{name:" "},secretCode:0011,id:"sixth2",date:"2018-12-06",isOpen:true},
                    {startTime:"02:00:00",meeting:{name:" "},secretCode:0012,id:"seventh2",date:"2018-12-07",isOpen:false},
                    {startTime:"02:00:00",secretCode:0013,id:"eighth2",meeting:{name:" "},date:"2018-12-08",isOpen:false},
                    {startTime:"02:00:00",meeting:{name:" "},secretCode:0014,id:"nineth2",date:"2018-12-09",isOpen:true},

                    {startTime:"03:00:00",secretCode:0015,id:"third3",meeting:{name:"Mtng8"},date:"2018-12-03",isOpen:false},
                    {startTime:"03:00:00",meeting:{name:" "},secretCode:0016,id:"fourth3",date:"2018-12-04",isOpen:false},
                    {startTime:"03:00:00",secretCode:0017,id:"fifth3",meeting:{name:"Mtng9"},date:"2018-12-05",isOpen:false},
                    {startTime:"03:00:00",meeting:{name:" "},secretCode:0018,id:"sixth3",date:"2018-12-06",isOpen:false},
                    {startTime:"03:00:00",meeting:{name:"Mtng10"},secretCode:0019,id:"seventh3",date:"2018-12-07",isOpen:true},
                    {startTime:"03:00:00",secretCode:0020,id:"eighth3",meeting:{name:" "},date:"2018-12-08",isOpen:false},
                    {startTime:"03:00:00",meeting:{name:"Mtng11"},secretCode:0021,id:"nineth3",date:"2018-12-09",isOpen:false},

                    {startTime:"04:00:00",secretCode:0022,id:"third4",meeting:{name:"Mtng12"},date:"2018-12-03",isOpen:false},
                    {startTime:"04:00:00",meeting:{name:" "},secretCode:0023,id:"fourth4",date:"2018-12-04",isOpen:true},
                    {startTime:"04:00:00",secretCode:0024,id:"fifth4",meeting:{name:"Mtng13"},date:"2018-12-05",isOpen:false},
                    {startTime:"04:00:00",meeting:{name:" "},secretCode:0025,id:"sixth4",date:"2018-12-06",isOpen:true},
                    {startTime:"04:00:00",meeting:{name:" "},secretCode:0026,id:"seventh4",date:"2018-12-07",isOpen:true},
                    {startTime:"04:00:00",secretCode:0027,id:"eighth4",meeting:{name:"Mtng14"},date:"2018-12-08",isOpen:false},
                    {startTime:"04:00:00",meeting:{name:" "},secretCode:0028,id:"nineth4",date:"2018-12-09",isOpen:true}];

var dummySchedule = {startDate:"2018-12-03", startTime:"01:00:00", slotDuration:15,id:"Test",numSlotsPerDay:4,timeSlots:dummyTimeslots,orgCode:1234,fullEndDate:"2018-12-09",fullStartDate:"2018-12-03"};
var currSchedule = dummySchedule;
var currts = currSchedule.timeSlots[0];

// window.onload = initialize;
// //
// function initialize(){
//
//   generateCalendar();
// }

/**********  Create Calendar Display **********/
function generateCalendar(){
    document.getElementById("daysview").innerHTML = "";
    var weekdays = ["Time","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"];
    let date = currSchedule.startDate;
    let parseddate = date.split("-");
    let d = parseddate[2];
    let m = parseddate[1];
    let y = parseddate[0];
    let calendar = document.getElementById("daysview");
    /********** header ************/
    let header = calendar.insertRow(0);
    header.id = "timelbl";
    let head = header.insertCell(-1);
    var html = weekdays[0];
    head.innerHTML = html;

    /********* Calendar *********/
    let st = currSchedule.startTime.split(":");
    let dur = currSchedule.slotDuration;
    let timelabel = document.createElement("td");
    var tsiterator = 0;
    let filt = document.getElementById("filterTime");//update filter while im here
    filt.innerHTML = "<option value=0>--none--</option>"
    var hrs;
    var mins;
    for(let row=1; row<=(currSchedule.numSlotsPerDay); row++){
      let filteroption = document.createElement("OPTION");
      /********** Time Slot Label **********/
      let labelslot = currSchedule.timeSlots[tsiterator];
      var min = labelslot.startTime.minute;
      if(min<10){
        min = "0" + min;
      }
      label = labelslot.startTime.hour + ":" + min;
      let tsrow = calendar.insertRow(-1);
      timeslot = tsrow.insertCell(0);
      timeslot.innerHTML = label;
      timeslot.id = "daylbl";
      //update filter dropdown menu
      filteroption.innerHTML = label;
      filt.appendChild(filteroption);
      /********** Time Slots **********/
      for(let d=1;d<=7;d++){
        let thisSlot = tsrow.insertCell(-1);
        let myslot = currSchedule.timeSlots[tsiterator];


        if(row==1){
            let head = header.insertCell(-1);
            var html = weekdays[d] + "<br>" + myslot.slotDate.month + "/" + myslot.slotDate.day;
            head.innerHTML = html;
        }

        if((myslot.meetingName == " ")&&(myslot.isOpen)){
          let freebtn = document.createElement("BUTTON");
          freebtn.innerText = "Schedule Mtng";
          freebtn.addEventListener('click', function(){promptMeetingName(myslot)});
          thisSlot.appendChild(freebtn);
        }else if(myslot.meetingName != " "){
          let mtng = document.createElement("P");
          mtng.innerText = myslot.meetingName;
          let cancelbtn = document.createElement("BUTTON");
          cancelbtn.innerText = "Cancel Mtng";
          cancelbtn.addEventListener('click',function(){cancelMtng(myslot)});
          thisSlot.appendChild(mtng);
          thisSlot.appendChild(cancelbtn);
        }

        tsiterator++;
      }
    }
    //if not already visible, make visible
    document.getElementById("calendarwindow").style.visibility = 'visible';
  }


/********* Create A Meeting **********/
function promptMeetingName(ts){
  currts = ts;
  let label = document.getElementById("mtnglabel");
  label.innerHTML="<b>Name for Meeting on " + currts.slotDate.month + "/ " + currts.slotDate.day + " at " + currts.startTime.hour + ":" + currts.startTime.minute + ": <b>";
  let prompt = document.getElementById('mtngPrompt');
  prompt.style.display='block';
}

function createMtng(name){
  if ((name == "") || (name == " ")) {
    alert("You must enter a valid name");
  }  else{
    let data = {};
    data["requestSchedID"] = String(currSchedule.id);
    data["requestWeekStart"] = String(currSchedule.startDate);
    data["requestTSId"] = String(currts.id);
    data["requestMtngName"] = String(name);
    let createmtng_url = base_url + "/createmeeting";
    sendData(data,createmtng_url,processScheduleMtng);
  }
}

function cancelMtng(ts){
  currts = ts;
  document.getElementById("editMtngPrompt").style.display = 'block';
}

function checkMeetingAuthorization(){
  let enteredCode = document.getElementById("secretCode").value;
  if(enteredCode == currts.secretCode){
    document.getElementById("editMtngPrompt").style.display = 'none';
    alert("Successfully Logged in");
  }else{
    alert("Invalid Code");
    return;
  }
  alert("Deleting Meeting on " + currts.slotDate.month + "/ " + currts.slotDate.day + " at " + currts.startTime.hour + ":" + currts.startTime.minute + ": <b>");
  let data = {};
  data["requestSchedID"] = String(currSchedule.id);
  data["requestWeekStart"] = String(currSchedule.startDate);
  data["requestTSId"] = String(currts.id);
  let deletemtng_url = base_url + "/cancelmeeting";
  sendData(data,deletemtng_url,processSchedule);
}

function openSchedPrompt(e){
  document.getElementById("schedprompt").style.display='block';
  return false;
}
function openSchedule(){
  let enteredID = document.getElementById("schedid").value;
  orgCredentials = "";
  if((enteredID == " ") || (enteredID == "")){
    alert("You Must enter a valid id");
  }else{
    let data = {};
    data["requestSchedID"] = String(enteredID);
    data["requestWeekStart"] = "";
    let openschedule_url = base_url + "/getschedule";
    sendData(data,openschedule_url,processSchedule);
    document.getElementById("schedprompt").style.display ='none';
  }
}

function filter(){
  let day = document.getElementById("dayofweek").value;
  let month = document.getElementById("selectmonth").value;
  let year = document.getElementById("selectyear").value;
  let date = document.getElementById("selectdayofmonth").value;
  let time = document.getElementById("filterTime").value;
  let data = {};
  data["requestSchedID"] = String(currSchedule.id);
  data["requestWeekStart"] = String(currSchedule.startDate);
  data["requestWeekday"] = String(day);
  data["requestMonth"] = String(month);
  data["requestYear"] = String(year);
  data["requestDate"] = String(date);
  data["requestTime"] = String(time);
  let filter_url = base_url + "/filterschedule";
  sendData(data,filter_url,processSchedule);
}


function getNextWeek(){
  let data = {};
  data["requestSchedID"] = String(currSchedule.id);
  data["requestWeekStart"] = String(currSchedule.startDate);
  let next_url = base_url + "/getnextweek";
  sendData(data,next_url,processSchedule);
}

function getPreviousWeek(){
  let data = {};
  data["requestSchedID"] = String(currSchedule.id);
  data["requestWeekStart"] = String(currSchedule.startDate);
  let previous_url = base_url + "/getpreviousweek";
  sendData(data,previous_url,processSchedule);
}
/*********************** SUBMIT DATA TO JAVA *********************************/
function sendData(data,url,callback){
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
      callback(xhr.responseText);
    } else {
      callback(xhr.responseText);
    }
  };
}

function processSchedule(xhrResult){
	console.log("result:" + xhrResult);
	let js = JSON.parse(xhrResult);

  //add if schedule couldnt be opened, display "could not find schedule"
  if((js["response"]=="Successfully retrieved schedule")|| (js["response"]=="Successfully created schedule")){
    currSchedule.startDate = js["responseStartDateOfWeek"];
    currSchedule.startTime = js["responseStartTime"];
    currSchedule.id = js["responseID"];
    currSchedule.slotDuration = js["responseSlotDuration"];
    currSchedule.orgCode = js["responseSecretCode"];
    currSchedule.numSlotsPerDay = js["responseNumSlotsDay"];
    currSchedule.timeSlots = js["responseWeeklyTimeSlots"];
    currSchedule.fullStartDate = js["responseScheduleStartDate"];
    currSchedule.fullEndDate = js["responseScheduleEndDate"];
    generateCalendar();
  }else{
    alert(js["response"]);
  }
}


function processScheduleMtng(xhrResult){
	console.log("result:" + xhrResult);
	let js = JSON.parse(xhrResult);

  //add if schedule couldnt be opened, display "could not find schedule"
  if(js["response"]=="Successfully retrieved schedule"){
    currSchedule.startDate = js["responseStartDateOfWeek"];
    currSchedule.startTime = js["responseStartTime"];
    currSchedule.id = js["responseID"];
    currSchedule.slotDuration = js["responseSlotDuration"];
    currSchedule.orgCode = js["responseSecretCode"];
    currSchedule.numSlotsPerDay = js["responseNumSlotsDay"];
    currSchedule.timeSlots = js["responseWeeklyTimeSlots"];
    currSchedule.fullStartDate = js["responseScheduleStartDate"];
    currSchedule.fullEndDate = js["responseScheduleEndDate"];
    var this_slot;
    for(let i=0; i<currSchedule.timeSlots.length;i++){
      this_slot = currSchedule.timeSlots[i];
      if(this_slot.id == currts.id){
        break;
      }
    }
    document.getElementById("mtngcode").innerHTML = this_slot.secretCode;
    document.getElementById("mtngIDPrompt").style.display='block';
    generateCalendar();
  }else{
    alert(js["response"]);
  }
}
