## Introduction

This report describes a simple Web Application written in using Spring Boot, which contains five vulnerabilities listed in the 2013 OWASP Top-10 (https://www.owasp.org/index.php/Top_10_2013-Top_10). The Application provides an (insecure) login page, followed by a very simplistic Web application that lists user details, adds notes, and changes the user's password.

Source code is hosted on GitHub at: https://github.com/bwritchie/cybersecuritybase-project

More information is given in `README.md`

## Getting Started

The username `dave` and password `password` can be used as login credentials,
or it is trivial to brute-force a password (e.g. using the hydra framework and a
list of 100 common passwords). The User database is reset on restart.

## Vulnerability 1: SQL Injection

#### Steps to reproduce:
1. Open http://localhost:8080/ in a browser
2. Enter username `dave` / `password` password and click submit
3. In the 'Change Password' section, enter `' OR TRUE;--` in the old password box
4. Enter any data text in the new password box, and click change password
5. The contents of the USERS table is displayed

#### Cause
The `ApplicationController.changePassword(...)` method attempts to validate existing credentials with a query

        String query = "SELECT * FROM Users where username = '" + username + "' and password = '" + oldpassword + "'";

This will return a single row if the credentials are correct, and nothing if they are wrong. However, this approach to writing the query permits SQL Injection via the `oldpassword` field.

#### Resolution
When validating existing credentials, prepared statements with parameterized queries should be used. Alternatively, the application could be rewritten to look up the username via JPA and query the password on the `User` object returned. Either approach would prevent the SQL injection attack from executing.

## Vulnerability 2: Cross-Site Scripting (XSS)

#### Steps to reproduce:
1. Open http://localhost:8080/ in a browser
2. Enter username `dave` / password `password` and click submit
3. In the 'Add a Note' section, enter `<script>alert("Hello, World!")</script>` in
the text box
4. Click "Add Note"
5. A Javascript Alert box pops up, indicating the script was executed

#### Cause
Notes are stored in a database table and are returned to the `employees.html` template when the page is rendered using Thymeleaf. The notes are listed as

    <li th:each="item : ${list}">
       <span th:utext="${item.note}">Notes</span></li>

The use of `th:utext` means that the text is not escaped, and the `<script>` tags are therefore interpreted literally, making the method vulnerable to XSS attacks.

#### Resolution
If the template is changed to use `th:text` rather than `th:utext`, then all text entered as a note is escaped before display. This prevents a `<script>` tag from being executed when the page is displayed, and the XSS attack does not execute.

## Vulnerability 3: Sensitive Data Exposure
---
#### Steps to reproduce:
1. Start Wireshark to capturing traffic on `TCP/port-8080`
2. Open http://localhost:8080/ in a browser
3. Enter username `dave` / password `password`
4. Observe that the password is displayed in plain text
5. Press Submit
6. Halt Wireshark packet capture
7. Enter wireshark filter `http && http.request.method == POST`
8. Observe that the HTTP POST includes an HTTP form with `username` and `password` entries listed in plain text
9. Execute the SQL injection attack listed as Vulnerability 1, and observe that the USERS table stores passwords in plain text.

#### Cause
The application makes no attempt to manage password data securely. Passwords are not obscured on the login page, are transmitted across the network in plain text, and are stored in the application database without being salted or hashed. As a result, credentials can be easily obtained.

#### Resolution
At a minimum, the login password form should specify `type="password"` rather than `type="text"` to obscure the password, the POST should be over HTTPS rather than HTTP to prevent passwords being visible in transit, and passwords should be salted and hashed with a secure algorithm (e.g. BCrypt) before being stored in the USERS table. However, in general the application introducing weaknesses by writing its own security: Spring Boot provides a comprehensive set of well-tested tools for providing user access control, and the application should be rewritten to use these tools.

## Vulnerability 4: Missing Function Level Access Control

#### Steps to reproduce:
1. From a Unix command line, run `curl --data "username=dave&note=hacked!" -v localhost:8080/addnote`
2. Open http://localhost:8080/ in a browser
3. Enter username `dave` / `password` password and click submit
4. The note will be displayed in the Employee Notes section

#### Cause
The application expects that the `/addnote` URL will only be invoked by a user who has logged in, via the "add note" form. However, there are no checks in the `ApplicationController.addNote(...)` method to ensure this is true, and the method can be used to assign notes to arbitrary users without authentication.

#### Resolution
This attack can be prevented by removing the call to `http.csrf().disable();` in the application's security configuration, which makes step 1 fail with `403 FORBIDDEN` due to the lack of a valid CSRF token. The application should also introduce full access control to the `/addnote` endpoint, to ensure that only valid entries are being processed.

## Vulnerability 5: Broken Authentication and Session Management

#### Steps to reproduce:
1. Open http://localhost:8080/ in a browser
2. Enter username `dave` / password `password` and click submit
3. Right click on the "Change Password" button and click on "Inspect"
4. Look for the text `<input type="hidden" name="username" value="dave">`
5. Change the value to another user, e.g. `value="joe"`
6. Enter `' OR TRUE;--` in the old password box
7. Enter any desired password in the new password box
8. Click on Change passwords
9. Non-manager `dave` has reset the password of the manager `joe`

#### Cause
Once a user is logged in, the `username` is passed as a hidden form property. However, this value can be edited client-side. Therefore while `ApplicationController.changePassword(...)` attempts to validate credentials (unlike vulnerability 4), it is possible to assert a different identity and change another user's password (here step 6 is used to bypass password verification, meaning that the existing password is not needed).

#### Resolution
The application must use session management that resists client-side changes. Spring Boot again provides tools to manage HTTP sessions and restrict access to methods, and these should be used by the application to ensure that password changes come from an authenticated user only.
