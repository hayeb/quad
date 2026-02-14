# OpenTrivia app

Very simple app that allows answering questions from [OpenTrivia](https://opentdb.com/) without any cheating by checking the
answers.

## Building and running backend

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

## Building and running frontend 
1. Install [Node Version Manager](https://github.com/nvm-sh/nvm)
2. Install node 24:
   ```shell
   nvm install 24
   ````
4. Go into frontend and install dependencies
   ```shell
   cd frontend
   npm i
   ```
5. Start the Angular dev server
   ```shell
   cd frontend
   npx ng serve
   ```

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

## Possible improvements
* Use OpenTrivia session tokens in some way to prevent duplicate questions
* Store answers in a persistent database
* Add OpenAPI endpoint/schema