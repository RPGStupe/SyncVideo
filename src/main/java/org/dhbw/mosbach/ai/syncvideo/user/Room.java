package org.dhbw.mosbach.ai.syncvideo.user;

import org.dhbw.mosbach.ai.syncvideo.util.RandomString;
import org.dhbw.mosbach.ai.syncvideo.websocket.UserSessionHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import javax.json.*;
import javax.json.spi.JsonProvider;
import javax.websocket.Session;
import java.util.*;

public class Room {

    public LinkedList<Video> getPlaylist() {
        return playlist;
    }

    private LinkedList<Video> playlist;
    private static final String USER_AGENT = "Mozilla/5.0 (X11; U; Linux x86_64; en-US; rv:1.9.2.13) Gecko/20101206 Ubuntu/10.10 (maverick) Firefox/3.6.13";
    private static final String ripLink = "http://i.imgur.com/eKmmyv1.mp4";
    private HashMap<Session, Boolean> readyStates = new HashMap<Session, Boolean>();
    private HashMap<Session, User> userMap = new HashMap<Session, User>();
    private List<Session> sessions;
    private String video = "";
    private Session host;
    private String id;
    private boolean playing = false;
    private boolean buffering = false;
    private boolean isDirectLink = false;
    private boolean autoNext = false;
    private String lastCookie = "";
    private CloseableHttpClient httpClient;
    private JsonNumber timestamp;
    private Random random = new Random();
    private Video lastVideo;
    private static RandomString randomString = new RandomString(10);

    public Room(Session host, String hostname, String hostuid, boolean hostanonymous) {
        playlist = new LinkedList<Video>();
        this.host = host;
        do {
            id = randomString.nextString();
        } while (!RoomHandler.getInstance().checkId(id));
        sessions = new LinkedList<Session>();
        this.addSession(host, hostname, hostuid, hostanonymous);
        RoomHandler.getInstance().addRoom(this);
        httpClient = HttpClients.createDefault();
    }

    public Video getLastVideo() {
        return lastVideo;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isPlaying() {
        return playing;
    }

    public void setAutoNext(boolean value) {
        autoNext = value;
    }

    public void pause(JsonNumber current, Session session, boolean intended) {
        if (this.playing) {
            buffering = !intended;
            this.playing = false;
            this.timestamp = current;
            JsonProvider provider = JsonProvider.provider();
            JsonObject messageJson = provider.createObjectBuilder()
                    .add("action", "pause")
                    .add("current", current)
                    .build();
            if (!intended) {
                this.markReady(session, false);
            }
            UserSessionHandler.getInstance().sendToRoom(messageJson, this);
        }
    }

    public boolean isHost(Session s) {
        return s == host;
    }

    public List<Session> getSessions() {
        return new LinkedList<Session>(sessions);
    }

    public void addSession(Session session, String name, String uid, boolean anonymous) {
        String avatarUrl = "http://www.thehindu.com/sci-tech/technology/internet/article17759222.ece/alternates/FREE_660/02th-egg-person";
        User user = new User(uid, name, avatarUrl, anonymous);
        userMap.put(session, user);
        setName(session, name);
        readyStates.put(session, false);
        sessions.add(session);
        JsonProvider provider = JsonProvider.provider();
        JsonObject messageJson = provider.createObjectBuilder()
                .add("action", "roomID")
                .add("id", id)
                .build();
        UserSessionHandler.getInstance().sendToSession(session, messageJson);
        if (!video.equals("")) {
            sendVideoToSession(session, true);
            sendPlaylist();
        }
        sendRoomList();
    }

    public void removeSession(Session session) {
        readyStates.remove(session);
        userMap.remove(session);
        sessions.remove(session);
        if (session == host) {
            if (sessions.isEmpty()) {
                RoomHandler.getInstance().removeRoom(this);
                return;
            } else {
                host = sessions.get(0);
                JsonProvider provider = JsonProvider.provider();
                JsonObject messageJson = provider.createObjectBuilder()
                        .add("action", "owner")
                        .build();
                UserSessionHandler.getInstance().sendToSession(host, messageJson);
            }
        }
        sendRoomList();
    }

    public void addVideo(String url) {
        playlist.add(new Video(url));
        if (playlist.size() == 1) {
            updatePlaylistInfo(playlist.peek());
            if (!playlist.isEmpty()) {
                setVideo(playlist.peek().url);
            }
        }
        sendPlaylist();
    }

    public void addVideo(String url, int episode) {
        System.out.println(episode);
        playlist.add(new Video(url, episode));
        if (playlist.size() == 1) {
            updatePlaylistInfo(playlist.peek());
            if (!playlist.isEmpty()) {
                setVideo(playlist.peek().url);
            }
        }
        sendPlaylist();
    }

    private void sendPlaylist() {
        updatePlaylistInfo();
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (Video v : playlist) {
            arrayBuilder.add(Json.createObjectBuilder()
                    .add("title", "")
                    .build());
        }
        JsonProvider provider = JsonProvider.provider();
        JsonObject messageJson = provider.createObjectBuilder()
                .add("action", "playlist")
                .add("playlist", arrayBuilder.build())
                .build();
        UserSessionHandler.getInstance().sendToRoom(messageJson, this);
    }

    private void updatePlaylistInfo() {
        for (Video v : playlist) {
            updatePlaylistInfo(v);
        }
    }

    public void playNow(int episode) {
        if (episode > 0) {
            for (int i = 0; i < episode - 1; i++) {
                playlist.poll();
            }
            loadNextVideo();
        }
    }

    public void delete(int episode) {
        if (episode == 0) {
            if (playlist.size() != 1) {
                loadNextVideo();
            }
        } else {
            playlist.remove(playlist.get(episode));
            sendPlaylist();
        }
    }

    private void updatePlaylistInfo(Video v) {
        if (!v.infoGot) {
            System.out.println("fetching info for video with url " + v.url);
            v.url = createDirectLink(v.url);
            if (v.url.equals("")) {
                playlist.remove(v);
                sendDebugToHost("invalid URL");
            }
            v.infoGot = true;
        }
    }

    private void sendRoomList() {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (Session s : sessions) {
            arrayBuilder.add(Json.createObjectBuilder()
                    .add("uid", userMap.get(s).getUid())
                    .add("name", userMap.get(s).getName())
                    .add("avatar", userMap.get(s).getAvatarUrl())
                    .add("isOwner", s == host)
                    .build());
        }
        JsonProvider provider = JsonProvider.provider();
        JsonObject messageJson = provider.createObjectBuilder()
                .add("action", "room-list")
                .add("userList", arrayBuilder.build())
                .build();
        UserSessionHandler.getInstance().sendToRoom(messageJson, this);
    }

    public void changeName(Session s, String name) {
        String old = userMap.get(s).getName();
        if (!name.equals(old)) {
            setName(s, name);
            sendRoomList();
        }
    }

    private void setName(Session s, String name) {
        if (name.contains("<")) {
            name = "User " + random.nextInt(10000);
        }
        userMap.get(s).setName(name);
    }

    public void setVideo(String url) {
        timestamp = null;
        for (Session s : readyStates.keySet()) {
            markReady(s, false);
        }
        video = url;
        playing = false;
        sendVideoToRoom();
    }

    private void sendVideoToRoom() {
        for (Session s : sessions) {
            sendVideoToSession(s, false);
        }
    }

    public void setCurrent(JsonNumber current) {
        this.timestamp = current;
    }

    private void sendVideoToSession(Session s, boolean newJoin) {
        if (newJoin) {
            if (timestamp != null) {
                System.out.println("pause");
                pause(timestamp, s, false);
            }
        }
        String url = createDirectLink(playlist.peek().url);
        JsonProvider provider = JsonProvider.provider();
        JsonObject messageJson;
        if (timestamp == null) {
            messageJson = provider.createObjectBuilder()
                    .add("action", "video")
                    .add("url", url)
                    .add("current", 0)
                    .build();
        } else {
            messageJson = provider.createObjectBuilder()
                    .add("action", "video")
                    .add("url", url)
                    .add("current", timestamp)
                    .build();
        }
        UserSessionHandler.getInstance().sendToSession(s, messageJson);
    }

    private String createDirectLink(String video) {
        System.out.println(video);
        return video;
    }


    private void sendDebugToHost(String s) {
        JsonProvider provider = JsonProvider.provider();
        JsonObject messageJson = provider.createObjectBuilder()
                .add("action", "debug")
                .add("message", s)
                .build();
        UserSessionHandler.getInstance().sendToSession(host, messageJson);
    }

    public void videoFinished() {
        playing = false;
        buffering = false;
        loadNextVideo();
    }

    public void loadNextVideo() {
        Video v = playlist.poll();
        lastVideo = v;
        timestamp = null;
        if (!playlist.isEmpty()) {
            sendPlaylist();
            setVideo(playlist.peek().getUrl());
        } else {
            sendPlaylist();
        }
    }

    public void markReady(Session s, boolean status) {
        readyStates.put(s, status);
        if (!playing && buffering) {
            play();
        }
    }

    public void play() {
        boolean flag = true;
        for (Session s : sessions) {
            if (!readyStates.get(s)) {
                sendBufferedRequest(s);
                buffering = true;
                flag = false;
            }
        }
        if (flag) {
            //start playing
            JsonProvider provider = JsonProvider.provider();
            JsonObject messageJson = provider.createObjectBuilder()
                    .add("action", "play")
                    .build();
            playing = true;
            buffering = false;
            UserSessionHandler.getInstance().sendToRoom(messageJson, this);
        }
    }

    private void sendBufferedRequest(Session s) {
        JsonProvider provider = JsonProvider.provider();
        JsonObject messageJson = provider.createObjectBuilder()
                .add("action", "bufferedRequest")
                .build();
        UserSessionHandler.getInstance().sendToSession(s, messageJson);
    }

    public String getId() {
        return id;
    }

    public String getUrl() {
        return video;
    }


    public Map<Session, User> getUserMap() {
        return userMap;
    }
}
