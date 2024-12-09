'use strict';

const usernamePage = document.querySelector('#username-page');
const gamePage = document.querySelector('#game-page');
const usernameForm = document.querySelector('#usernameForm');
const moveForm = document.querySelector('#move-form');
const statusMessage = document.querySelector('#status-message');
const gameBoardContainer = document.querySelector('#game-board-header');
const detectiveInfoContainer = document.getElementById('detective-info');
const turnIndicator = document.getElementById('turn-indicator');
const boardContainer = document.getElementById('game-board');
const transportationDropdown = document.querySelector('#transportation');
const nodeIdSelect = document.querySelector('#node-id');
const detectiveMarkerImg = document.getElementById('detective-marker');
const thiefMarkerImg = document.getElementById('thief-marker');


const nodePositions = {
    1: { x: 141, y: 72 }, 2: { x: 590, y: 78 }, 3: { x: 495, y: 629 },
    4: { x: 98, y: 628 }, 5: { x: 430, y: 86 }, 6: { x: 516, y: 256 },
    7: { x: 504, y: 423 }, 8: { x: 289, y: 603 }, 9: { x: 74, y: 426 },
    10: { x: 106, y: 270 }, 11: { x: 316, y: 205 }, 12: { x: 349, y: 366 },
};


let stompClient = null;
let username = null;
let gameId = null;
let isSuspect = false;
let currentRound = null;
let currentPlayers = [];
const REVEAL_ROUNDS = [6, 12, 18, 24];


function connect(event) {
    event.preventDefault();
    const nameInput = document.querySelector('#name');
    if (!nameInput) return;

    username = sanitizeName(nameInput.value.trim());
    if (!username) {
        updateStatusMessage('Username is required!', 'red');
        return;
    }

    usernamePage.classList.add('hidden');
    gamePage.classList.remove('hidden');

    const socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, onConnected, onError);
}


function onConnected() {
    joinGame(username);
    stompClient.subscribe('/topic/game-created', handleGameCreated);
    stompClient.subscribe('/topic/game-progress', handleGameProgress);
    stompClient.subscribe('/topic/reply', handleReplyMessage);
    stompClient.subscribe('/topic/game-over', handleGameOver);
    stompClient.subscribe('/topic/valid-destinations', handleValidDestinations);
}


function onError(error) {
    updateStatusMessage('Could not connect to the server. Please refresh and try again.', 'red');
    console.error('WebSocket error:', error);
}

function joinGame(player) {
    sendMessage({
        type: 'game.join',
        player,
    });
}

function makeMove(event) {
    event.preventDefault();
    const nodeIdInput = document.querySelector('#node-id');
    if (!nodeIdInput) {
        updateStatusMessage('Node ID input not found.', 'red');
        return;
    }

    const nodeId = nodeIdInput.value;
    if (!nodeId) {
        updateStatusMessage('Node ID is required.', 'red');
        return;
    }

    let transportation = null;
    if (transportationDropdown && !transportationDropdown.disabled && transportationDropdown.style.display !== 'none') {
        transportation = transportationDropdown.value;
        if (!transportation) {
            updateStatusMessage('Transportation is required for subsequent moves.', 'red');
            return;
        }
    }
    sendMessage({
        type: 'game.move',
        gameId,
        player: username,
        nodeId,
        transportation,
    });
}


transportationDropdown.addEventListener('change', () => {
    const transport = transportationDropdown.value;
    if (!transport) {
        clearNodeDropdown();
        return;
    }

    if (transport && gameId && username) {
        sendMessage({
            type: 'game.destinations',
            gameId: gameId,
            player: username,
            transportation: transport
        });
    }
});

function handleGameCreated(message) {
    try {
        const gameData = JSON.parse(message.body);
        currentRound = gameData.Round;
        updateStatusMessage(`${gameData.Message} Game ID: ${gameData.GameId}`, 'green');
        gameId = gameData.GameId;
        renderGameBoard(gameData.Users, gameData.Turn, gameData.Round);
        renderGameBoardColor(gameData.Users);
    } catch (error) {
        console.error('Failed to handle game-created message:', error, message.body);
    }
}

function handleGameProgress(message) {
    try {
        const gameData = JSON.parse(message.body);
        currentRound = gameData.Round;
        renderGameBoard(gameData.Users, gameData.Turn, gameData.Round);
        renderGameBoardColor(gameData.Users);
    } catch (error) {
        console.error('Failed to handle game-progress message:', error, message.body);
    }
}


function handleReplyMessage(message) {
    try {
        const payload = JSON.parse(message.body);
        if (sanitizeName(payload.username) === username) {
            updateStatusMessage(`Welcome ${payload.username}, ${payload.message}`, 'blue');
        } else {
            console.log('Message for another user:', payload.username);
        }
    } catch (error) {
        console.error('Failed to handle reply message:', error, message.body);
    }
}


function handleGameOver(message) {
    try {
        const gameData = JSON.parse(message.body);
        renderGameOver(gameData.Winner);
    } catch (error) {
        console.error('Failed to handle game-over message:', error, message.body);
    }
}



function handleValidDestinations(message) {
    try {
        const data = JSON.parse(message.body);
        const destinations = data.Destinations;

        clearNodeDropdown();
        destinations.forEach(dest => {
            const option = document.createElement('option');
            option.value = dest;
            option.textContent = dest;
            nodeIdSelect.appendChild(option);
        });
    } catch (error) {
        console.error('Failed to handle valid-destinations message:', error, message.body);
    }
}


function renderGameBoard(players, currentTurn, round) {
    currentPlayers = players;
    gameBoardContainer.innerHTML = '';
    detectiveInfoContainer.innerHTML = '';

    const currentPlayerTurnName = sanitizeName(currentTurn.name);
    const isMyTurn = currentPlayerTurnName === username;
    let isFirstMove = false;

    players.forEach((player) => {
        const playerName = sanitizeName(player.name);
        const isCurrentPlayer = (playerName === username);
        const isPlayerSuspect = player.suspect;

        if (isCurrentPlayer) {
            isSuspect = isPlayerSuspect;
            isFirstMove = (player.location === -1);
        }

        let locationText;
        if (player.location === -1) {
            locationText = 'Not set';
        } else if (isPlayerSuspect) {
            locationText = isCurrentPlayer
                ? player.location
                : (REVEAL_ROUNDS.includes(round) ? player.location : 'Unknown');
        } else {
            locationText = player.location;
        }

        const playerDetailsContainer = document.createElement('div');
        playerDetailsContainer.classList.add('player-details');

        if (isCurrentPlayer) {
            playerDetailsContainer.innerHTML = `
                <div><strong>Name:</strong> ${playerName}</div>
                <div><strong>Role:</strong> ${isPlayerSuspect ? 'Suspect' : 'Detective'}</div>
                <div><strong>Location:</strong> ${locationText}</div>
                ${
                isPlayerSuspect
                    ? '' // Do not display tickets for suspects
                    : `<div><strong>Tickets:</strong> ${formatTickets(player.tickets)}</div>`
            }
            `;
            gameBoardContainer.appendChild(playerDetailsContainer);
        }

        if (!player.suspect) {
            const detectiveDetailsContainer = document.createElement('div');
            detectiveDetailsContainer.classList.add('detective-details');
            detectiveDetailsContainer.innerHTML = `
                <div><strong>Detective Name:</strong> ${playerName}</div>
                <div><strong>Location:</strong> ${locationText}</div>
            `;
            detectiveInfoContainer.appendChild(detectiveDetailsContainer);
        }
    });

    turnIndicator.textContent = `Current Turn: ${currentPlayerTurnName} | Round: ${round}`;
    toggleMoveForm(isMyTurn, isFirstMove);
}



function toggleMoveForm(isMyTurn, isFirstMove) {
    if (!moveForm) return;
    if (isMyTurn) {
        moveForm.style.display = 'block';
        if (isFirstMove) {
            transportationDropdown.disabled = true;
            transportationDropdown.style.display = 'none';
            populateAllNodes();
        } else {
            transportationDropdown.disabled = false;
            transportationDropdown.style.display = 'block';
            clearNodeDropdown();

            const currentPlayer = currentPlayers.find(p => sanitizeName(p.name) === username);
            if (!currentPlayer) return;

            transportationDropdown.innerHTML = `
                <option value="" disabled selected>Select Transport</option>
                <option value="TRAIN">Train</option>
                <option value="TAXI">Taxi</option>
            `;

            const tickets = currentPlayer.tickets || {};
            if (!tickets.TRAIN || tickets.TRAIN <= 0) {
                const trainOption = transportationDropdown.querySelector('option[value="TRAIN"]');
                if (trainOption) trainOption.remove();
            }
            if (!tickets.TAXI || tickets.TAXI <= 0) {
                const taxiOption = transportationDropdown.querySelector('option[value="TAXI"]');
                if (taxiOption) taxiOption.remove();
            }

            if (transportationDropdown.options.length <= 1) {
                clearNodeDropdown();
            }
        }
    } else {
        moveForm.style.display = 'none';
    }
}

function renderGameBoardColor(players) {
    Array.from(boardContainer.children).forEach((child) => {
        if (child.tagName !== 'IMG') {
            boardContainer.removeChild(child);
        }
    });

    players.forEach((player) => {
        const node = player.location;
        if (node === -1 || !nodePositions[node]) return;

        if (player.suspect && sanitizeName(player.name) !== username && !REVEAL_ROUNDS.includes(currentRound)) {
            return;
        }

        const marker = createMarker(nodePositions[node].x, nodePositions[node].y, player.suspect);
        boardContainer.appendChild(marker);
    });
}

function createMarker(x, y, isSuspect) {
    const marker = document.createElement('div');
    marker.style.position = 'absolute';
    marker.style.left = `${x}px`;
    marker.style.top = `${y}px`;
    marker.style.transform = 'translate(-50%, -50%)';
    marker.style.width = '80px';
    marker.style.height = '80px';
    marker.style.borderRadius = '50%';
    marker.style.overflow = 'hidden';
    marker.style.zIndex = '10';
    marker.style.backgroundSize = 'cover';
    marker.style.backgroundPosition = 'center';

    const imgSrc = isSuspect ? thiefMarkerImg.src : detectiveMarkerImg.src;
    marker.style.backgroundImage = `url('${imgSrc}')`;
    return marker;
}

function renderGameOver(winner) {
    const statusElement = document.getElementById('status-message');
    if (!statusElement) return;

    let message;
    let color;
    if (winner === "Detectives") {
        if (!isSuspect) {
            message = "Game Over! You Win! The suspect was caught.";
            color = "green";
        } else {
            message = "Game Over! You Lose! The detectives caught you.";
            color = "red";
        }
    } else {
        if (isSuspect) {
            message = "Game Over! You Win! You evaded the detectives.";
            color = "green";
        } else {
            message = "Game Over! You Lose! The suspect got away.";
            color = "red";
        }
    }

    statusElement.textContent = message;
    statusElement.style.color = color;

    const moveFormElement = document.getElementById('move-form');
    if (moveFormElement) {
        moveFormElement.style.display = 'none';
    }
}

function sanitizeName(name) {
    return name.replace(/\"/g, '');
}

function updateStatusMessage(message, color = 'black') {
    if (statusMessage) {
        statusMessage.textContent = message;
        statusMessage.style.color = color;
    }
}

function sendMessage(message) {
    if (!stompClient) {
        console.error('STOMP client is not connected.');
        return;
    }
    stompClient.send(`/app/${message.type}`, {}, JSON.stringify(message));
}

function formatTickets(tickets) {
    if (!tickets) return 'No tickets';
    return Object.entries(tickets).map(([type, count]) => `${type}: ${count}`).join(', ');
}

function appendTextToContainer(container, text) {
    const p = document.createElement('p');
    p.textContent = text;
    container.appendChild(p);
}

function clearNodeDropdown() {
    while (nodeIdSelect.options.length > 1) {
        nodeIdSelect.remove(1);
    }
}

function populateAllNodes() {
    clearNodeDropdown();
    for (let i = 1; i <= 12; i++) {
        const option = document.createElement('option');
        option.value = i;
        option.textContent = i;
        nodeIdSelect.appendChild(option);
    }
}

if (usernameForm) {
    usernameForm.addEventListener('submit', connect);
}

if (moveForm) {
    moveForm.addEventListener('submit', makeMove);
}
