(function() {
    var i;
    var myPitsDivs = document.getElementById("firstPlayerDiv").getElementsByTagName('div');
    for (i = 0; i < myPitsDivs.length; i++) {
        myPitsDivs[i].addEventListener("click", makeMove.bind(null, i));
    }

    var stompSuccessCallback = function (stompClient, frame) {
        console.log('STOMP: Connected: ' + frame);
        stompClient.subscribe('/topic/game/' + gameId, function (result) {
            var game = JSON.parse(result.body);
            updateGame(game);
        });
    }

    var stompFailureCallback = function (error) {
        console.log('STOMP: ' + error);
        setTimeout(connectWebSocket, 10000);
        console.log('STOMP: Reconnecting in 10 seconds');
    };

    connectWebSocket(stompSuccessCallback, stompFailureCallback);
})();

function updateGame(board, status) {
    if (status === "NEW" || !board) {
        setMessage("Game was successfully created! Send this link to your opponent and wait...", true);
        return;
    }

    if (isFirstPlayer) {
        updateBoard(board.firstPlayerPits, board.firstPlayerLargePit, board.secondPlayerPits, board.secondPlayerLargePit);
    } else {
        updateBoard(board.secondPlayerPits, board.secondPlayerLargePit, board.firstPlayerPits, board.firstPlayerLargePit);
    }

    var winner = board.result;
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

    var playerTurn = board.playerTurn;
    if (isFirstPlayer && playerTurn === 'FIRST_PLAYER' || !isFirstPlayer && playerTurn !== 'FIRST_PLAYER') {
        setMessage("Now it's your turn!");
        changePitsStatus();
    } else {
        setMessage("Waiting for your opponent's turn...", true)
    }
}

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

function connectWebSocket(stompSuccessCallback, stompFailureCallback) {
    var socket = new SockJS('/websocket');
    var stompClient = Stomp.over(socket);
    stompClient.connect({}, stompSuccessCallback.bind(null, stompClient), stompFailureCallback);
}

function copyLinkToClipboard() {
    var link = document.getElementById("gameLink").href;
    navigator.clipboard.writeText(link);

    var tooltip = document.getElementById("copyTooltip");
    tooltip.innerHTML = "Copied: " + link;
}