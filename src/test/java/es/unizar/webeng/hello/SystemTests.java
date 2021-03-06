package es.unizar.webeng.hello;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
/*
* Indicates that the class should use Spring's JUnit facilities. SpringJUnit4ClassRunner is a custom extension of JUnit's BlockJUnit4ClassRunner
* which provides functionality of the Spring TestContext Framework
*/

@RunWith(SpringJUnit4ClassRunner.class)
/*
* SpringApplicationConfiguration is a Class which especifies how to load and configure
* an ApplicationContext for integration tests.
*/
@SpringApplicationConfiguration(classes = Application.class)

/*
* @WebAppConfiguration must be present in order to tell Spring that a WebApplicationContext should be loaded for the test
*/
@WebAppConfiguration

// The application will start at a random free port, caching it throughout all unit tests
@IntegrationTest("server.port=0")

// Indicates that the ApplicationContext associated with a test is "dirty" and should therefore be closed and removed from the context cache
@DirtiesContext
/**
* SystemTests
*
* Program that performs the system tests of the application 'hello'
* System tests look for discrepancies between the program 
* and the objective or requirement , focusing on the mistakes made during 
* the transition process to design the functional specification.
* 
*/
public class SystemTests {

	// It will contain the random port
	@Value("${local.server.port}")
	private int port = 0;

	/**
	* Method that can be executed in order to test the connection to the Home page
	* If something goes wrong this method throws an Exception
	* @throws Exception
	*/
	@Test
	public void testHome() throws Exception {

		// Information given by a GET petition to the URL specified
		// is stored on an Entity
		ResponseEntity<String> entity = new TestRestTemplate().getForEntity(
				"http://localhost:" + this.port, String.class);

		//Check if the status is OK, this means code is 200 so is reachable
		assertEquals(HttpStatus.OK, entity.getStatusCode());
		
		//Check if the page's title starts with Hello, if it doesn't it throws an error
		assertTrue("Wrong body (title doesn't match):\n" + entity.getBody(), entity
				.getBody().contains("<title>Hello"));
	}

	/**
	* Method that can be executed in order to test the connection to styles sheet
	* If something goes wrong this method throws an Exception
	* @throws Exception
	*/
	@Test
	public void testCss() throws Exception {

		// Information given by a GET petition to the URL specified by the first parameter
		// is stored on an Entity
		ResponseEntity<String> entity = new TestRestTemplate().getForEntity(
				"http://localhost:" + this.port
						+ "/webjars/bootstrap/3.3.5/css/bootstrap.min.css", String.class);

		// Checkings to verify that it is available to connect, 
		// connection has been successful and it contains a correct css format
		assertEquals(HttpStatus.OK, entity.getStatusCode());
		// Checks if the body of the GET petition is correct (contains the word 'body').
		// If the verification is not positive it throws an error with the given message.
		assertTrue("Wrong body:\n" + entity.getBody(), entity.getBody().contains("body"));
		// Checks if the 'Content-type' field of the GET petition is correct.
		// If the verification is not positive it throws an error with the given message.
		assertEquals("Wrong content type:\n" + entity.getHeaders().getContentType(),
				MediaType.valueOf("text/css;charset=UTF-8"), entity.getHeaders()
						.getContentType());
	}	
	
	
}
