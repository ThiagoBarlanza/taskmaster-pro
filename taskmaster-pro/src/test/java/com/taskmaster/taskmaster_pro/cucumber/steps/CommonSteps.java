package com.taskmaster.taskmaster_pro.cucumber.steps;

import com.taskmaster.taskmaster_pro.cucumber.config.CucumberSpringConfig;
import io.cucumber.java.en.Given;
import io.restassured.RestAssured;

public class CommonSteps extends CucumberSpringConfig {

    @Given("that the API is running")
    public void theApiIsRunning() {
        RestAssured.baseURI = getBaseUrl();
        RestAssured.port = port;
    }
}