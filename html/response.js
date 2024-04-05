function updatePlayerList(playerList){
    playerListElement = document.getElementById("playerList");
    playerListElement.innerHTML = "";
    for(let i = 0; i < playerList.length; i++){
        playerListElement.innerHTML +=
        `<p>${playerList[i]}</p>`;
    }
}

function updateLobbies(lobbyLists){
    lobbies = [document.getElementById("lobbyOnePlayers"), document.getElementById("lobbyTwoPlayers"), document.getElementById("lobbyThreePlayers")]

    for(let i = 0; i < lobbyLists.length; i++){
        lobbies[i].innerHTML = "";
        for(let j = 0; j < lobbyLists[i].length; j++){
            lobbies[i].innerHTML += `<p>${lobbyLists[i][j]}</p>`;
        }
    }
}


function updateLeaderboard(leaderBoardInfo){
    let leaderBoardElement = document.getElementById("leaderboard");
    leaderBoardElement.innerHTML = "";
    for(let i = 0; i < leaderBoardInfo["usernames"].length; i++){
        leaderBoardElement.innerHTML += `<p>${leaderBoardInfo["usernames"][i]}: ${leaderBoardInfo["scores"][i]}</p>`;
    }
}

function updateChat(){

}
