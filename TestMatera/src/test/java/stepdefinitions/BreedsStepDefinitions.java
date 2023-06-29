package stepdefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Assert;

public class BreedsStepDefinitions {
    private String endpointUrl;
    private Response response;

    @Given("the endpoint {string} is available")
    public void setEndpointUrl(String url) {
        this.endpointUrl = url;
    }

    @When("I send a GET request to the endpoint")
    public void sendGETRequest() {
        response = RestAssured.get(endpointUrl);
    }

    @Then("the response should have status code {int}")
    public void verifyStatusCode(int expectedStatusCode) {
        int actualStatusCode = response.getStatusCode();
        Assert.assertEquals(expectedStatusCode, actualStatusCode);
    }

    @Then("the response should contain information about {int} cat breeds")
    public void verifyCatBreeds(int expectedCount) {
        int actualCount = response.jsonPath().getList("data").size();
        Assert.assertEquals(expectedCount, actualCount);
    }

    @Then("the response should contain at most {int} cat breeds")
    public void verifyMaximumCatBreeds(int expectedCount) {
        int actualCount = response.jsonPath().getList("data").size();
        Assert.assertTrue(actualCount <= expectedCount);
    }
}