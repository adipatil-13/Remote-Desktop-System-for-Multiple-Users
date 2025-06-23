package com.rds.rds_multi.controller;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.net.URI;

@Component
public class ScreenSocketHandler extends TextWebSocketHandler {

    private final Map<String, List<WebSocketSession>> sessionMap = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String sessionId = getSessionIdFromQuery(session.getUri());
        session.getAttributes().put("sessionId", sessionId);

        sessionMap.computeIfAbsent(sessionId, k -> new ArrayList<>()).add(session);

        System.out.println("✅ " + session.getId() + " session: " + sessionId);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String sessionId = (String) session.getAttributes().get("sessionId");
        if (sessionId != null && sessionMap.containsKey(sessionId)) {
            sessionMap.get(sessionId).remove(session);
            System.out.println("❌ " + session.getId() + " session: " + sessionId);
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String sessionId = (String) session.getAttributes().get("sessionId");
        if (sessionId == null) return;

        List<WebSocketSession> peers = sessionMap.get(sessionId);
        if (peers == null) return;

        String payload = message.getPayload();

        for (WebSocketSession client : peers) {
            if (client.isOpen() && !client.getId().equals(session.getId())) {
                client.sendMessage(new TextMessage(payload));
            }
        }
    }


    private String getSessionIdFromQuery(URI uri) {
        if (uri != null && uri.getQuery() != null) {
            String[] params = uri.getQuery().split("&");
            for (String param : params) {
                String[] keyValue = param.split("=");
                if (keyValue.length == 2 && keyValue[0].equals("session")) {
                    return keyValue[1];
                }
            }
        }
        return "default";
    }
}
