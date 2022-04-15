# Todo-app-Spring-Boot

### Technologies:
* Java v. 11
* Spring Boot v. 2.4.3 
* JPA - Hibernate
* flyway for db migrations v. 6.5.7
* h2 - for database kept in file
* Thymeleaf v. 3.0.4.RELEASE, HTML with vanilla Javascript - for frontend
* Keycloak v. 13.0.1 - for base authorization and authentication


### Interesting concepts:
* Composition over inheritance `@Embeddable`, `@Embedded`
* Aspects
* Events
* LDAP
* Hiding methods from JPA and exposing only relevant ones - take a look for example at SqlProjectRepository and ProjectRepository


It's advanced todoapp. 
In addition to standard tasks, there are also:
* groups
* projects

## Task
Entity that contains fields: id, description, done, deadline, group (association)

![image](https://user-images.githubusercontent.com/71709330/163211568-3616b7c2-9d62-4b2c-bf8c-57e0c35bcf3c.png)


## Group
Entity that contains: set of tasks (association), `@NotBlank` description, id, done.
### Group rules:
* group is be done only if all associated tasks are done
* group deadline (property that is available only in GroupReadModel DTO) is equal to the latest task in group

![image](https://user-images.githubusercontent.com/71709330/163211486-27c007ce-61cf-44ec-befc-73c8f3dc12c9.png)


## Project
A project is something like a group template that you create from time to time, such as a group when you go on a trip. Suppose the client goes on a trip three times a year, so instead of creating this group from scratch, the client can create a project that will only be updated with a new date and possibly new tasks before creating the new group from the exisitng project
### Project rules:
* whether there can be two or more undone groups from the same project at once depends on `task.template.allowMultipleTasks=true` from `application.properties`
* layout that contains projects in available only for logged in users with role `ROLE_ADMIN`

## Other information about this project:
In this app I did not divide classes into packages by domains (task, group, project) as usual I prefer to do it, only by class roles (controller, logic, model)
