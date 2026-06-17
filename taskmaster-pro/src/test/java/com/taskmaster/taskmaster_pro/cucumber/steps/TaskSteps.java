package com.taskmaster.taskmaster_pro.cucumber.steps;

import com.taskmaster.taskmaster_pro.cucumber.config.CucumberSpringConfig;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;

import static org.hamcrest.Matchers.*;

public class TaskSteps extends CucumberSpringConfig {

    private Response response;
    private String requestBody;
    private Long createdTaskId;

    @Given("I have a task payload with title {string}, priority {string}, deadline {string}")
    public void iHaveTaskPayload(String title, String priority, String deadline) {
        requestBody = String.format(
                "{\"title\":\"%s\",\"priority\":\"%s\",\"deadline\":\"%s\"}",
                title, priority, deadline
        );
    }

    @When("I send a POST request to {string}")
    public void iSendPostRequestTo(String endpoint) {
        response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(endpoint)
                .then()
                .extract().response();
        if (response.statusCode() == HttpStatus.CREATED.value()) {
            createdTaskId = response.jsonPath().getLong("id");
        }
    }

    @When("I send a GET request to {string}")
    public void iSendGetRequestTo(String endpoint) {
        response = RestAssured.given()
                .when()
                .get(endpoint)
                .then()
                .extract().response();
    }

    @When("I send a GET request to the created task")
    public void iSendGetRequestToCreatedTask() {
        String endpoint = "/tasks/" + createdTaskId;
        iSendGetRequestTo(endpoint);
    }

    @When("I send a DELETE request to {string}")
    public void iSendDeleteRequestTo(String endpoint) {
        response = RestAssured.given()
                .when()
                .delete(endpoint)
                .then()
                .extract().response();
    }

    @When("I send a DELETE request to the created task")
    public void iSendDeleteRequestToCreatedTask() {
        String endpoint = "/tasks/" + createdTaskId;
        iSendDeleteRequestTo(endpoint);
    }

    @Then("the response status should be {int}")
    public void theResponseStatusShouldBe(int statusCode) {
        response.then().statusCode(statusCode);
    }

    @And("the response should contain the task title {string}")
    public void theResponseShouldContainTaskTitle(String title) {
        response.then().body("title", equalTo(title));
    }

    @And("the response should contain a list of tasks")
    public void theResponseShouldContainListOfTasks() {
        response.then().body("$", not(empty()));
    }

    @Given("there is at least one task in the system")
    public void thereIsAtLeastOneTask() {
        iHaveTaskPayload("Setup Task", "HIGH", "2026-12-31");
        iSendPostRequestTo("/tasks");
        response.then().statusCode(HttpStatus.CREATED.value());
        createdTaskId = response.jsonPath().getLong("id");
    }
}