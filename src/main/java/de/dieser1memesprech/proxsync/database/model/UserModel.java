package de.dieser1memesprech.proxsync.database.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlID;

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

    public UserModel() {
        super();
    }

    public Long getId() {
        return id;
    }
}
