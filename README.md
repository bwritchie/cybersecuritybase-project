# cybersecuritybase-project
Template for the first course project.

This is a simple and intentionally broken Spring Boot application written for the CyberSecurity Base Course Project I (see https://cybersecuritybase.github.io/). It (intentionally) contains five of the OWASP Top-10 Web Security vulnerabilities:

* **A1 - Injection**: it is possible to carry out a SQL injection attack using the change password form
* **A2 - Broken Authentication and Session Management**: there isn't much in the way of meaningful session management, so this is broken
* **A3 - Cross-Site Scripting**: you can carry out an XSS attack using the Add Note form
* **A6 - Sensitive Data Exposure**: credentials are passed in plaintext and are not stored securely
* **A7 - Missing Function Level Access Control**: no proper access control is carried out on management and note functionality

There are also some more more (including **A5 - Security Misconfiguration** as much of the Spring Boot security defaults are disabled, and
it is almost certainly possible to carry out **A8 - Cross-Site Request Forgery (CSRF)** as I configured CSRF protection off). In fact, this
probably allows all of the top ten in one way or another...

## Intended functionality

The intended use-cases are as follows:

* HTTP requests to unknown URLs are redirected to ***/login***
* ***/login*** verifies username and password information against data stored in the Users table
* ***/employees*** provides basic employee management functionality:
  * if the Employee is not a manager, only list their details from the Employees table
  * otherwise, if they are a manager, list all Employees
  * list any Notes about the Employee from the Notes table
  * add new Notes and store them in the Notes table
  * allow the Employee to change their password, and store the changed password in the Notes table
  
Due to the intentionally-broken nature of the application, none of these functions work quite as intended (there are probably also a number of bugs as well - I was learning Spring Boot, JPA, and Thymeleaf as I went along, and some of the coding is probably 'non-optimal' ;) ). 

## Getting Started

You can use dave / password (non-manager) or joe / letmein (manager) usernames / passwords to get logged in

## Intentional attacks

* The change password form permits a SQL injection attack (e.g. enter **' OR TRUE;select * from Users;--'** in the old password field ) 
* The add note form permits a XSS attack (e.g. enter **<code>alert("Hello, World!")</code>** as a Note)
* There is no authentication on methods, so you can add notes without knowing the User's password (try **curl -- data "username=dave&amp;note=Hacked!" localhost:8080/addnote** from the command line, or combine with the XSS attack above) 
* Credentials are not stored securely, and can be captured during the HTTP POST on login or by doing a SQL injection attack to list the Users table
* Some of the HTTP post information may be trivially manipulated on the client side

## bugs and broken stuff

Please let me know if you find anything broken and i'll fix it. 