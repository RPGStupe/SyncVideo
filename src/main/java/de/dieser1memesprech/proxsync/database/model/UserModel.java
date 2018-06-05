package de.dieser1memesprech.proxsync.database.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.xml.bind.annotation.*;

import java.io.Serializable;

@Entity
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class UserModel implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @XmlID
    private Long id;

    @XmlElement
    private String username;

    @XmlElement
    private String pw;

    public UserModel(String username, String pw) {
        super();
        this.username = username;
        this.pw = pw;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPw() {
        return pw;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public void setPw(String pw) {
        this.pw = pw;
    }
}
