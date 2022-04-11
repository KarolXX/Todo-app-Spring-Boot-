# ATTENTION: I started building this project when I wasn't very familiar with GIT 
# so it was not without side effect :(. 
# For this reason, most of the commits have been pushed to a branch named "new_branch"
# although it should be in the master branch. SO CHECK OUT "new_branch" branch.

In this project I use:
* Spring Boot 2.4.3
* h2 database (kept in file)
* JPA (Hibernate ORM)
* flyway for DB migrations
* Keycloak (basic, simple use)
* Thymeleaf and JavaScript for Front-End

It's not a standard todoapp. Besides tasks, there are groups and projects
*Group is just a group of tasks
*Project is used to create groups of tasks that are repeated from time to time.
 In other words it's a template for creating group to avoid writing it from sratch from time to time.
 ![image](https://user-images.githubusercontent.com/71709330/162849924-fb176b0a-9e8b-4e56-98c9-ad224e62a97a.png)


This project was made for educational purposes so some features like
filter and interceptor that only logs the request method and URI
may seem nonsensial
