Feature: Cat breeds API contract
  A consumer can retrieve curated information about cat breeds from the API.

  Background:
    Given the cat breeds endpoint is available

  Scenario Outline: Retrieve a limited set of cat breeds
    When I request the first <limit> cat breeds
    Then the response should have status code 200
    And the response should contain at most <limit> cat breeds
    And each breed entry should provide name and origin data

    Examples:
      | limit |
      | 2     |
      | 4     |
