package com.udacity.pricing.controller;

import com.jayway.jsonpath.JsonPath;
import com.udacity.pricing.entity.Price;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasProperty;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PricingControllerIntegrationTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void givenNoParameter_whenGetAllPrices_returnStatusCodeSuccessAndPageAndSize() {
        ResponseEntity<Object> response = this.testRestTemplate.getForEntity("http://localhost:" + this.port + "/prices", Object.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(JsonPath.read(response.getBody(), "$._embedded.prices.length()"), equalTo(20));
        assertThat(JsonPath.read(response.getBody(), "$.page.size"), equalTo(20));
        assertThat(JsonPath.read(response.getBody(), "$.page.totalElements"), equalTo(20));
        assertThat(JsonPath.read(response.getBody(), "$.page.totalPages"), equalTo(1));
    }

    @Test
    public void givenPageAndPageSize_whenGetAllPrices_returnStatusCodeSuccessAndPageAndSizeAndListPricing() {
        URI uri = UriComponentsBuilder.fromHttpUrl("http://localhost:" + this.port)
                .path("/prices")
                .queryParam("page", "0")
                .queryParam("size", "5")
                .build()
                .toUri();
        ResponseEntity<Object> response = this.testRestTemplate.getForEntity(uri, Object.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(JsonPath.read(response.getBody(), "$._embedded.prices.length()"), equalTo(5));
        assertThat(JsonPath.read(response.getBody(), "$.page.size"), equalTo(5));
        assertThat(JsonPath.read(response.getBody(), "$.page.totalElements"), equalTo(20));
        assertThat(JsonPath.read(response.getBody(), "$.page.totalPages"), equalTo(4));
    }

    @Test
    public void givenInvalidPage_whenGetAllPrices_returnStatusSuccessAndPageAndSizeAndEmptyListPricing() {
        URI uri = UriComponentsBuilder.fromHttpUrl("http://localhost:" + this.port)
                .path("/prices")
                .queryParam("page", "10000")
                .queryParam("size", "5")
                .build()
                .toUri();
        ResponseEntity<Object> response = this.testRestTemplate.getForEntity(uri, Object.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(JsonPath.read(response.getBody(), "$._embedded.prices.length()"), equalTo(0));
        assertThat(JsonPath.read(response.getBody(), "$.page.size"), equalTo(5));
        assertThat(JsonPath.read(response.getBody(), "$.page.totalElements"), equalTo(20));
        assertThat(JsonPath.read(response.getBody(), "$.page.totalPages"), equalTo(4));
    }

    @Test
    public void givenVehicleId_whenGetPrice_returnStatusSuccessAndContainFieldInObject() {
        URI uri = UriComponentsBuilder.fromHttpUrl("http://localhost:" + this.port)
                .path("/prices")
                .path("/1")
                .build()
                .toUri();
        ResponseEntity<Price> response = this.testRestTemplate.getForEntity(uri, Price.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(response.getBody(), hasProperty("id"));
        assertThat(response.getBody(), hasProperty("currency"));
        assertThat(response.getBody(), hasProperty("price"));
        assertThat(response.getBody(), hasProperty("vehicleId"));
    }

    @Test
    public void givenVehicleId_whenGetPrice_returnStatusSuccessAndDataNotNull() {
        URI uri = UriComponentsBuilder.fromHttpUrl("http://localhost:" + this.port)
                .path("/prices")
                .path("/1")
                .build()
                .toUri();
        ResponseEntity<Price> response = this.testRestTemplate.getForEntity(uri, Price.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(response.getBody(), hasProperty("id", notNullValue()));
        assertThat(response.getBody(), hasProperty("currency", notNullValue()));
        assertThat(response.getBody(), hasProperty("price", notNullValue()));
        assertThat(response.getBody(), hasProperty("vehicleId", notNullValue()));
    }

    @Test
    public void givenInvalidVehicleId_whenGetPrice_returnNotFoundStatus() {
        URI uri = UriComponentsBuilder.fromHttpUrl("http://localhost:" + this.port)
                .path("/prices")
                .path("/10000")
                .build()
                .toUri();
        ResponseEntity<Price> response = this.testRestTemplate.getForEntity(uri, Price.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
    }
}
