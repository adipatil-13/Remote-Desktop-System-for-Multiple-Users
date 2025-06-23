package com.rds.rds_multi.service;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SessionManager {
    private final Set<WebSocketSession> sessions = ConcurrentHashMap.newKeySet();

    public void addSession(WebSocketSession session) {
        sessions.add(session);
    }

    public void removeSession(WebSocketSession session) {
        sessions.remove(session);
    }

    public void broadcast(String message) {
        for (WebSocketSession session : sessions) {
            if (session.isOpen()) {
                try {
                    session.sendMessage(new org.springframework.web.socket.TextMessage(message));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Set<WebSocketSession> getAllSessions() {
        return sessions;
    }
}
