function changeGrid(){
    let gridDiv = document.getElementById("gameGrid");
    console.log("gridDiv: ", gridDiv);

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
changeGrid();

var serverUrl = "ws://0.tcp.ngrok.io:19539";
var connection = new WebSocket(serverUrl);

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
    
}


// function sendMessage() {
//     connection.send(JSON.stringify());
//     console.log(JSON.stringify())
// }

