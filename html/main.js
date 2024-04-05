var serverUrl = "ws://4.tcp.ngrok.io:12881";
var connection = new WebSocket(serverUrl);
showLoginPage();
connection.onopen = function (evt) {
    console.log("open");
}
connection.onclose = function (evt) {
    console.log("close");
}

connection.onmessage = function (evt) {
    var msg =  evt.data;
    console.log("Message received: " + msg);
    const obj = JSON.parse(msg);
    switch (obj.type) {
        case "LoginResponse":
            if (obj.eventData.loggedIn) {
                showgridPage();
                showLobbyPage();
            } else{
                let errorMessage = document.getElementById("loginError");
                errorMessage.innerHTML =
                `<p>Please enter a different username, the one you entered is already taken</p>`;
            }
            break;
        case "PlayerListResponse":
            updatePlayerList(obj.eventData.onlineUsers);
            break;
        case "LobbyUpdateResponse":
            updateLobbies(obj.eventData.lobbies);
            break;
        case "LeaderBoardResponse":
            updateLeaderboard(obj.eventData);
            break;
        case "StartGame":
            showGamePage();
            break;
        default:
            console.log("unknown message type");
            break;
    }
}

function showgridPage() {
    document.getElementById('loginForm').style.display = 'none';
    document.getElementById('mainWindow').style.display = 'block';
}

function showLoginPage() {
    document.getElementById('mainWindow').style.display = 'none';
}
function showLobbyPage() {
    document.getElementById('game').style.display = 'none';
    document.getElementById('lobby').style.display = 'block';
}
function showGamePage() {
    document.getElementById('lobby').style.display = 'none';
    document.getElementById('game').style.display = 'block';
}


function changeGrid(){
    console.log("gridDiv: ", gridDiv)

    for (let i = 0; i < 20; i++) {
        const row = document.createElement("div");
        row.classList.add("grid-row");
        gridDiv.appendChild(row);
        for (let j = 0; j < 20; j++) {
            row.innerHTML +=
            `<button type="button">N</button>`;
        }
    }
}
// showLoginPage();

let grid = [];
for (let i = 0; i < 20; i++) {
    grid[i] = [];
    for (let j = 0; j < 20; j++) {
        let gridElement = {
            letter: "X",
            selectedBy: [],
            foundBy: []
        };
        grid[i].push(gridElement);
    }
}
// changeGrid();




// function sendMessage() {
//     connection.send(JSON.stringify());
//     console.log(JSON.stringify())
// }

