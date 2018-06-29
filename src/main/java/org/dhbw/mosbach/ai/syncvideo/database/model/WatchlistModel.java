package org.dhbw.mosbach.ai.syncvideo.database.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.xml.bind.annotation.*;
import java.io.Serializable;

/*
    Model for watchlist
 */
@Entity
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class WatchlistModel implements Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @XmlID
    private Long id;

    @XmlElement
    private String url;

    @XmlElement
    private Long timestamp;

    @XmlElement
    private Long userId;

    public WatchlistModel() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

}
