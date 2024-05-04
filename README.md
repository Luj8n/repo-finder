# GitHub Repository Finder

## Objective:

Develop a Spring Boot application that allows users to input a GitHub organization link and access token. Upon clicking the 'Go' button, the application should display all repositories of the specified organization and highlight repositories containing the word 'Hello' in their README.md file.

## Requirements:

- Java 21
- GitHub access token

## Usage:

1. Start the application.

```
./gradlew bootrun
```

or

```
./gradlew.bat bootrun
```

2. Open your web browser and navigate to `http://localhost:8080`.
3. Enter the GitHub organization link (or just the name) and access token in the provided input fields.
4. Click the 'Go' button.
5. View the list of repositories for the specified organization.
6. Repositories containing the word 'Hello' in their README.md file will be highlighted green.

## Technologies Used:

- Spring Boot
- Thymeleaf
- GitHub API
