package org.dhbw.mosbach.ai.syncvideo.websocket;

import org.dhbw.mosbach.ai.syncvideo.user.Room;

import javax.enterprise.context.ApplicationScoped;
import javax.json.JsonObject;
import javax.websocket.Session;
import java.io.IOException;
import java.util.*;

@ApplicationScoped
public class UserSessionHandler {
    private final Set<Session> sessions = new HashSet<Session>();
    private static UserSessionHandler instance;

    private UserSessionHandler() {}

    public static UserSessionHandler getInstance() {
        if(instance == null) {
            instance = new UserSessionHandler();
        }
        return instance;
    }

    public void addSession(javax.websocket.Session session) {
        sessions.add(session);
    }

    public void removeSession(javax.websocket.Session session) {
        sessions.remove(session);
    }

    public void sendToRoom(JsonObject message, Room room) {
        for(Session s: room.getSessions()) {
            sendToSession(s,message);
        }
    }

    public synchronized void sendToSession(javax.websocket.Session session, JsonObject message) {
        try {
            session.getBasicRemote().sendText(message.toString());
        } catch (IOException ex) {
            sessions.remove(session);
        }
    }
}
