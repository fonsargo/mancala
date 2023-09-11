(function() {
    var createButton = document.getElementById("createButton");
    var connectButton = document.getElementById("connectButton");

    createButton.addEventListener("click", function(event){
        var nameInput = document.getElementById("nameInput");
        var name = nameInput.value;
        if (name && name.trim()) {
            $.post("/game/create", {name: name})
                .done(function (game) {
                    setGameId(createButton, connectButton, game);
                    alert("Successfully created game with id: " + game.id + ". Send this ID to your opponent!");
                })
                .fail(function (error) {
                    console.log(error);
                    alert(error.responseJSON.message);
                })
        } else {
            alert("Please, enter your name!");
        }
    });

    connectButton.addEventListener("click", function(event){
        var nameInput = document.getElementById("nameInput");
        var gameIdInput = document.getElementById("gameIdInput");
        var name = nameInput.value;
        var gameId = gameIdInput.value;
        if (name && name.trim() && gameId) {
            $.post("/game/" + gameId + "/connect" , {name: name})
                .done(function (game) {
                    setGameId(createButton, connectButton, game);
                    updateBoard(game.board.secondPlayerPits, game.board.secondPlayerLargePit, game.board.firstPlayerPits, game.board.firstPlayerLargePit);
                    alert("Successfully connected to game with id: " + game.id + "!");
                })
                .fail(function (error) {
                    console.log(error);
                    alert(error.responseJSON.message);
                })
        } else {
            alert("Please, enter your name and game ID!");
        }
    });
})();

function updateBoard(myPits, myLargePit, opponentPits, opponentLargePit) {
    var i;
    var myPitsDivs = document.getElementById("firstPlayerDiv").getElementsByTagName('div');
    for (i = 0; i < myPitsDivs.length; i++) {
        myPitsDivs[i].innerHTML = myPits[i];
    }
    document.getElementById("firstPlayerLargeDiv").innerHTML = myLargePit;

    var opponentPitsDivs = document.getElementById("secondPlayerDiv").getElementsByTagName('div');
    for (i = 0; i < opponentPitsDivs.length; i++) {
        opponentPitsDivs[i].innerHTML = opponentPits[i];
    }
    document.getElementById("secondPlayerLargeDiv").innerHTML = opponentLargePit;
}

function setGameId(createButton, connectButton, game) {
    createButton.disabled = true;
    connectButton.disabled = true;
    var header = document.getElementById("gameIdHeader");
    header.innerHTML = "Game #" + game.id;
}