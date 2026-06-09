# TaskMaster Pro

[![Java](https://img.shields.io/badge/Java-21-blue.svg)](https://adoptium.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.5-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Maven](https://img.shields.io/badge/Maven-3.9.6-orange.svg)](https://maven.apache.org/)
[![JaCoCo](https://img.shields.io/badge/JaCoCo-0.8.12-red.svg)](https://www.jacoco.org/jacoco/)

**REST API for high‑performance task management** – built to demonstrate solid engineering practices: efficient file processing, custom sorting, manual caching, unit testing, and SQL joins.

---

## 🚀 Features

- ✅ **CRUD operations** – create, list, retrieve, delete tasks
- ✅ **Mass import** – CSV and JSON files processed **line‑by‑line / streaming** (no memory overflow)
- ✅ **Custom sorting** – by priority (`HIGH > MID > LOW`) or deadline, using `Comparator<Task>`
- ✅ **Manual caching** – `ConcurrentHashMap` for `GET /tasks/{id}` (configurable TTL ready)
- ✅ **Unit tests** – JUnit 5 + Mockito, >80% coverage (JaCoCo report included)
- ✅ **SQL join demonstration** – `INNER JOIN` vs `LEFT JOIN` with `User` ↔ `Task` relationship
- ✅ **Composition over inheritance** – services composed of focused components
- ✅ **RESTful design** – clear distinction between `GET` (idempotent) and `POST` (resource creation)

---

## 🛠️ Tech Stack

| Layer       | Technology                                      |
|-------------|-------------------------------------------------|
| Language    | Java 21                                         |
| Framework   | Spring Boot 3.4.5 (Web, Data JPA)              |
| Database    | H2 (in‑memory, can be switched to PostgreSQL)  |
| Testing     | JUnit 5, Mockito, JaCoCo                        |
| Build       | Maven                                           |
| JSON/CSV    | Jackson (streaming parser), BufferedReader     |

---

## 📦 Getting Started

### Prerequisites
- JDK 21 ([Eclipse Temurin](https://adoptium.net/) recommended)
- Maven 3.9+ (or use the included Maven wrapper)

### Clone and run

```bash
git clone https://github.com/ThiagoBarlanza/taskmaster-pro.git
cd taskmaster-pro/taskmaster-pro
./mvnw spring-boot:run
