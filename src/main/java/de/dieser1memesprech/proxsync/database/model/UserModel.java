package de.dieser1memesprech.proxsync.database.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.xml.bind.annotation.*;

import java.io.Serializable;

@Entity
@XmlAccessorType(XmlAccessType.FIELD)
public class UserModel implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @XmlID
    private Long id;

    private String username;

    private String pw;
    private String banner;
    private String avatar;

    public UserModel() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

	public String getBanner() {
		return banner;
	}

	public void setBanner(String banner) {
		this.banner = banner;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
}
