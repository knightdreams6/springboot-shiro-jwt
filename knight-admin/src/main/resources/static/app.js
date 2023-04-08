var stompClient = null;

function setConnected(connected) {
  $("#connect").prop("disabled", connected);
  $("#disconnect").prop("disabled", !connected);
  if (connected) {
    $("#conversation").show();
  } else {
    $("#conversation").hide();
  }
  $("#greetings").html("");
}

function connect() {
  var socket = new SockJS('http://127.0.0.1:8888/websocket');
  stompClient = Stomp.over(socket);
  stompClient.connect({"Authorization": "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJwaG9uZSI6IjE4NzM1MTgyMjg1IiwidXNlcklkIjoiMSIsImV4cCI6MTY4MDk0NjM1NX0.FYDiMtcDvosixoCBzn1KrOiZLGqm5uZdkbncpfG8wvA"}, function (frame) {
    setConnected(true);
    console.log('Connected: ' + frame);
    stompClient.subscribe('/topic/outgoing', function (greeting) {
      showGreeting(greeting.body);
      console.log("greeting", greeting.body)
    });
    stompClient.subscribe('/user/queue/specified', function (specified) {
      showGreeting(specified.body);
      console.log("specified", specified.body)
    });
  });
}

function disconnect() {
  if (stompClient !== null) {
    stompClient.disconnect();
  }
  setConnected(false);
  console.log("Disconnected");
}

function sendName() {
  stompClient.send("/app/incoming", {}, JSON.stringify({'message': $("#name").val()}));
}

function showGreeting(message) {
  $("#greetings").append("<tr><td>" + message + "</td></tr>");
}

$(function () {
  $("form").on('submit', function (e) {
    e.preventDefault();
  });
  $("#connect").click(function () {
    connect();
  });
  $("#disconnect").click(function () {
    disconnect();
  });
  $("#send").click(function () {
    sendName();
  });
});

