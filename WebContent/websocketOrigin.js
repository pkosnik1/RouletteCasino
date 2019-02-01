/**
 * 
 */
window.onload = init;
var socket = new WebSocket("ws://localhost:8080/RouletteCasino/actions");
socket.onmessage = onMessage;

function onMessage(event) {
	var device = JSON.parse(event.data);
	if (device.action === "add") {
		printDeviceElement(device);
	}
	if (device.action === "remove") {
		document.getElementById(device.id).remove();
		// device.parentNode.removeChild(device);
	}
	if (device.action === "toggle") {
		var node = document.getElementById(device.id);
		var statusText = node.children[2];
		if (device.status === "On") {
			statusText.innerHTML = "Status: " + device.status
					+ " (<a href=\"#\" OnClick=toggleDevice(" + device.id
					+ ")>Turn off</a>)";
		} else if (device.status === "Off") {
			statusText.innerHTML = "Status: " + device.status
					+ " (<a href=\"#\" OnClick=toggleDevice(" + device.id
					+ ")>Turn on</a>)";
		}
	}
	if (device.action === "addPlayer") {
		printPlayerElement(device);
	}
	if (device.action === "playerList") {
		printPlayersElement(device);
	}
	if (device.action === "betList") {
		printPlayerBets(device);
	}
	if (device.action === "stateInfo") {
		printStateInfo(device);
	}
}

function addDevice(name, type, description) {
	var DeviceAction = {
		action : "add",
		name : name,
		type : type,
		description : description
	};
	socket.send(JSON.stringify(DeviceAction));
}

function addPlayer(nick, budget) {
	var PlayerAction = {
		action : "addPlayer",
		nick : nick,
		budget : budget
	};
	socket.send(JSON.stringify(PlayerAction));
}

function addBet(type, fields, value) {
	var BetAction = {
		action : "addBet",
		type : type,
		fields : fields,
		value : value
	};
	socket.send(JSON.stringify(BetAction));
}

function removeDevice(element) {
	var id = element;
	var DeviceAction = {
		action : "remove",
		id : id
	};
	socket.send(JSON.stringify(DeviceAction));
}

function toggleDevice(element) {
	var id = element;
	var DeviceAction = {
		action : "toggle",
		id : id
	};
	socket.send(JSON.stringify(DeviceAction));
}

function printStateInfo(state) {
	var content = document.getElementById("state");
	content.innerHTML = "";

	var stateDiv = document.createElement("div");
	stateDiv.setAttribute("id", "stateInfo");
	content.appendChild(stateDiv);

	var ballState = document.createElement("span");
	ballState.setAttribute("class", "ballState");
	ballState.innerHTML = state.ballState;
	stateDiv.appendChild(ballState);

	var ballNumber = document.createElement("span");
	ballNumber.innerHTML = "<b> (</b> " + state.ballNumber + "<b> )</b> ";
	stateDiv.appendChild(ballNumber);
	
	var ballTime = document.createElement("span");
	ballTime.setAttribute("class", "ballTime");
	ballTime.innerHTML = state.ballTime;
	stateDiv.appendChild(ballTime);
}
function printPlayerElement(player) {
	var content = document.getElementById("players");

	var playerDiv = document.createElement("div");
	playerDiv.setAttribute("id", player.nick);
	content.appendChild(playerDiv);

	var playerNick = document.createElement("span");
	playerNick.setAttribute("class", "playerNick");
	playerNick.innerHTML = player.nick;
	playerDiv.appendChild(playerNick);

	var playerBudget = document.createElement("span");
	playerBudget.innerHTML = "<b> (</b> " + player.budget + "<b> )</b> ";
	playerDiv.appendChild(playerBudget);
}

function printPlayersElement(devices) {
	document.getElementById("players").innerHTML = ""; // clear content
	for (var i = 0; i < devices.players.length; i++) {
		var obj = devices.players[i];
		printPlayerElement(obj);
	}
}

function printBetElement(bet) {
	var content = document.getElementById("bets");

	var betDiv = document.createElement("div");
	betDiv.setAttribute("id", bet.type);
	content.appendChild(betDiv);

	var betType = document.createElement("span");
	betType.setAttribute("class", "betType");
	betType.innerHTML = bet.type;
	betDiv.appendChild(betType);

	var betValue = document.createElement("span");
	betValue.innerHTML = "<b> (</b> " + bet.value + "<b> ) </b> ";
	betDiv.appendChild(betValue);

	for (var i = 0; i < bet.fields.length; i++) {
		var betField = document.createElement("span");
		var obj = bet.fields[i];
		betField.innerHTML = obj.field + "#";
		betDiv.appendChild(betField);
	}
}

function printPlayerBets(devices) {
	document.getElementById("bets").innerHTML = ""; // clear content
	for (var i = 0; i < devices.bets.length; i++) {
		var obj = devices.bets[i];
		printBetElement(obj);
	}
}

function printDeviceElement(device) {
	var content = document.getElementById("content");

	var deviceDiv = document.createElement("div");
	deviceDiv.setAttribute("id", device.id);
	deviceDiv.setAttribute("class", "device " + device.type);
	content.appendChild(deviceDiv);

	var deviceName = document.createElement("span");
	deviceName.setAttribute("class", "deviceName");
	deviceName.innerHTML = device.name;
	deviceDiv.appendChild(deviceName);

	var deviceType = document.createElement("span");
	deviceType.innerHTML = "<b>Type:</b> " + device.type;
	deviceDiv.appendChild(deviceType);

	var deviceStatus = document.createElement("span");
	if (device.status === "On") {
		deviceStatus.innerHTML = "<b>Status:</b> " + device.status
				+ " (<a href=\"#\" OnClick=toggleDevice(" + device.id
				+ ")>Turn off</a>)";
	} else if (device.status === "Off") {
		deviceStatus.innerHTML = "<b>Status:</b> " + device.status
				+ " (<a href=\"#\" OnClick=toggleDevice(" + device.id
				+ ")>Turn on</a>)";
		// deviceDiv.setAttribute("class", "device off");
	}
	deviceDiv.appendChild(deviceStatus);

	var deviceDescription = document.createElement("span");
	deviceDescription.innerHTML = "<b>Comments:</b> " + device.description;
	deviceDiv.appendChild(deviceDescription);

	var removeDevice = document.createElement("span");
	removeDevice.setAttribute("class", "removeDevice");
	removeDevice.innerHTML = "<a href=\"#\" OnClick=removeDevice(" + device.id
			+ ")>Remove device</a>";
	deviceDiv.appendChild(removeDevice);
}

function showForm() {
	document.getElementById("addDeviceForm").style.display = '';
}

function hideForm() {
	document.getElementById("addDeviceForm").style.display = "none";
}

function formSubmit() {
	var form = document.getElementById("addDeviceForm");
	var name = form.elements["device_name"].value;
	var type = form.elements["device_type"].value;
	var description = form.elements["device_description"].value;
	hideForm();
	document.getElementById("addDeviceForm").reset();
	addDevice(name, type, description);
}

function formPlayerSubmit() {
	var form = document.getElementById("addPlayerForm");
	var nick = form.elements["player_nick"].value;
	var budget = form.elements["player_budget"].value;
	hideForm();
	document.getElementById("addDeviceForm").reset();
	addPlayer(nick, budget);
}

function formBetSubmit() {
	var form = document.getElementById("addBetForm");
	var type = form.elements["bet_type"].value;
	var fields = form.elements["bet_fields"].value;
	var value = form.elements["bet_value"].value;
	hideForm();
	document.getElementById("addBetForm").reset();
	addBet(type, fields, value);
}

function init() {
	hideForm();
}