package sec.project.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import sec.project.domain.Employee;
import sec.project.domain.Note;
import sec.project.domain.User;
import sec.project.repository.EmployeeRepository;
import sec.project.repository.NoteRepository;
import sec.project.repository.UserRepository;

/**
 * Deliberately insecure Spring Boot Application Controller class. This is a 
 * horrible implementation to permit various attacks on the system. Error 
 * handling is minimal, and there is no attempt to block CSRF / XSS etc. There
 * is no method authentication, and the login prompt can be trivially bypassed
 *
 * The controller links to three JPA repositories, containing user account 
 * details, employee details, and employee notes. These are populated on 
 * application startup via an import.sql script, a real application would link
 * to a permanent database. 
 *
 * Provides URL mapping for login, employee, and addNote endpoints
 * 
 * @author BenR
 */
@Controller
public class ApplicationController {

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private NoteRepository noteRepository;
    
    // this JDBC template is used to permit a SQL injection attack on 
    // the password change functionality
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Default request mapping gets sent to the login page
     * 
     * @return a redirect to the login page
     */
    @RequestMapping("*")
    public String defaultMapping() {
        return "redirect:/login";
    }

    /**
     * Handle GET requests to the /login endpoint at the start of login
     * 
     * @return the login template 
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String getLoginPage() {
        return "login";
    }

    /**
     * Handle POST requests to the /login endpoint to validate the supplied
     * credentials
     * 
     * @param model autowired Model 
     * @param username username supplied by the user
     * @param password password supplied the user
     * @return the employees page if login was successful
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String submitLogin(Model model, @RequestParam String username, @RequestParam String password) {
        // This is very basic login functionality. First look up the supplied
        // username, this will get zero or one users back from the User 
        // repository depending on whether or not the user exists. 
        User u = userRepository.findByUsername(username);
        // if we have a user, then get the password
        String pwd = (u != null ? u.getPassword() : null);
        // and check that the password matches the supplied value
        if (pwd != null && pwd.equals(password)) {
            // if so, populate the Model with user data
            populateModel(username, model);
            // and return the employees template page
            return "employees";
        } else {
            // otherwise, go back to login
            model.addAttribute("message", "incorrect username or password");
            return "login";
        }
    }

     /**
     * Handle GET requests to the /employees endpoint after login
     * 
     * @param model autowired Model 
     * @return the employees template 
     */
    @RequestMapping(value = "/employees", method = RequestMethod.GET)
    public String getEmployeesPage(Model model) {
        return "employees";
    }

    /**
     * Intentionally horrible implementation of change password functionality.
     * This does almost no validation, and uses an insecure SELECT via JDBC
     * in order to permit SQL injection. Unnecessary data is then returned to 
     * show the attacker the results of the injected query. 
     * 
     * Apart from that, it's great.      * 
     * 
     * @param model autowired Model
     * @param username username supplied by the user
     * @param oldpassword the existing password 
     * @param newpassword the new password
     * @return a confirmation message
     */
    
    @RequestMapping(value = "/employees", method = RequestMethod.POST)
    public String changePassword(Model model, @RequestParam String username, @RequestParam String oldpassword, @RequestParam String newpassword) {

        // construct the query in an intentionally-bad way
        String query = "SELECT * FROM Users where username = '" + username + "' and password = '" + oldpassword + "'";
        System.out.println("Executing: "+query);
        // unnecessary return to a a List to allow the injection attack 
        List l = jdbcTemplate.queryForList(query);
        // add the results (should just be a confirmation message really)
        populateModel(username, model);
        model.addAttribute("update", l);
        // and direct back to the employees page
        return "employees";
    }

    /**
     * Method to add an employee note. This is again deliberately coded badly,
     * to provide no authentication. Notes can therefore be added just by 
     * asserting a username. 
     * 
     * The lack of error handling probably means that this can be used for 
     * username enumeration too     * 
     * 
     * @param model autowired Model
     * @param username username supplied by the user
     * @param note the note to add
     * @return the employees page with the note added
     */
    @RequestMapping(value = "/addnote", method = RequestMethod.POST)
    public String addNote(Model model, @RequestParam String username, @RequestParam String note) {
        // no authentication. Save the note to the repository 
        noteRepository.save(new Note(username, note));
        // repopulate the Model
        populateModel(username, model);
        // and return the employees page
        return "employees";
    }

    /**
     * Utility method that populates the Model prior to a template page being
     * returned
     *
     * TODO: this should have some error handling
     * 
     * @param username
     * @param model
     */
    private void populateModel(String username, Model model) {
        // add username to the model
        model.addAttribute("username", username);
        // lookup employee details from username
        Employee e = employeeRepository.findByUsername(username);

        // if they are a manager, list all employee details, otherwise
        // just list the details of the employee
        List<Employee> details = new ArrayList<>();
        if (e.getManager()) {
            details = employeeRepository.findAll();
        } else {
            details.add(e);
        }
        model.addAttribute("details", details);
        // and add all the employee notes
        model.addAttribute("list", noteRepository.findByUsername(username));
    }
}
