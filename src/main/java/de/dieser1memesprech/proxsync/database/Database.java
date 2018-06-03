package de.dieser1memesprech.proxsync.database;

import com.google.firebase.database.*;
import de.dieser1memesprech.proxsync.config.Configuration;
import java.util.*;

public class Database {
    public static FirebaseDatabase database;

    public static void updateStreamId(String id) {
        database.getReference("config/stream-id").setValueAsync(id);
    }

    public static void initStreamIdListener() {
        database.getReference("config/stream-id").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("value event received");
                Configuration.instance.setStreamServerId(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static void addAnimeinfoToDatabase(String key, String title, List<String> res) {
        Map<String, Object> dataMapEpisodeNames = new LinkedHashMap<String, Object>();
        for (int i = 0; i < res.size(); i++) {
            dataMapEpisodeNames.put(Integer.toString(i), res.get(i));
        }

        Map<String, Object> dataMapAnimeInfo = new LinkedHashMap<String, Object>();
        dataMapAnimeInfo.put("title", title);
        dataMapAnimeInfo.put("episodenames", dataMapEpisodeNames);

        updateData("anime/animeinfo/" + key.replaceAll("\\.", "-"), dataMapAnimeInfo);
    }

    public static void updateData(String path, Map<String, Object> data) {
        DatabaseReference ref = database.getReference(path);
        ref.updateChildren(data);
    }

    public static void setAvatar(String uid, String url) {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("avatar", url);
        updateData("users/" + uid, map);
    }

    public static String getBannerFromDatabase(String uid) {
        DataSnapshot data = getDataFromDatabase("users/" + uid + "/banner");
        return data.getValue(String.class);
    }

    public static String getAvatarFromDatabase(String uid) {
        DataSnapshot data = getDataFromDatabase("users/" + uid + "/avatar");
        String res = data.getValue(String.class);
        if(res == null) {
            res = "null";
        }
        return res;
    }

    public static void setBanner(String uid, String url) {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("banner", url);
        updateData("users/" + uid, map);
    }

    public static DataSnapshot getDataFromDatabase(String path) {
        try {
            // attach a value listener to a Firebase reference
            DatabaseReference ref = database.getReference(path);
            LoadedValueEventListener listener = new LoadedValueEventListener();
            ref.addListenerForSingleValueEvent(listener);
            DataSnapshot data = listener.getData();
            ref.removeEventListener(listener);
            return data;
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
