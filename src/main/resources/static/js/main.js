keyboardeventKeyPolyfill.polyfill();
var pre = $('#pre');
var body = $('body');
var connect = false;
var termInstance = null;
var editor = null;
var helloString = "Welcome to interactive WebIDE.";
var codeString = null;
var socketUri = "ws://127.0.0.1:8080/ideWsServer";
var socket = null;
var curLang = "1";

function scroll_to_bottom() {
    var sHeight = body.prop('scrollHeight');
    body.scrollTop(sHeight);
}

function writeLineToTerminal(line) {
    if (termInstance == null) {
        return;
    }
    termInstance.echo(line);
}

function handleTerminalInput(command) {
    //console.log(command);
    if (socket != null) {
        socket.send(encodeURIComponent("ip@" + command));
    }
}

//create terminal

function createTerminal() {
    termInstance = $('#term').terminal(handleTerminalInput, { prompt: '', greetings: helloString });
}
function destroyTerminal() {
    if (termInstance == null) {
        return;
    }
    termInstance.destroy();
    termInstance = null;
}

function onChangeLang(obj) {
    var opt = obj.options[obj.selectedIndex].value
    curLang = opt;
    console.log(opt);
    if (opt == 1) {
        editor.session.setMode("ace/mode/python");
    }
    if (opt == 2) {
        editor.session.setMode("ace/mode/java");
    }
}

function onWsOpen() {
    //write code
    var pro = "us@" + curLang + "@" + codeString;
    pro = pro.replace("+","%2B")
    pro = encodeURI(encodeURI(pro))
    socket.send(pro);
    
}

function onWsMsg(event) {
    var text = decodeURIComponent(event.data.replace(/\+/g, '%20')).replace("%2B","+");
    var arr = text.split('@');
    if (arr.length != 2) {
        return false;
    }
    if (arr[0] == 'us' && arr[1] == 'ok') {
        writeLineToTerminal('Upload code success!');
    }else
    if (arr[0] == 'et' && arr[1] == 'et') {
        writeLineToTerminal('Code process complete!');
    }else
    if (arr[0] == 'op') {
        writeLineToTerminal(arr[1]);
    }
}
function onWsClose(event) {
    writeLineToTerminal('Exit');
    socket = null;
}
function onClickRun() {

    destroyTerminal();
    createTerminal();
    codeString = editor.getValue();
    //create webSocket
    socketUri = "ws://" + window.location.host + "/ideWsServer";
    socket = new WebSocket(socketUri);
    socket.onopen = onWsOpen;
    socket.onmessage = onWsMsg;
    socket.onclose = onWsClose;
}


//create editor
editor = ace.edit("editor");
editor.setTheme("ace/theme/monokai");
editor.session.setMode("ace/mode/python");
editor.setValue('');