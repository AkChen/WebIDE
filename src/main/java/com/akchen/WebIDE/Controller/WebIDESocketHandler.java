package com.akchen.WebIDE.Controller;

import com.akchen.WebIDE.Service.WebIDESocketService;
import com.akchen.WebIDE.Util.IDEStringHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class WebIDESocketHandler extends TextWebSocketHandler {
    @Autowired
    private WebIDESocketService webIDESocketService;


    @Override
    protected void handleTextMessage(WebSocketSession wsSession, TextMessage message) throws Exception {
        webIDESocketService.HandleMessage(wsSession, IDEStringHelper.doDecode(message.getPayload()));
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        webIDESocketService.HandleClose(session);
    }
}
