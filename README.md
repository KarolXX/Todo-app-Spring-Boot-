# Todo-app-Spring-Boot
### Technologies:
* Spring Boot
* JPA - Hibernate
* flyway for db migrations
* MySQL
* Keycloak for base authorization and authentication

### Interesting concepts:
* Composition over inheritance
* Aspects
* Events


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
