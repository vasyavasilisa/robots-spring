/**
 * 
 */

'use strict'

var maxLogSize = 100;
var log = [];
var lastIndex = 0;
var url = "http://localhost:8080/";

document.addEventListener("DOMContentLoaded", initialization);

function initialization() {
	loggerInitialization();
	sendRobotInitialization();
	sendTaskInitialization();
	timeManagerInitialization();
	thresholdInitialization();
	intervalInitialization();
}

function loggerInitialization() {
	logTailRequest();
	setInterval(function() {
		doLog();
	}, 500);

}

function doLog() {
	var xhr = new XMLHttpRequest();
	xhr.open('GET', url + 'main/log_from_id?id=' + (lastIndex+1), true);
	xhr.send();
	xhr.onreadystatechange = function() {
		if (this.readyState != 4)
			return;
		if (this.status == 200) {
			var data = JSON.parse(this.responseText);
			for (var item in data) {
				if(data[item].id>lastIndex){
					lastIndex = data[item].id
					appendLog(data[item].message);
				}	
			}
		}
	};
	setLog();
}

function logTailRequest() {
	var xhr = new XMLHttpRequest();
	xhr.open('GET', url + 'main/log_tail?amount=' + maxLogSize, true);
	xhr.send();
	xhr.onreadystatechange = function() {
		if (this.readyState != 4)
			return;
		if (this.status == 200) {
			var data = JSON.parse(this.responseText);
			for ( var item in data) {
				if (data[item].id > lastIndex)
					lastIndex = data[item].id;
				appendLog(data[item].message)
			}
		}
	};
	setLog();
}

function appendLog(message) {
	if(log.push(message)>maxLogSize)log.shift();
}

function setLog() {
	var line = log.join("</br>");
	var block = document.getElementById("log");
	block.innerHTML = line
	block.scrollTop = block.scrollHeight;
}

function sendRobotInitialization(){
	$('#name').val("");
	$('#robot_type').val("")
	$('#send_robot').click(sendRobot)
}

function sendRobot(){
	var name = document.getElementById('name').value;
	var type = document.getElementById('robot_type').value;
	if(name.length==0)return;
	if(type.length==0)return;
	var json = {};
	json.name = name;
	json.type = type;
	var xhr = new XMLHttpRequest();
	xhr.open('POST', url + "main/newrobot", true);
	xhr.send(JSON.stringify(json));
	$('#name').val("");
	$('#robot_type').val("")
}

function sendTaskInitialization(){
	$('#robot_name').val("");
	$('#task_subject').val("");
	$('#send_task').click(sendTask)
}

function sendTask(){
	var robot = document.getElementById('robot_name').value;
	var task = document.getElementById('task_subject').value;
	if(task.length==0)return;
	var json = {}
	json.type = task;
	if(robot.length==0){
		var xhr = new XMLHttpRequest();
		xhr.open('POST', url + "main/newtask", true);
		xhr.send(JSON.stringify(json));
	}else{
		var xhr = new XMLHttpRequest();
		xhr.open('POST', url + "main/taskfor?name="+robot, true);
		xhr.send(JSON.stringify(json));
	}
	$('#robot_name').val("");
	$('#task_subject').val("");
}

function timeManagerInitialization(){
	$('#task_time').val("");
	$('#set_time').click(sendTimeManageRequest)
}

function sendTimeManageRequest(){
	var time  = document.getElementById('task_time').value;
	if(isNumeric(time)&&time>1&&time<3601){
		var json = {};
		json.value = time*1000;
		var xhr = new XMLHttpRequest();
		xhr.open('PUT', url + "main/taskmaxtime", true);
		xhr.send(JSON.stringify(json));
		$('#task_time').val("");
	}else $('#task_time').val("???");
}

function thresholdInitialization(){
	$('#threshold').val("");
	$('#set_threshold').click(thresholdManagerRequest)
}

function thresholdManagerRequest(){
	var threshold  = document.getElementById('threshold').value;
	if(isNumeric(threshold)&&threshold>1&&threshold<101){
		var json = {};
		json.value = threshold*1;
		var xhr = new XMLHttpRequest();
		xhr.open('PUT', url + "main/taskthreshold", true);
		xhr.send(JSON.stringify(json));
		$('#threshold').val("");
	}else $('#threshold').val("???");
}

function intervalInitialization(){
	$('#interval').val("");
	$('#set_interval').click(intervalManagerRequest)
}

function intervalManagerRequest(){
	var interval  = document.getElementById('interval').value;
	if(isNumeric(interval)&&interval>1&&interval<3601){
		var json = {};
		json.value = interval*1000;
		var xhr = new XMLHttpRequest();
		xhr.open('PUT', url + "main/taskinterval", true);
		xhr.send(JSON.stringify(json));
		$('#interval').val("");
	}else $('#interval').val("???");
}
function isNumeric(n) {
	   return !isNaN(parseFloat(n)) && isFinite(n);
	}