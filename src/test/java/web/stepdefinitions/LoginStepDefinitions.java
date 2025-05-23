package web.stepdefinitions;

import io.cucumber.java.en.*;
import org.junit.Assert;
import web.pages.LoginPage;
import web.utils.Hooks;

/**
 * Step definitions for DemoBlaze User Authentication scenarios,
 * using the new LoginPage API (openLoginModal(), login(), logout(), etc.).
 */
public class LoginStepDefinitions {
    private final LoginPage loginPage = new LoginPage(Hooks.getDriver());

    /**
     * Opens the DemoBlaze home page and the login dialog.
     */
    @Given("the user is on the DemoBlaze homepage")
    @And("the login dialog is open")
    public void openHomeAndLoginDialog() {
        loginPage.openLoginModal();
    }

    /**
     * Performs the full login flow with the given credentials.
     * @param username credential
     * @param password credential
     */
    @When("the user logs in with username {string} and password {string}")
    public void user_logs_in(String username, String password) {
        loginPage.login(username, password);
    }

    /**
     * Verifies successful login by checking for the “Welcome” label.
     */
    @Then("the user should be redirected to the homepage")
    public void verifySuccessfulLogin() {
        Assert.assertTrue(
                "Expected user to be logged in, but they were not.",
                loginPage.isUserLoggedIn()
        );
    }

    /**
     * Verifies that either an alert or inline error is shown.
     */
    @Then("an error message should be displayed")
    public void verifyErrorDisplayed() {
        Assert.assertTrue(
                "Expected an error message or alert, but none was shown.",
                loginPage.isErrorMessageDisplayed()
        );
    }

    /**
     * Ensures a valid user is already logged in (for logout scenarios).
     */
    @Given("the user is logged in with valid credentials")
    public void ensureUserIsLoggedIn() {
        // Reuse the loginPage flow
        loginPage
                .openLoginModal()
                .login("lamhot_auto", "Querty@2023");
        Assert.assertTrue(
                "Setup failure: login did not succeed.",
                loginPage.isUserLoggedIn()
        );
    }

    /**
     * Clicks the logout button.
     */
    @When("the user clicks the logout button")
    public void clickLogout() {
        loginPage.logout();
    }

    /**
     * Verifies logout by checking that the login nav button is visible again.
     */
    @Then("the login button should be visible on the page")
    public void verifyLogoutSuccess() {
        Assert.assertFalse(
                "Expected login button to be visible after logout.",
                loginPage.isUserLoggedIn()
        );
    }
}
