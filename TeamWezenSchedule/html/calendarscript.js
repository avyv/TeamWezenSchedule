//var schedule_url = "https://b3ivwb09fg.execute-api.us-east-1.amazonaws.com/Alpha";
var base_url = "https://xdk3131931.execute-api.us-east-2.amazonaws.com/Alpha";
var schedule_url = base_url + "/schedule";
var curr_month = "December";
var curr_day = "1";
var curr_year = "2018";
var available_dates = [2,4,6];
var meetings = [];
window.onload = generateCalendar;

  function updateDay(){
    curr_day = document.getElementById("selectDate").value;
  }
  function updateMonth(){
    if (document.getElementById("selectmonth").value == "nomonth"){
      curr_month = "December"
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
    document.getElementById("displaymonth").innerHTML = curr_month;
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

  function weekView(){
    let headervals = ["Time","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"];
    makeHeader(headervals);
    let currWeek = document.createElement("tr");
    document.getElementById("daysview").appendChild(currWeek);

    let emptytime = document.createElement("td");
    currWeek.appendChild(emptytime);

    let weekstart = 1;
    let weekend = weekstart + 7;
    for (let i = weekstart; i < weekend; i++){
          let day = document.createElement("td");
          day.className = "day";
          day.innerHTML = i + "<br>";
          // let isopen = available_dates.includes(i);
          if (i==2) {
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


function handleFreeButton(obj){
  // alert(available_dates.includes(2));

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

	alert(responseName);
}

function setName(nameobj){
  let name = document.getElementById("mtngName").value;

  let data = {};
  data["responseName"] = name;
  data["responseDate"] = curr_month + " " + curr_day + ", " + curr_year;

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
