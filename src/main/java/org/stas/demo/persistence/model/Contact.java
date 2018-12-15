package org.stas.demo.persistence.model;


import javax.persistence.*;

/**
 * @author stas ambartsumov
 * represents a Contact entity
 */
@Entity
@Table(name = "contacts")
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false)
    private String name;

    @Column
    private String url;

    public Contact() {}

    public Contact(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Contact[" +
                "name=" + name +
                ", url=" + url +
                ']';
    }
}
