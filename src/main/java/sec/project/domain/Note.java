/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sec.project.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * Simple Note entity that stores the username of the Note owner, and the text
 * of a Note.
 *
 * TODO: the username should match a username in the Users table. This is not
 * currently enforced.
 *
 * @author BenR
 */
@Entity
@Table(name = "Notes")
public class Note extends AbstractPersistable<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = false)
    private String username;
    @Column(unique = false)
    private String note;

    /**
     * Default no-args constructor
     */
    public Note() {
    }

    /**
     * Construct a new Note object with a supplied username and note text
     *
     * @param username the username that owns the Note
     * @param note the text of the Note
     */
    public Note(String username, String note) {
        this();
        this.username = username;
        this.note = note;
    }

    /**
     * Getter for the username property
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Setter for the username property
     *
     * @param username the password to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Getter for the note text property
     *
     * @return the note text
     */
    public String getNote() {
        return note;
    }

    /**
     * Setter for the note text property
     *
     * @param note the note text to set
     */
    public void setNote(String note) {
        this.note = note;
    }

    /**
     * Implementation of the toString() method for debugging
     *
     * @return a String representation of the Note object
     */
    @Override
    public String toString() {
        return "Note{" + "id=" + id + ", username=" + username + ", note=" + note + '}';
    }
}
