/**
 * 
 */
window.onload = init;
// var socket = new WebSocket("ws://192.168.137.1:8080/RouletteCasino/actions");
//var socket = new WebSocket("ws://localhost:8080/RouletteCasino/actions");
var socket = new WebSocket("ws://" + location.host + "/RouletteCasino/actions");

socket.onmessage = onMessage;

function onMessage(event) {
	var msg = JSON.parse(event.data);
	/*if (device.action === "toggle") {
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
	}*/
	if (msg.action === "addPlayer") {
		printPlayerElement(msg);
	}
	if (msg.action === "playerList") {
		printPlayersElement(msg);
	}
	if (msg.action === "betList") {
		printPlayerBets(msg);
	}
	if (msg.action === "stateInfo") {
		printStateInfo(msg);
	}
	if (msg.action === "addMessage") {
		printMessage(msg);
	}
}

function spinWheel() {
	var el = document.getElementById('roulette_image');
	el.style.animation = 'none';
	el.offsetHeight; /* trigger reflow */
	el.style.animation = null;
}

function addPlayer(nick, budget, key) {
	var PlayerAction = {
		action : "addPlayer",
		nick : nick,
		budget : budget,
		key : key
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

function printMessage(msg) {
	var content = document.getElementById("message");
	content.innerHTML = "";

	var msgDiv = document.createElement("div");
	msgDiv.setAttribute("id", "msg");
	content.appendChild(msgDiv);

	var value = document.createElement("span");
	value.setAttribute("class", "message");
	value.innerHTML = msg.message;
	msgDiv.appendChild(value);
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
	// spin the wheel
	if (state.ballState == 'ROLL')
		if (state.ballTime == '1')
			spinWheel();
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

/*function printDeviceElement(device) {
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
}*/

function showForm() {
	 document.getElementById("addBetForm").style.display = '';
}

function hideForm() {
	 document.getElementById("addBetForm").style.display = "none";
}

function formPlayerSubmit() {
	var form = document.getElementById("addPlayerForm");
	var nick = form.elements["player_nick"].value;
	var budget = form.elements["player_budget"].value;
	var key = form.elements["session_key"].value;
	hideForm();
	// document.getElementById("addDeviceForm").reset();
	addPlayer(nick, budget,key);
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
    document.getElementById("session_key").value = makeid();
}

function makeid() {
	var text = "";
	var possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

	for (var i = 0; i < 5; i++)
		text += possible.charAt(Math.floor(Math.random() * possible.length));

	return text;
}