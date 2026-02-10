# OpenTrivia app

Very simple app that allows answering questions from [OpenTrivia](https://opentdb.com/) without any cheating by checking the
answers.

## Building

1. Ensure JDK 25 and Maven are installed and available on PATH
2. Run Maven build:
   ```shell
    mvn compile
   ```
3. Use SpringBoot plugin to run locally:
   ```shell
    mvn spring-boot:run
   ```
   
   You can override the OpenTrivia endpoint or the file used for answers storage: 
   ```shell
   mvn spring-boot:run -Dspring-boot.run.arguments='--answersLocation=answers-new.json'
   ``` 
4. In browser, navigate to http://localhost:8080/

## Testing
1. Run the tests using Maven:
   ```shell
    mvn test
   ```

## Implementation notes

1. OpenTrivia does not provide question IDs in their response, so we use the text as ID.
2. OpenTrivia does not implement a check API, so whenever we retrieve questions we also store the answers in a local
   store so they may be checked later
    1. This store is currently a file with JSON contents (PoC, MVP). Should be replaced with proper persistence (Redis,
       SQLite, ...). An attempt is made to allow multithreading, but no hard guarantees are given
3. Frontend it a simple static HTML page.

## Possible improvements
* Handle OpenTrivia rate limiting
* Use OpenTrivia session tokens in some way to prevent duplicate questions
* Store answers in a persistent database
* Dynamically download categories from OpenTrivia and display in category selection input
  * Also display category question counts?
* Add OpenAPI endpoint/schema