function addUser(event) {
    event.preventDefault(); 

    let username = document.getElementById("username").value;
    // Dont send a request if the value is empty

    connection.send(JSON.stringify({
        "type": "UserLoginRequest",
        "eventData": {
            "username": username
        }
    }));
    return false;
}

function joinLobby(lobbyId){
    connection.send(JSON.stringify({
        "type": "JoinLobbyRequest",
        "eventData": {
            "lobby": lobbyId
        }
    }));
}

function selectGrid(x, y){
    console.log(x,y)
    connection.send(JSON.stringify({
        "type": "SelectGridRequest",
        "eventData": {
            "x": x,
            "y": y,
        }
    }));
}