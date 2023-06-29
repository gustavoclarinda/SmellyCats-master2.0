Feature: Testing the API data from the breeds

  Scenario: Get information on the two most popular cat breeds
    Given the endpoint "https://catfact.ninja/breeds?limit=2" is available
    When I send a GET request to the endpoint
    Then the response should have status code 200
    And the response should contain information about 2 cat breeds

  Scenario: Check if the maximum limit of breeds returned is respected
    Given the endpoint "https://catfact.ninja/breeds?limit=2" is available
    When I send a GET request to the endpoint
    Then the response should have status code 200
    And the response should contain at most 2 cat breeds
