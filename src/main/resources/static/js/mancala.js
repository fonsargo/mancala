var isFirstPlayer;
var gameId;

(function() {
    var createButton = document.getElementById("createButton");
    var connectButton = document.getElementById("connectButton");

    createButton.addEventListener("click", function(event){
        var nameInput = document.getElementById("nameInput");
        var name = nameInput.value;
        if (name && name.trim()) {
            $.post("/game/create", {name: name})
                .done(function (game) {
                    isFirstPlayer = true;
                    startGame(createButton, connectButton, game);
                    setMessage("Successfully created game with id: " + game.id + ". Send this ID to your opponent and wait...", true);
                })
                .fail(function (error) {
                    console.log(error);
                    alert(error.responseJSON.message);
                });
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
                    isFirstPlayer = false;
                    updateBoard(game.board.secondPlayerPits, game.board.secondPlayerLargePit, game.board.firstPlayerPits, game.board.firstPlayerLargePit);
                    startGame(createButton, connectButton, game);
                    setMessage("Successfully connected to game! Waiting for your opponent's turn...", true);
                })
                .fail(function (error) {
                    console.log(error);
                    alert(error.responseJSON.message);
                });
        } else {
            alert("Please, enter your name and game ID!");
        }
    });

    var i;
    var myPitsDivs = document.getElementById("firstPlayerDiv").getElementsByTagName('div');
    for (i = 0; i < myPitsDivs.length; i++) {
        myPitsDivs[i].addEventListener("click", makeMove.bind(null, i));
    }
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
        opponentPitsDivs[i].innerHTML = opponentPits[opponentPits.length - i - 1];
    }
    document.getElementById("secondPlayerLargeDiv").innerHTML = opponentLargePit;
}

function makeMove(i) {
    changePitsStatus();
    $.post("/game/" + gameId + "/makeMove", {pitIndex: i})
        .done(function (result) {
            //TODO
        })
        .fail(function (error) {
            console.log(error);
            alert(error.responseJSON.message);
        });
}

function changePitsStatus() {
    var firstPlayerDiv = document.getElementById("firstPlayerDiv");
    firstPlayerDiv.classList.toggle("player-one");
    firstPlayerDiv.classList.toggle("player-one-enabled");
}

function startGame(createButton, connectButton, game) {
    createButton.disabled = true;
    connectButton.disabled = true;
    var header = document.getElementById("gameIdHeader");
    header.innerHTML = "Game #" + game.id;
    gameId = game.id;
    connectWebSocket();
}

function setMessage(message, isWaiting) {
    var messageElement = document.getElementById("message");
    messageElement.innerHTML = message;
    var progressBar = document.getElementById("progress");
    if (isWaiting) {
        progressBar.style.visibility = 'visible';
    } else {
        progressBar.style.visibility = 'hidden';
    }
}

function connectWebSocket() {
    var socket = new SockJS('/websocket');
    var stompClient = Stomp.over(socket);
    stompClient.connect({}, stompSuccessCallback.bind(null, stompClient), stompFailureCallback);
}

var stompSuccessCallback = function (stompClient, frame) {
    console.log('STOMP: Connected: ' + frame);
    stompClient.subscribe('/topic/game/' + gameId, function (result) {
        var game = JSON.parse(result.body);
        if (isFirstPlayer) {
            updateBoard(game.board.firstPlayerPits, game.board.firstPlayerLargePit, game.board.secondPlayerPits, game.board.secondPlayerLargePit);
        } else {
            updateBoard(game.board.secondPlayerPits, game.board.secondPlayerLargePit, game.board.firstPlayerPits, game.board.firstPlayerLargePit);
        }
        var winner = game.board.winner;
        if (winner) {
            if (isFirstPlayer && winner === 'FIRST_PLAYER' || !isFirstPlayer && winner === 'SECOND_PLAYER') {
                setMessage("Congratulations, you win!")
            } else if (winner === 'DRAW') {
                setMessage("Draw result!")
            } else {
                setMessage("Unfortunately, you lose!")
            }
            return;
        }

        var playerTurn = game.board.playerTurn;
        if (isFirstPlayer && playerTurn === 'FIRST_PLAYER' || !isFirstPlayer && playerTurn !== 'FIRST_PLAYER') {
            setMessage("Now it's your turn!");
            changePitsStatus();
        } else {
            setMessage("Waiting for your opponent's turn...", true)
        }
    });
}

var stompFailureCallback = function (error) {
    console.log('STOMP: ' + error);
    setTimeout(connectWebSocket, 10000);
    console.log('STOMP: Reconnecting in 10 seconds');
};