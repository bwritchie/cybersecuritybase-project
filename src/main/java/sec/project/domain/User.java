package sec.project.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * Simple User entity that stores username and password. The password should be
 * hashed somewhere, but is stored in plaintext.
 *
 * TODO: the Employee and Notes tables should only have usernames that 
 * correspond to a valid User object. This is not currently enforced.
 *
 * @author BenR
 */
@Entity
@Table(name = "Users")
public class User extends AbstractPersistable<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true)
    private String username;
    @Column(unique = false)
    private String password;

    /**
     * Default no-args constructor
     */
    public User() {
    }

    /**
     * Construct a new User object with a supplied username and password
     *
     * @param username the username
     * @param password the password
     */
    public User(String username, String password) {
        this();
        this.username = username;
        this.password = password;
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
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Getter for the password property
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Setter for the password property
     *
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Implementation of the toString() method for debugging
     * @return a String representation of the User object 
     */
    @Override
    public String toString() {
        return "User{" + "id=" + id + ", username=" + username + ", password=" + password + '}';
    }
}
