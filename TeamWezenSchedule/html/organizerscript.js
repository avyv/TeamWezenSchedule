function handleSubmit(){
  document.getElementById("orgtools").style.display = 'none';
  createSchedule();
  return false;  
}
function createSchedule(){
  // alert("Schedule Created");
  let startDay = document.getElementById("setstartday").value;
  let endDay = document.getElementById("setendday").value;
  let timeslot = document.getElementById("settimeslots").value;
  let dailyStartTime = document.getElementById("settimestart").value;
  let dailyEndTime = document.getElementById("settimeend").value;
  alert("end time: "+dailyEndTime);
}
