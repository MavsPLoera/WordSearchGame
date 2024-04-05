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
    console.log("lobbyId", lobbyId);
    connection.send(JSON.stringify({
        "type": "JoinLobbyRequest",
        "eventData": {
            "lobby": lobbyId
        }
    }));
}