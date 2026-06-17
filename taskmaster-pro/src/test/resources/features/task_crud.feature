Feature: Task CRUD
  As a user of the TaskMaster API
  I want to manage tasks
  So that I can organize my work

  Background:
    Given that the API is running

  Scenario: Successfully create a new task
    Given I have a task payload with title "Implement Cucumber", priority "HIGH", deadline "2026-06-30"
    When I send a POST request to "/tasks"
    Then the response status should be 201
    And the response should contain the task title "Implement Cucumber"

  Scenario: List all tasks
    Given there is at least one task in the system
    When I send a GET request to "/tasks"
    Then the response status should be 200
    And the response should contain a list of tasks

  Scenario: Retrieve a task by ID
    Given there is at least one task in the system
    When I send a GET request to the created task
    Then the response status should be 200
    And the response should contain the task title "Setup Task"

  Scenario: Delete a task
    Given there is at least one task in the system
    When I send a DELETE request to the created task
    Then the response status should be 204