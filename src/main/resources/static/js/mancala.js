(function() {
    var i;
    var myPitsDivs = document.getElementById("firstPlayerDiv").getElementsByTagName('div');
    for (i = 0; i < myPitsDivs.length; i++) {
        myPitsDivs[i].addEventListener("click", makeMove.bind(null, i));
    }

    connectWebSocket();
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
        changePitsStatus(true);
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
    changePitsStatus(false);
    $.post("/game/" + gameId + "/makeMove", {pitIndex: i})
        .fail(function (error) {
            console.log(error);
            alert(error.responseJSON.message);
            changePitsStatus(true);
        });
}

function changePitsStatus(enabled) {
    var myPitsDivs = document.getElementById("firstPlayerDiv").getElementsByTagName('div');
    for (i = 0; i < myPitsDivs.length; i++) {
        if (!enabled) {
            myPitsDivs[i].classList.remove("pit-enabled");
        } else if (parseInt(myPitsDivs[i].innerHTML) > 0) {
            myPitsDivs[i].classList.add("pit-enabled");
        }
    }
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

function stompSuccessCallback(stompClient, frame) {
    console.log('STOMP: Connected: ' + frame);
    stompClient.subscribe('/topic/game/' + gameId, function (result) {
        var game = JSON.parse(result.body);
        updateGame(game);
    });
}

function stompFailureCallback(error) {
    console.log('STOMP: ' + error);
    setTimeout(connectWebSocket, 10000);
    console.log('STOMP: Reconnecting in 10 seconds');
};

function copyLinkToClipboard() {
    var link = document.getElementById("gameLink").href;
    navigator.clipboard.writeText(link);

    var tooltip = document.getElementById("copyTooltip");
    tooltip.innerHTML = "Copied: " + link;
}