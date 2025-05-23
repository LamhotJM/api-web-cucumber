package api.stepdefinitions;

import io.cucumber.java.en.*;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

/**
 * Step definitions for JSONPlaceholder User Management API tests.
 */
public class ApiUserStepDefinitions {
    private static final String BASE_URL = "https://jsonplaceholder.typicode.com"; // Base API URL
    private RequestSpecification spec; // Holds common request settings (base URI, path, headers)
    private Response response;         // Captures the response for each request

    /**
     * Prepare Rest-Assured to target the "/users" endpoint.
     * @param basePath the API path (here, "/users")
     */
    @Given("the JSONPlaceholder API is available at {string}")
    public void the_api_is_available_at(String basePath) {
        spec = new RequestSpecBuilder()                       // start building request spec
                .setBaseUri(BASE_URL)                        // set base URI for all requests
                .setBasePath(basePath)                       // set the common path "/users"
                .setContentType(ContentType.JSON)            // all payloads are JSON
                .build();                                    // finalize the specification
    }

    /**
     * Send a GET for a specific user.
     * @param userId the ID to fetch
     */
    @When("the client requests user with ID {string}")
    public void the_client_requests_user_with_ID(String userId) {
        response = given()                                   // use Rest-Assured DSL
                .spec(spec)                                  // apply our prepared spec
                .when()                                      // trigger the request
                .get("/" + userId);                          // GET /users/{userId}
    }

    /**
     * Send a GET to fetch all users.
     */
    @When("the client requests all users")
    public void the_client_requests_all_users() {
        response = given()                                   // start request
                .spec(spec)                                  // use same spec
                .when()                                      // trigger request
                .get();                                      // GET /users
    }

    /**
     * Send a POST to create a user.
     * @param name  new user's name
     * @param email new user's email
     */
    @When("the client creates a new user with name {string} and email {string}")
    public void the_client_creates_new_user(String name, String email) {
        String payload = "{ \"name\": \"" + name + "\", \"email\": \"" + email + "\" }"; // build JSON
        response = given()                                   // start request
                .spec(spec)                                  // apply spec
                .body(payload)                              // attach JSON body
                .when()                                      // trigger request
                .post();                                     // POST /users
    }

    /**
     * Send a DELETE to remove a user.
     * @param userId the ID to delete
     */
    @When("the client deletes the user with ID {string}")
    public void the_client_deletes_user(String userId) {
        response = given()                                   // start request
                .spec(spec)                                  // apply spec
                .when()                                      // trigger request
                .delete("/" + userId);                       // DELETE /users/{userId}
    }

    /**
     * Assert that the response HTTP status matches expectation.
     * @param expectedStatus the expected status code
     */
    @Then("the response status code should be {int}")
    public void the_response_status_code_should_be(Integer expectedStatus) {
        response.then()                                     // begin validation
                .statusCode(expectedStatus);                // assert status code
    }
}
