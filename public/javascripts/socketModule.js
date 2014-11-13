var socketModule = angular.module('socketModule', []);

socketModule.factory('Socket',function(){
    var websocket;
    var wsUri = "ws://localhost:9000/socket";
    function onClose(evt){
       console.log("Close websocket");
    }
    function onMessage(evt,callbackCtrlFn) {
       var plane = JSON.parse(event.data);
       callbackCtrlFn(plane);
    }
    return{
        open: function(callbackCtrlFn){
            if(websocket == undefined || websocket.readyState == 3){
                    websocket = new WebSocket(wsUri);
                    websocket.onclose = function(evt) { onClose(evt) };
                    websocket.onmessage = function(evt) { onMessage(evt,callbackCtrlFn) };
            }
        },
        close: function(){  websocket.close();}
    }
});

