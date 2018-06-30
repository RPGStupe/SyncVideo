package org.dhbw.mosbach.ai.syncvideo.user;

public class Video {
    String title;
    String url;
    boolean infoGot;
    public String key;


    public Video(String url, int episode) {
        this.url = url;
        this.infoGot = false;
        this.title = url;
        this.url = url;
    }

    public Video(String url) {
        this.infoGot = false;
        this.title = url;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getKey() {
        return key;
    }
}
