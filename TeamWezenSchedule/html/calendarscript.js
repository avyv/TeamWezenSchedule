//var schedule_url = "https://b3ivwb09fg.execute-api.us-east-1.amazonaws.com/Alpha";
var base_url = "https://xdk3131931.execute-api.us-east-2.amazonaws.com/Alpha";
var schedule_url = base_url + "/schedule";
var curr_day = "1";
var curr_year = "2018";
var available_dates = [2,4,6];
var meetings = [{name:"m1",date:1},{name:"m2",date:5}];
var months = [{name: "none",length:0,startsOn:"none"},
              {name: "January",length:31,startsOn:"Thursday"},
              {name: "February",length:28,startsOn:"Sunday"},
              {name: "March",length:31,startsOn:"Sunday"},
              {name: "April",length:30,startsOn:"Wednesday"},
              {name: "May",length:31,startsOn:"Friday"},
              {name: "June",length:30,startsOn:"Monday"},
              {name: "July",length:31,startsOn:"Wednesday"},
              {name: "August",length:31,startsOn:"Sunday"},
              {name: "September",length:30,startsOn:"Tuesday"},
              {name: "October",length:31,startsOn:"Thursday"},
              {name: "November",length:30,startsOn:"Sunday"},
              {name: "December",length:31,startsOn:"Tuesday"}]
var curr_month = months[12];

window.onload = generateCalendar;

  function updateDay(){
    curr_day = document.getElementById("selectDate").value;
  }
  function updateMonth(){
    if (document.getElementById("selectmonth").value == "nomonth"){
      curr_month = months[12];
    }else {
      curr_month = document.getElementById("selectmonth").value;
    }
    updateDisplay();
  }
  function updateYear(){
    curr_year = document.getElementById("selectyear").value;
    updateDisplay();
  }

  function updateDisplay(){
    document.getElementById("showyear").innerHTML = curr_year;
    document.getElementById("displaymonth").innerHTML = curr_month.name;
  }

  function generateCalendar(){
    document.getElementById("daysview").innerHTML = "";
    var view = document.getElementById("viewselect").value;
    if(view == "week"){weekView();}
    else{monthView();}
  }
  function makeHeader(arr){
    let header = document.createElement("tr");
    document.getElementById("daysview").appendChild(header);
    for(let j=0;j<arr.length;j++){
      let head = document.createElement("td");
      html =  "<b>" + arr[j];
      head.innerHTML = html;
      header.appendChild(head);
    }
  }

function checkIncludes(arr, obj){
  for(let i=0; i< arr.length; i++){
    if(arr[i]==obj){return true;}
  }
  return false;
}

function checkForMtng(day){
  for(let i=0; i<meetings.length;i++){
    if(meetings[i].date == day){
      return true;
    }
  }
  return false;
}
function createMtng(n,d){
  mtng = {name: n, date: d};
  meetings.push(mtng);
  generateCalendar();
}
function deleteMtng(d){
  for(let i=0; i<meetings.length;i++){
    if(meetings[i].date == d){
      meetings.splice(i,1);
      alert(meetings);
      generateCalendar();
    }
  }
}
  function weekView(){
    let headervals = ["Time","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"];
    makeHeader(headervals);
    let currWeek = document.createElement("tr");
    document.getElementById("daysview").appendChild(currWeek);
    let emptytime = document.createElement("td");
    currWeek.appendChild(emptytime);

    let weekstart = 1;
    let weekend = weekstart + 7;
    let index = 0;
    for (let i = weekstart; i < weekend; i++){
          let day = document.createElement("td");
          day.className = "day";
          day.innerHTML = i + "<br>";
          if(checkForMtng(i)){
            var mtng = document.createElement("P");
            for(let j=0;j<meetings.length;j++){
              if(meetings[j].date == i){
                mtng.innerHTML = meetings[j].name;
                break;
              }
            }
            day.appendChild(mtng);
            index++;
          } else if (checkIncludes(available_dates,i)) {
            let free = document.createElement("button");
            free.innerText = "Free";
            free.value = i;
            free.addEventListener('click', function(){handleFreeButton(free)});
            day.appendChild(free);
          }

          currWeek.appendChild(day);//only the first row
      }
  }

  function monthView(){
    let monthlength = 31;
    let daysview = document.getElementById("daysview");

    let headervals = ["Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"];
    makeHeader(headervals);
    let currWeek = document.createElement("tr");
    daysview.appendChild(currWeek);

    for (let i = 1; i <= monthlength; i++){
          let day = document.createElement("td");
          day.className = "day";
          day.innerHTML = i + "<br>";

          if (i == 3 || i == 5) {
              let free = document.createElement("button");
              free.innerText = "Free";
              free.value = i;
              free.addEventListener('click', function(){handleFreeButton(free)});
              day.appendChild(free);
          }

          if(i%7 == 0){
            currWeek.appendChild(day)
            currWeek = document.createElement("tr");
            daysview.appendChild(currWeek);
          } else {currWeek.appendChild(day);}
      }
  }

  function handleSubmit(e){
    if(document.getElementById("mtngName").value == ""){
      alert("You must enter a Meeting Name");
    }else{
      document.getElementById("mtngPrompt").style.display = 'none';
      setName(e);
    }
    return false;
  }

function handleFreeButton(obj){
    curr_day = obj.value;
    promptMeetingName();
}

function promptMeetingName(){
  let label = document.getElementById("mtnglabel");
  label.innerHTML="<b>Name for Meeting on " + curr_day + ", " + curr_month + " " + curr_year + ": <b>";
  let prompt = document.getElementById('mtngPrompt');
  prompt.style.display='block';
}

function processMeetingNameResponse(name, xhrResult) {
	console.log("result:" + xhrResult);
	let js = JSON.parse(xhrResult);

	let responseName = js["responseName"];
	let responseDate = js["responseDate"];

	alert("received name: " + responseName + " date: " + responseDate);
}

function setName(nameobj){
  let name = document.getElementById("mtngName").value;
  createMtng(name,curr_day);
  let data = {};
  data["requestName"] = name;
  data["requestDate"] = curr_month + " " + curr_day + ", " + curr_year;

  let js = JSON.stringify(data);
  console.log("JS:" + js);
  let xhr = new XMLHttpRequest();
  xhr.open("POST", schedule_url, true);

  xhr.send(js);

  xhr.onloadend = function () {
  	console.log(xhr);
  	console.log(xhr.request);
  	if(xhr.readyState == XMLHttpRequest.DONE) {
  		console.log("XHR:" + xhr.responseText);
  		processMeetingNameResponse(name, xhr.responseText);
  	} else {
  		processMeetingNameResponse(name, "N/A");
  	}
  };

}
