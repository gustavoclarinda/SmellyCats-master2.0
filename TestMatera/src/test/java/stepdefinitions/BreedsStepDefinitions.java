package stepdefinitions;

import client.CatBreedsClient;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.junit.Assert;
import support.CatBreedsApiStub;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class BreedsStepDefinitions {
    private final CatBreedsApiStub apiStub = new CatBreedsApiStub();
    private CatBreedsClient catBreedsClient;
    private Response response;

    @Before
    public void setUp() throws IOException {
        apiStub.start();
        catBreedsClient = new CatBreedsClient(apiStub.getBaseUrl());
    }

    @After
    public void tearDown() {
        apiStub.stop();
        response = null;
    }

    @Given("the cat breeds endpoint is available")
    public void theCatBreedsEndpointIsAvailable() {
        Assert.assertTrue("Stub server should be running before requests are issued", apiStub.isRunning());
    }

    @When("I request the first {int} cat breeds")
    public void iRequestTheFirstCatBreeds(int limit) {
        response = catBreedsClient.fetchBreeds(limit);
    }

    @Then("the response should have status code {int}")
    public void verifyStatusCode(int expectedStatusCode) {
        Assert.assertNotNull("No response was captured for validation", response);
        Assert.assertEquals("Unexpected HTTP status", expectedStatusCode, response.getStatusCode());
    }

    @Then("the response should contain at most {int} cat breeds")
    public void verifyMaximumCatBreeds(int expectedCount) {
        List<Map<String, Object>> breeds = response.jsonPath().getList("data");
        Assert.assertNotNull("Breed list should be present in the response body", breeds);
        Assert.assertFalse("Breed list should not be empty", breeds.isEmpty());
        Assert.assertTrue(String.format("Expected at most %d breeds but got %d", expectedCount, breeds.size()),
                breeds.size() <= expectedCount);
    }

    @Then("each breed entry should provide name and origin data")
    public void verifyBreedAttributes() {
        List<Map<String, Object>> breeds = response.jsonPath().getList("data");
        Assert.assertNotNull("Breed list should be present in the response body", breeds);

        for (Map<String, Object> breed : breeds) {
            Assert.assertTrue("Breed entry should contain the breed name", breed.containsKey("breed"));
            Assert.assertTrue("Breed entry should contain the origin", breed.containsKey("origin"));
            Assert.assertTrue("Breed entry should contain the country", breed.containsKey("country"));
            Assert.assertTrue("Breed entry should contain the coat", breed.containsKey("coat"));
            Assert.assertTrue("Breed entry should contain the pattern", breed.containsKey("pattern"));

            Assert.assertTrue("Breed name should not be blank", isFilled(breed.get("breed")));
            Assert.assertTrue("Origin should not be blank", isFilled(breed.get("origin")));
            Assert.assertTrue("Country should not be blank", isFilled(breed.get("country")));
            Assert.assertTrue("Coat should not be blank", isFilled(breed.get("coat")));
            Assert.assertTrue("Pattern should not be blank", isFilled(breed.get("pattern")));
        }
    }

    private boolean isFilled(Object value) {
        return value != null && !Objects.toString(value).isBlank();
    }
}
