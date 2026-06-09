# TaskMaster Pro

[![Java](https://img.shields.io/badge/Java-21-blue.svg)](https://adoptium.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.5-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Maven](https://img.shields.io/badge/Maven-3.9.6-orange.svg)](https://maven.apache.org/)
[![JaCoCo](https://img.shields.io/badge/JaCoCo-0.8.12-red.svg)](https://www.jacoco.org/jacoco/)

**REST API for high-performance task management** built to demonstrate solid software engineering practices, including efficient file processing, custom sorting, manual caching, unit testing, and SQL joins.

---

## 🚀 Features

* ✅ CRUD operations (create, list, retrieve, and delete tasks)
* ✅ Mass import of CSV and JSON files using streaming processing
* ✅ Custom sorting by priority or deadline
* ✅ Manual caching using `ConcurrentHashMap`
* ✅ Unit testing with JUnit 5 and Mockito
* ✅ Code coverage reporting with JaCoCo
* ✅ SQL JOIN demonstrations (`INNER JOIN` and `LEFT JOIN`)
* ✅ Composition over inheritance design
* ✅ RESTful API principles

---

## 🛠️ Tech Stack

| Layer           | Technology            |
| --------------- | --------------------- |
| Language        | Java 21               |
| Framework       | Spring Boot 3.4.5     |
| Database        | H2 Database           |
| Persistence     | Spring Data JPA       |
| Testing         | JUnit 5, Mockito      |
| Coverage        | JaCoCo                |
| Build Tool      | Maven                 |
| JSON Processing | Jackson Streaming API |
| CSV Processing  | BufferedReader        |

---

## 📦 Getting Started

### Prerequisites

* Java 21
* Maven 3.9+
* Git

### Clone the Repository

```bash
git clone https://github.com/ThiagoBarlanza/taskmaster-pro.git
cd taskmaster-pro
```

### Run the Application

Using Maven Wrapper:

```bash
./mvnw spring-boot:run
```

Or on Windows:

```cmd
mvnw.cmd spring-boot:run
```

The API will be available at:

```text
http://localhost:8080
```

---

## 📚 API Endpoints

### List All Tasks

```http
GET /tasks
```

Example:

```bash
curl http://localhost:8080/tasks
```

---

### Get Task By ID

```http
GET /tasks/{id}
```

Example:

```bash
curl http://localhost:8080/tasks/1
```

---

### Create Task

```http
POST /tasks
```

Request Body:

```json
{
  "title": "Finish README",
  "priority": "HIGH",
  "deadline": "2025-12-31"
}
```

Example:

```bash
curl -X POST http://localhost:8080/tasks \
-H "Content-Type: application/json" \
-d '{"title":"Finish README","priority":"HIGH","deadline":"2025-12-31"}'
```

---

### Delete Task

```http
DELETE /tasks/{id}
```

Example:

```bash
curl -X DELETE http://localhost:8080/tasks/1
```

---

### Sort Tasks

```http
GET /tasks/sorted?criteria=priority
```

or

```http
GET /tasks/sorted?criteria=deadline
```

Example:

```bash
curl "http://localhost:8080/tasks/sorted?criteria=priority"
```

---

### Import CSV File

```http
POST /tasks/import/csv
```

Example:

```bash
curl -F "file=@tasks.csv" \
http://localhost:8080/tasks/import/csv
```

---

### Import JSON File

```http
POST /tasks/import/json
```

Example:

```bash
curl -F "file=@tasks.json" \
http://localhost:8080/tasks/import/json
```

---

### Generate Random Tasks

```http
POST /tasks/generate?count=100
```

Example:

```bash
curl -X POST \
"http://localhost:8080/tasks/generate?count=100"
```

---

### Setup Demo Data

```http
POST /tasks/setup-demo
```

Example:

```bash
curl -X POST \
"http://localhost:8080/tasks/setup-demo"
```

---

### JOIN Demonstration

```http
GET /tasks/join-demo
```

Example:

```bash
curl http://localhost:8080/tasks/join-demo
```

---

## ⚡ Performance Considerations

### CSV Processing

CSV files are processed line by line using `BufferedReader`.

Benefits:

* Constant memory usage
* Suitable for large files
* Avoids loading the entire file into memory

### JSON Processing

JSON files are processed using Jackson Streaming API.

Benefits:

* Low memory consumption
* Faster processing for large datasets
* Scales significantly better than loading entire JSON arrays

---

## 🧠 Sorting Strategy

The application supports custom sorting through dedicated comparator classes.

### Priority Sorting

Order:

```text
HIGH > MID > LOW
```

Implemented using:

```java
Comparator<Task>
```

### Deadline Sorting

Tasks are sorted by ascending deadline date.

### Complexity

Java's TimSort is used internally:

```text
Time Complexity: O(n log n)
Space Complexity: O(n)
```

---

## 🗄️ Caching Strategy

A manual cache implementation is provided using:

```java
ConcurrentHashMap<Long, Task>
```

Benefits:

* O(1) average lookup time
* Reduced database access
* Demonstrates understanding of caching internals

Typical flow:

```text
Request
   ↓
Cache Lookup
   ↓
Hit → Return Cached Object
Miss → Query Database → Store in Cache
```

---

## 🧪 Running Tests

Run all tests:

```bash
./mvnw test
```

Generate coverage report:

```bash
./mvnw clean test jacoco:report
```

Coverage report location:

```text
target/site/jacoco/index.html
```

Target coverage:

```text
80%+
```

---

## 🔗 SQL JOIN Demonstration

The project includes a simple relationship between:

```text
User 1 ---- * Task
```

### INNER JOIN

Returns only tasks associated with existing users.

Example:

```sql
SELECT *
FROM users u
INNER JOIN tasks t
ON u.id = t.user_id;
```

### LEFT JOIN

Returns all tasks, even when no user is assigned.

Example:

```sql
SELECT *
FROM tasks t
LEFT JOIN users u
ON t.user_id = u.id;
```

---

## 📐 Design Decisions

### Why Composition Over Inheritance?

Instead of large inheritance hierarchies:

```text
TaskService
 ├─ CsvImportService
 ├─ JsonImportService
 ├─ TaskSorter
 └─ TaskCacheService
```

Benefits:

* Easier testing
* Better maintainability
* Lower coupling
* Higher flexibility

Each service has a single responsibility.

---

## 🎯 Interview Feedback Mapping

| Feedback Received                        | Solution Implemented                 |
| ---------------------------------------- | ------------------------------------ |
| Difficulty processing large text files   | Streaming CSV and JSON imports       |
| Limited discussion of performance        | Complexity analysis included         |
| Weak explanation of storage design       | Manual cache implementation          |
| Composition only understood conceptually | Entire architecture uses composition |
| Difficulty explaining GET vs POST        | Clear REST endpoint examples         |
| SQL JOIN knowledge                       | Live JOIN demonstration endpoint     |

---

## 📹 Suggested Demo Video Structure

### 1. Project Overview (30s)

* Explain the purpose of the API
* Show application startup

### 2. File Import (1 min)

* Upload CSV file
* Upload JSON file
* Explain streaming processing

### 3. Sorting & Complexity (1 min)

* Demonstrate priority sorting
* Explain O(n log n)

### 4. Cache Demonstration (1 min)

* Request same task twice
* Show cache hit and miss

### 5. SQL JOIN Demo (1 min)

* Create demo data
* Execute JOIN endpoint
* Explain INNER vs LEFT JOIN

### 6. Conclusion (30s)

* Summarize lessons learned
* Connect project to interview feedback

---

## 📄 License

This project was created for educational and portfolio purposes.

Feel free to use it as a reference for learning and interview preparation.

---

## 👨‍💻 Author

**Thiago Barlanza**

GitHub: https://github.com/ThiagoBarlanza
