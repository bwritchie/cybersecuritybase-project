package sec.project.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * Simple Employee entity that stores basic information about the employee. Some
 * of the data is confidential, and should be protected. But it is not.
 *
 * TODO: the username should match a username in the Users table. This is not
 * currently enforced.
 *
 * @author BenR
 */
@Entity
@Table(name = "Employees")
public class Employee extends AbstractPersistable<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true)
    private String username;
    @Column(unique = false)
    private String name;
    @Column(unique = false)
    private String address;
    @Column(unique = false)
    private String ninumber;
    @Column(unique = false)
    private float salary;
    @Column(unique = false)
    private boolean manager;

    /**
     * Default no-args constructor
     */
    public Employee() {
    }

    /**
     * Constructor for a new Employee entity
     *
     * @param username the username (needs to match a User object)
     * @param name the name of the Employee
     * @param address the address of the Employee
     * @param ninumber the National Insurance Number of the Employee
     * @param salary the Salary of the Employee
     * @param isManager is the Employee a Manager?
     */
    public Employee(String username, String name, String address, String ninumber, float salary, boolean isManager) {
        this();
        this.username = username;
        this.name = name;
        this.ninumber = ninumber;
        this.address = address;
        this.salary = salary;
        this.manager = isManager;
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
     * Getter for the name property
     *
     * @return the Employee's name
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for the name property
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for the address property
     *
     * @return the Employee's address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Setter for the address property
     *
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Getter for the National Insurance number property
     *
     * @return the Employee's national insurance number
     */
    public String getNinumber() {
        return this.ninumber;
    }

    /**
     * Setter for the National Insurance number property
     *
     * @param ninumber the National Insurance number to set
     */
    public void setNinumber(String ninumber) {
        this.ninumber = ninumber;
    }

    /**
     * Getter for the salary property
     *
     * @return the Employee's salary
     */
    public float getSalary() {
        return this.salary;
    }

    /**
     * Setter for the salary property
     *
     * @param salary the salary to set
     */
    public void setSalary(float salary) {
        this.salary = salary;
    }

    /**
     * Getter for the manager property
     *
     * @return true if the Employee is a manager
     */
    public boolean getManager() {
        return this.manager;
    }

    /**
     * Setter for the isManager property
     *
     * @param isManager true if the Employee is a manager
     */
    public void setManager(boolean isManager) {
        this.manager = isManager;
    }

    /**
     * Implementation of the toString() method for debugging
     *
     * @return a String representation of the Employee object
     */
    @Override
    public String toString() {
        return "Employee{" + "id=" + id + ", username=" + username + ", name=" + name + ", address=" + address + ", nationalInsuranceNumber=" + ninumber + ", salary=" + salary + ", isManager=" + manager + '}';
    }
}
