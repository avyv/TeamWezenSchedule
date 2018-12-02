var base_url = "https://xdk3131931.execute-api.us-east-2.amazonaws.com/Alpha";

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

var dummySchedule = {startDate:"2018-12-03", startTime:"01:00:00", slotDuration:"01:00:00",id:1000,numSlotsPerDay:4,timeSlots:dummyTimeslots};
var currts = dummySchedule.timeSlots[0];
window.onload = initialize;

function initialize(){
  head = document.getElementById("head");
  openScheduleBtn = document.createElement("BUTTON");
  openScheduleBtn.innerText = "Open Schedule";
  openScheduleBtn.addEventListener('click', function(){document.getElementById("schedprompt").style.display='block';});
  head.appendChild(openScheduleBtn);
  generateCalendar();
}

/**********  Create Calendar Display **********/
function generateCalendar(){
    document.getElementById("daysview").innerHTML = "";

    var weekdays = ["Time","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"];
    let date = dummySchedule.startDate;
    let parseddate = date.split("-");
    let d = parseddate[2];
    let m = parseddate[1];
    let y = parseddate[0];//last 2 digits of year
    let calendar = document.getElementById("daysview");
    /********** header ************/
    let header = calendar.insertRow(0);
    header.id = "timelbl";

    for(let j=0;j<weekdays.length;j++){
      let head = header.insertCell(-1);
      if(j>0){
        var html = weekdays[j] + "<br>" + m + "/" + d ;
        d++;
      }else{var html = weekdays[j];}//dont put date on time label
      head.innerHTML = html;
    }
    /********* Calendar *********/
    let st = dummySchedule.startTime.split(":");
    let dur = dummySchedule.slotDuration.split(":");
    let timelabel = document.createElement("td");
    var tsiterator = 0;
    for(let row=1; row<=(dummySchedule.numSlotsPerDay); row++){
      /********** Time Slot Label **********/
       let hrs = parseInt(st[0]) + (parseInt(dur[0])*(row-1));
       let mins = parseInt(st[1]) + (parseInt(dur[1])*(row-1));
       if(mins<10){
         label = "<b>" + hrs + ":" + 0 + mins + "</b>";
       }else{label = "<b>" + hrs + ":" + mins + "</b>";}

      let tsrow = calendar.insertRow(-1);
      timeslot = tsrow.insertCell(0);
      timeslot.innerHTML = label;
      timeslot.id = "daylbl";
      /********** Time Slots **********/
      var text;
      for(let d=1;d<=7;d++){
        let thisSlot = tsrow.insertCell(-1);
        let myslot = dummySchedule.timeSlots[tsiterator];
        if((myslot.meeting.name == " ") && myslot.isOpen){
          let freebtn = document.createElement("BUTTON");
          freebtn.innerText = "Free";
          freebtn.addEventListener('click', function(){promptMeetingName(myslot)});
          thisSlot.appendChild(freebtn);
        }else{
          text = myslot.meeting.name;
          thisSlot.innerHTML = text;
        }
        tsiterator++;
      }
    }
  }
/********* Create A Meeting **********/
function createMtng(name){
  if ((name == "") || (name == " ")) {
    alert("You must enter a valid name");
  }  else{
      let data = {};
      data["requestTSId"] = currts.id;
      data["requestMtngName"] = name;
      let createmtng_url = base_url + "/createmtng";
      sendCreateMtng(data,createmtng_url);
  }
  generateCalendar();
}

function promptMeetingName(ts){
  currts = ts;
  let label = document.getElementById("mtnglabel");
  label.innerHTML="<b>Name for Meeting on " + currts.date + " at " + currts.startTime + ": <b>";
  let prompt = document.getElementById('mtngPrompt');
  prompt.style.display='block';
}

/*********************** Submit Data to Java*********************************/
function handleSubmitSchedId(e){
  let schedID = document.getElementById("schedid").value;
  if (schedID == "") {
    alert("You must enter a Schedule ID");
  }  else{
      let data = {};
      data["requestSchedID"] = schedID;
      let schedidurl = base_url + "/schedid";
      sendCalData(data,schedidurl);
  }
}
function handleSubmitMtngName(nameobj){
  let name = document.getElementById("mtngName").value;
  let data = {};
  data["requestName"] = name;
  data["requestDate"] = currts.date;
  data["requestTime"] = currts.startTime;
  let mtngnameurl = base_url + "/schedule";
  sendCalData(data,mtngnameurl,processMtngResponse);
  //close prompt
  document.getElementById('mtngPrompt').style.display = 'none';
}
function processMtngResponse(xhrResult) {
	console.log("result:" + xhrResult);
	let js = JSON.parse(xhrResult);
  let responseName = js["responseName"];
  let responseDate = js["responseDate"];
  alert("received name: " + responseName + " date: " + responseDate);
  generateCalendar();
}

function sendCalData(data,url,processcallback){
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
