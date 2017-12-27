/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sec.project.controller;

import java.util.UUID;
import org.junit.Test;
import org.fluentlenium.adapter.FluentTest;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import sec.project.repository.UserRepository;

/**
 *
 * @author BenR
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ApplicationControllerTest extends FluentTest {

    public WebDriver webDriver;

    @Autowired
    private WebApplicationContext webAppContext;

    @Autowired
    private MockHttpSession mockSession;

    @Autowired
    private UserRepository userRepository;

    @LocalServerPort
    private Integer port;

    private MockMvc mockMvc;

    public ApplicationControllerTest() {
        super();
        this.webDriver = new HtmlUnitDriver(false);
    }

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
    }
    
    @Test
    public void testIndexShownAtRoot() {
        goTo("http://localhost:" + port + "/");
        assertThat(pageSource()).contains("login");
    }
    
    @Test
    public void testRandomLoginFails() throws Throwable {
        
        // login
        String username = UUID.randomUUID().toString().substring(0, 6);
        String password = UUID.randomUUID().toString().substring(0, 8);

//        // should not be able to login as a random user
//        mockMvc.perform(post("/login").session(mockSession).param("name", username).param("password", password)).andReturn();

    }
}
