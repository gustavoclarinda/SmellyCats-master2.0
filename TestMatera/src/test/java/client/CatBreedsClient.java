package client;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class CatBreedsClient {
    private final RequestSpecification requestSpecification;

    public CatBreedsClient(String baseUrl) {
        this.requestSpecification = new RequestSpecBuilder()
                .setBaseUri(baseUrl)
                .setBasePath("/breeds")
                .setRelaxedHTTPSValidation()
                .build();
    }

    public Response fetchBreeds(int limit) {
        return RestAssured
                .given(requestSpecification)
                .queryParam("limit", limit)
                .get();
    }
}
